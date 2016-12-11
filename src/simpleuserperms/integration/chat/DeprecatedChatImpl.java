package simpleuserperms.integration.chat;

import org.bukkit.Bukkit;

import net.milkbowl.vault.permission.Permission;

public class DeprecatedChatImpl extends ModernChatImpl {

	public DeprecatedChatImpl(Permission perms) {
		super(perms);
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