package simpleuserperms.handler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.User;
import simpleuserperms.storage.User.ReadLockedEffectivePermissions;

public class BukkitPermissions {

	private static final Field permissiosnMapField = getPermissionsMapField();

	private static Field getPermissionsMapField() {
		try {
			Field field = PermissionAttachment.class.getDeclaredField("permissions");
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			return null;
		}
	}

	private final SimpleUserPerms plugin;
	public BukkitPermissions(SimpleUserPerms plugin) {
		this.plugin = plugin;
	}

	private final HashMap<UUID, PermissionAttachment> attachments = new HashMap<UUID, PermissionAttachment>();

	public void updatePermissions() {
		Bukkit.getOnlinePlayers().forEach(this::updatePermissions);
	}

	public void updatePermissions(Player player) {
		UUID uuid = player.getUniqueId();
		PermissionAttachment attachment = attachments.get(uuid);
		if (attachment == null) {
			attachment = player.addAttachment(plugin);
			attachments.put(uuid, attachment);
		}
		try {
			@SuppressWarnings("unchecked")
			Map<String, Boolean> permissions = (Map<String, Boolean>) permissiosnMapField.get(attachment);
			permissions.clear();
			User user = SimpleUserPerms.getUsersStorage().getUser(uuid);
			try (ReadLockedEffectivePermissions effPerms = user.getDirectEffectivePermissions()) {
				permissions.putAll(effPerms.getEffectivePermissions());
				Boolean allPerms = effPerms.getEffectivePermissions().get("*");
				if (allPerms != null && allPerms) {
					Bukkit.getPluginManager().getPermissions().forEach((permission) -> permissions.put(permission.getName(), Boolean.TRUE));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		attachment.getPermissible().recalculatePermissions();
	}

	protected void cleanup(Player player) {
		attachments.remove(player.getUniqueId());
	}

}
