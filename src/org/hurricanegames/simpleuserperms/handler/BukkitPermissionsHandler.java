package org.hurricanegames.simpleuserperms.handler;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.storage.User;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

public class BukkitPermissionsHandler {

	protected static final Field permissionsMapField = getPermissionAttachmentPermissionsMapField();

	protected static Field getPermissionAttachmentPermissionsMapField() {
		try {
			Field field = PermissionAttachment.class.getDeclaredField("permissions");
			field.setAccessible(true);
			return field;
		} catch (Throwable e) {
			return null;
		}
	}

	protected final SimpleUserPerms plugin;
	protected final UsersStorage users;

	public BukkitPermissionsHandler(SimpleUserPerms plugin, UsersStorage users) {
		this.plugin = plugin;
		this.users = users;
	}

	protected final Map<Player, PermissionAttachment> attachments = new ConcurrentHashMap<>();

	public void updatePermissions() {
		Bukkit.getOnlinePlayers().forEach(this::updatePermissions);
	}

	public void updatePermissions(OfflinePlayer player) {
		Player onlinePlayer = player.getPlayer();
		if (onlinePlayer != null) {
			updatePermissions(onlinePlayer);
		}
	}

	public void updatePermissions(Player player) {
		User user = users.getUser(player.getUniqueId());
		PermissionAttachment attachment = attachments.computeIfAbsent(player, k -> player.addAttachment(plugin));
		try {
			@SuppressWarnings("unchecked")
			Map<String, Boolean> permissions = (Map<String, Boolean>) permissionsMapField.get(attachment);
			permissions.clear();
			Map<String, Boolean> effectivePermissions = user.getEffectivePermissions();
			permissions.putAll(effectivePermissions);
			if (Boolean.TRUE.equals(effectivePermissions.get("*"))) {
				plugin.getServer().getPluginManager().getPermissions().forEach(permission -> permissions.put(permission.getName(), Boolean.TRUE));
			}
			attachment.getPermissible().recalculatePermissions();
		} catch (Throwable e) {
			plugin.getLogger().log(Level.WARNING, "Unable to set direct permissions, using slower method", e);
			for (Map.Entry<String, Boolean> effPermsEntry : user.getEffectivePermissions().entrySet()) {
				attachment.setPermission(effPermsEntry.getKey(), effPermsEntry.getValue());
			}
		}
	}

	public void cleanupPermissions(Player player) {
		attachments.remove(player);
	}

	public void removePermissions() {
		Iterator<Map.Entry<Player, PermissionAttachment>> iterator = attachments.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Player, PermissionAttachment> entry = iterator.next();
			iterator.remove();
			entry.getKey().removeAttachment(entry.getValue());
		}
	}

}
