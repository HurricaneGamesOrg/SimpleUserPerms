package org.hurricanegames.simpleuserperms.integration.perms;

import org.bukkit.OfflinePlayer;
import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.handler.BukkitPermissionsHandler;
import org.hurricanegames.simpleuserperms.integration.SharedUtils;
import org.hurricanegames.simpleuserperms.storage.Group;
import org.hurricanegames.simpleuserperms.storage.GroupsStorage;
import org.hurricanegames.simpleuserperms.storage.User;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

public abstract class ModernPermImpl extends BasicPermImpl {

	public ModernPermImpl(SimpleUserPerms plugin, GroupsStorage groups, UsersStorage users, BukkitPermissionsHandler bperms) {
		super(plugin, groups, users, bperms);
	}

	@Override
	public boolean groupHas(String worldName, String groupName, String permission) {
		Group group = groups.getGroup(groupName);
		if (group != null) {
			return group.hasPermission(permission);
		}
		return false;
	}

	@Override
	public boolean playerInGroup(String world, OfflinePlayer player, String groupName) {
		Group group = groups.getGroup(groupName);
		if (group == null) {
			return false;
		}
		return users.getUser(player.getUniqueId()).hasGroup(group);
	}

	@Override
	public String[] getPlayerGroups(String world, OfflinePlayer player) {
		User user = users.getUser(player.getUniqueId());
		return SharedUtils.getUserGroupsA(user);
	}

	@Override
	public String getPrimaryGroup(String world, OfflinePlayer player) {
		User user = users.getUser(player.getUniqueId());
		return user.getMainGroup().getName();
	}

	@Override
	public boolean playerHas(String world, OfflinePlayer player, String permission) {
		User user = users.getUser(player.getUniqueId());
		return SharedUtils.hasPermission(user, permission);
	}

	@Override
	public boolean groupAdd(String worldName, String groupName, String permission) {
		Group group = groups.getGroup(groupName);
		if (group != null) {
			group.addPermission(worldName);
			users.recalculateAll();
			bperms.updatePermissions();
			return true;
		}
		return false;
	}

	@Override
	public boolean groupRemove(String worldName, String groupName, String permission) {
		Group group = groups.getGroup(groupName);
		if (group != null) {
			group.removePermission(permission);
			users.recalculateAll();
			bperms.updatePermissions();
			return true;
		}
		return false;
	}

	@Override
	public boolean playerAdd(String world, OfflinePlayer player, String permission) {
		users.getUser(player.getUniqueId()).addAdditionalPermission(permission);
		bperms.updatePermissions(player);
		return true;
	}

	@Override
	public boolean playerRemove(String world, OfflinePlayer player, String permission) {
		User user = users.getUser(player.getUniqueId());
		if (user.hasAdditionalPermission(permission)) {
			user.removeAdditionalPermission(permission);
		} else {
			user.addAdditionalPermission("-"+permission);
		}
		bperms.updatePermissions(player);
		return true;
	}

	@Override
	public boolean playerAddGroup(String world, OfflinePlayer player, String groupName) {
		Group group = groups.getGroup(groupName);
		if (group == null) {
			return false;
		}
		User user = users.getUser(player.getUniqueId());
		if (user.getMainGroup() == groups.getDefaultGroup()) {
			user.setMainGroup(group);
		} else {
			user.addSubGroup(group);
		}
		bperms.updatePermissions(player);
		return true;
	}

	@Override
	public boolean playerRemoveGroup(String world, OfflinePlayer player, String groupName) {
		Group group = groups.getGroup(groupName);
		if (group == null) {
			return false;
		}
		User user = users.getUser(player.getUniqueId());
		user.removeSubGroup(group);
		if (user.getMainGroup() == group) {
			user.setMainGroup(groups.getDefaultGroup());
		}
		bperms.updatePermissions(player);
		return true;
	}

}