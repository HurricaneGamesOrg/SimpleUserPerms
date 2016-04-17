package simpleuserperms.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import simpleuserperms.SimpleUserPerms;

public class Group {

	private final String name;
	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	protected final Set<Group> parentGroups = Collections.newSetFromMap(new ConcurrentHashMap<>());
	protected final Set<String> permissions = Collections.newSetFromMap(new ConcurrentHashMap<>());
	protected String prefix = "";

	public List<Group> getParentGroups() {
		return new ArrayList<Group>(parentGroups);
	}

	public boolean hasParentGroup(Group group) {
		return parentGroups.contains(group);
	}

	public void addParentGroup(Group group) {
		addParentGroup(group, true);
	}

	public void addParentGroup(Group group, boolean updateNow) {
		parentGroups.add(group);
		if (updateNow) {
			update();
		}
	}

	public void removeParentGroup(Group group) {
		removeParentGroup(group, true);
	}

	public void removeParentGroup(Group group, boolean updateNow) {
		parentGroups.remove(group);
		if (updateNow) {
			update();
		}
	}

	public boolean hasPermission(String permission) {
		return permissions.contains(permission);
	}

	public List<String> getPermissions() {
		return new ArrayList<String>(permissions);
	}

	public void addPermission(String permission) {
		addPermission(permission, true);
	}

	public void addPermission(String permission, boolean updateNow) {
		permissions.add(permission);
		if (updateNow) {
			update();
		}
	}

	public void removePermission(String permission) {
		removePermission(permission, true);
	}

	public void removePermission(String permission, boolean updateNow) {
		permissions.remove(permission);
		if (updateNow) {
			update();
		}
	}

	public void update() {
		UsersStorage users = SimpleUserPerms.getUsersStorage();
		Bukkit.getOnlinePlayers().forEach(player -> {
			users.getUser(player.getUniqueId()).recalculatePermissions();
		});
		DefaultUserPermsCache.recalculateDefaultPerms();
	}

	public String getPrefix() {
		if (prefix != null) {
			return prefix;
		}
		for (Group parent : parentGroups) {
			String pprefix = parent.getPrefix();
			if (pprefix != null) {
				return pprefix;
			}
		}
		return "";
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object otherObj) {
		if (!(otherObj instanceof Group)) {
			return false;
		}
		Group other = (Group) otherObj;
		return other.name.equals(name);
	}

}
