package simpleuserperms.integration.perms;

import org.bukkit.Bukkit;

public class DeprecatedPermImpl extends ModernPermImpl {

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