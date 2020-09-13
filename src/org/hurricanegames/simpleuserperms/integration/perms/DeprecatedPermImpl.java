package org.hurricanegames.simpleuserperms.integration.perms;

import org.bukkit.Bukkit;
import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.handler.BukkitPermissionsHandler;
import org.hurricanegames.simpleuserperms.storage.GroupsStorage;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

public class DeprecatedPermImpl extends ModernPermImpl {

	public DeprecatedPermImpl(SimpleUserPerms plugin, GroupsStorage groups, UsersStorage users, BukkitPermissionsHandler bperms) {
		super(plugin, groups, users, bperms);
	}

	@Deprecated
	@Override
	public String[] getPlayerGroups(String worldName, String playerName) {
		return getPlayerGroups(worldName, Bukkit.getOfflinePlayer(playerName));
	}

	@Deprecated
	@Override
	public String getPrimaryGroup(String worldName, String playerName) {
		return getPrimaryGroup(worldName, Bukkit.getOfflinePlayer(playerName));
	}

	@Deprecated
	@Override
	public boolean playerAdd(String worldName, String playerName, String permission) {
		return playerAdd(worldName, Bukkit.getOfflinePlayer(playerName), permission);
	}

	@Deprecated
	@Override
	public boolean playerAddGroup(String worldName, String playerName, String groupName) {
		return playerAddGroup(worldName, Bukkit.getOfflinePlayer(playerName), groupName);
	}

	@Deprecated
	@Override
	public boolean playerHas(String worldName, String playerName, String permission) {
		return playerHas(worldName, Bukkit.getOfflinePlayer(playerName), permission);
	}

	@Deprecated
	@Override
	public boolean playerInGroup(String worldName, String playerName, String groupName) {
		return playerInGroup(worldName, Bukkit.getOfflinePlayer(playerName), groupName);
	}

	@Deprecated
	@Override
	public boolean playerRemove(String worldName, String playerName, String permission) {
		return playerRemove(worldName, Bukkit.getOfflinePlayer(playerName), permission);
	}

	@Deprecated
	@Override
	public boolean playerRemoveGroup(String worldName, String playerName, String groupName) {
		return playerRemoveGroup(worldName, Bukkit.getOfflinePlayer(playerName), groupName);
	}

}