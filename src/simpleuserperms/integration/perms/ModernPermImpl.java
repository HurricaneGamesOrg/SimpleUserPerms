package simpleuserperms.integration.perms;

import org.bukkit.OfflinePlayer;

import simpleuserperms.SimpleUserPerms;
import simpleuserperms.integration.SharedUtils;
import simpleuserperms.storage.Group;
import simpleuserperms.storage.User;

public abstract class ModernPermImpl extends BasicPermImpl {

	@Override
	public boolean groupAdd(String worldName, String groupName, String permission) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group != null) {
			group.addPermission(worldName);
			return true;
		}
		return false;
	}

	@Override
	public boolean groupHas(String worldName, String groupName, String permission) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group != null) {
			return group.hasPermission(permission);
		}
		return false;
	}

	@Override
	public boolean groupRemove(String worldName, String groupName, String permission) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group != null) {
			group.removePermission(permission);
			return true;
		}
		return false;
	}

	@Override
	public boolean playerAdd(String world, OfflinePlayer player, String permission) {
		SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId()).addAdditionalPermission(permission);
		return true;
	}

	@Override
	public boolean playerRemove(String world, OfflinePlayer player, String permission) {
		User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
		if (user.hasAdditionalPermission(permission)) {
			user.removeAdditionalPermission(permission);
		} else {
			user.addAdditionalPermission("-"+permission);
		}
		return true;
	}

	@Override
	public boolean playerInGroup(String world, OfflinePlayer player, String groupName) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group == null) {
			return false;
		}
		User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
		return user.getMainGroup() == group || user.hasSubGroup(group);
	}

	@Override
	public boolean playerAddGroup(String world, OfflinePlayer player, String groupName) {
		User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group == null) {
			return false;
		}
		if (user.getMainGroup() == SimpleUserPerms.getGroupsStorage().getDefaultGroup()) {
			user.setMainGroup(group);
		} else {
			user.addSubGroup(group);
		}
		return true;
	}

	@Override
	public boolean playerRemoveGroup(String world, OfflinePlayer player, String groupName) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group == null) {
			return false;
		}
		User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
		user.removeSubGroup(group);
		if (user.getMainGroup() == group) {
			user.setMainGroup(SimpleUserPerms.getGroupsStorage().getDefaultGroup());
		}
		return true;
	}

	@Override
	public String[] getPlayerGroups(String world, OfflinePlayer player) {
		User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
		return SharedUtils.getUserGroupsA(user);
	}

	@Override
	public String getPrimaryGroup(String world, OfflinePlayer player) {
		User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
		return user.getMainGroup().getName();
	}

	@Override
	public boolean playerHas(String world, OfflinePlayer player, String permission) {
		User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
		return SharedUtils.hasPermission(user, permission);
	}

}