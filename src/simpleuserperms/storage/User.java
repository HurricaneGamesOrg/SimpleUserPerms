package simpleuserperms.storage;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import simpleuserperms.SimpleUserPerms;

public class User {

	protected Group group = SimpleUserPerms.getGroupsStorage().getDefaultGroup();
	protected final HashSet<Group> subGroups = new HashSet<>();
	protected final HashSet<String> additionalPerms = new HashSet<>();
	protected String prefix;

	protected final HashMap<String, Boolean> activePermissions = new HashMap<>();

	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	protected final ReadLock readLock = lock.readLock();
	protected final WriteLock writeLock = lock.writeLock();

	public User() {
		this(true);
	}

	protected User(boolean applyDefault) {
		if (applyDefault) {
			DefaultUserPermsCache.applyDefaultPermsTo(this);
		}
	}

	public void setMainGroup(Group group) {
		setMainGroup(group, true);
	}

	public void setMainGroup(Group group, boolean recalculateNow) {
		writeLock.lock();
		try {
			this.group = group;
			if (recalculateNow) {
				recalculatePermissions();
			}
		} finally {
			writeLock.unlock();
		}
	}

	public Group getMainGroup() {
		readLock.lock();
		try {
			return group;
		} finally {
			readLock.unlock();
		}
	}

	public void addSubGroup(Group group) {
		addSubGroup(group, true);
	}

	public void addSubGroup(Group group, boolean recalculateNow) {
		writeLock.lock();
		try {
			subGroups.add(group);
			if (recalculateNow) {
				recalculatePermissions();
			}
		} finally {
			writeLock.unlock();
		}
	}

	public void removeSubGroup(Group group) {
		removeSubGroup(group, true);
	}

	public void removeSubGroup(Group group, boolean recalculateNow) {
		writeLock.lock();
		try {
			subGroups.remove(group);
			if (recalculateNow) {
				recalculatePermissions();
			}
		} finally {
			writeLock.unlock();
		}
	}

	public boolean hasSubGroup(Group group) {
		readLock.lock();
		try {
			return subGroups.contains(group);
		} finally {
			readLock.unlock();
		}
	}

	public List<Group> getSubGroups() {
		readLock.lock();
		try {
			return new ArrayList<Group>(subGroups);
		} finally {
			readLock.unlock();
		}
	}

	public void addAdditionalPermission(String permission) {
		addAdditionalPermission(permission, true);
	}

	public void addAdditionalPermission(String permission, boolean recalculateNow) {
		writeLock.lock();
		try {
			additionalPerms.add(permission);
			if (recalculateNow) {
				calculatePermission(permission);
				update();
			}
		} finally {
			writeLock.unlock();
		}
	}

	public void removeAdditionalPermission(String permission) {
		removeAdditionalPermission(permission, false);
	}

	public void removeAdditionalPermission(String permission, boolean recalculateNow) {
		writeLock.lock();
		try {
			additionalPerms.remove(permission);
			if (recalculateNow) {
				calculatePermission(permission);
				update();
			}
		} finally {
			writeLock.unlock();
		}
	}

	public boolean hasAdditionalPermission(String permission) {
		readLock.lock();
		try {
			return additionalPerms.contains(permission);
		} finally {
			readLock.unlock();
		}
	}

	public List<String> getAdditionalPermissions() {
		readLock.lock();
		try {
			return new ArrayList<String>(additionalPerms);
		} finally {
			readLock.unlock();
		}
	}

	public String getRawPrefix() {
		return prefix;
	}

	public String getPrefix() {
		if (prefix != null) {
			return prefix;
		} else {
			return group.getPrefix();
		}
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	protected WeakReference<Player> player;

	public Player getPlayerRef() {
		if (player != null) {
			return player.get();
		}
		return null;
	}

	public void setPlayerRef(Player player) {
		this.player = new WeakReference<Player>(player);
	}

	public void clearPlayerRef() {
		this.player = null;
	}

	public void recalculatePermissions() {
		writeLock.lock();
		try {
			activePermissions.clear();
			calculateGroupPerms(group);
			subGroups.forEach(this::calculateGroupPerms);
			additionalPerms.forEach(this::calculatePermission);
		} finally {
			writeLock.unlock();
		}
		update();
	}

	protected void calculateGroupPerms(Group group) {
		group.permissions.forEach(this::calculatePermission);
		group.parentGroups.forEach(this::calculateGroupPerms);
	}

	protected void calculatePermission(String permission) {
		if (permission.startsWith("-")) {
			activePermissions.put(permission.substring(1, permission.length()), false);
		} else {
			activePermissions.put(permission, true);
		}
	}

	protected void update() {
		if (player == null) {
			return;
		}
		if (Bukkit.isPrimaryThread()) {
			Player playerr = player.get();
			if (playerr != null) { 
				SimpleUserPerms.getBukkitPermissions().updatePermissions(playerr);
			}
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(SimpleUserPerms.class), new Runnable() {
				@Override
				public void run() {
					if (player != null) {
						Player playerr = player.get();
						if (playerr != null) { 
							SimpleUserPerms.getBukkitPermissions().updatePermissions(playerr);
						}
					}
				}
			});
		}
	}

	public Map<String, Boolean> getEffectivePermissions() {
		readLock.lock();
		try {
			return new HashMap<String, Boolean>(activePermissions);
		} finally {
			readLock.unlock();
		}
	}

	public ReadLockedEffectivePermissions getDirectEffectivePermissions() {
		return new ReadLockedEffectivePermissions(activePermissions, readLock);
	}

	public static class ReadLockedEffectivePermissions implements AutoCloseable {

		private Map<String, Boolean> activePerms;
		private ReadLock readLock;

		protected ReadLockedEffectivePermissions(Map<String, Boolean> activePerms, ReadLock readLock) {
			this.activePerms = Collections.unmodifiableMap(activePerms);
			this.readLock = readLock;
			this.readLock.lock();
		}

		public Map<String, Boolean> getEffectivePermissions() {
			return activePerms;
		}

		public void unlock() {
			activePerms = null;
			readLock.unlock();
		}

		@Override
		public void close() {
			unlock();
		}

	}

}
