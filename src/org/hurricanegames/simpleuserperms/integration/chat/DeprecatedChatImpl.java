package org.hurricanegames.simpleuserperms.integration.chat;

import org.bukkit.Bukkit;
import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.storage.GroupsStorage;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

import net.milkbowl.vault.permission.Permission;

public class DeprecatedChatImpl extends ModernChatImpl {

	public DeprecatedChatImpl(SimpleUserPerms plugin, GroupsStorage groups, UsersStorage users, Permission perms) {
		super(plugin, groups, users, perms);
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getPlayerPrefix(String worldName, String playerName) {
		return getPlayerPrefix(worldName, Bukkit.getOfflinePlayer(playerName));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setPlayerPrefix(String worldName, String playerName, String prefix) {
		setPlayerPrefix(worldName, Bukkit.getOfflinePlayer(playerName), prefix);
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getPlayerSuffix(String worldName, String playerName) {
		return getPlayerSuffix(worldName, Bukkit.getOfflinePlayer(playerName));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setPlayerSuffix(String worldName, String playerName, String suffix) {
		setPlayerSuffix(worldName, Bukkit.getOfflinePlayer(playerName), suffix);
	}

}