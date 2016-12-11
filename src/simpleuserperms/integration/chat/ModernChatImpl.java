package simpleuserperms.integration.chat;

import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.permission.Permission;
import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.Group;

public abstract class ModernChatImpl extends UnsupportedNodesChatImpl {

	public ModernChatImpl(Permission perms) {
		super(perms);
	}

	@Override
	public String getPlayerPrefix(String worldName, OfflinePlayer player) {
		return SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId()).getPrefix();
	}

	@Override
	public void setPlayerPrefix(String worldName, OfflinePlayer player, String prefix) {
		SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId()).setPrefix(prefix);
	}

	@Override
	public String getPlayerSuffix(String worldName, OfflinePlayer player) {
		return SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId()).getSuffix();
	}

	@Override
	public void setPlayerSuffix(String worldName, OfflinePlayer player, String suffix) {
		SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId()).setSuffix(suffix);
	}

	@Override
	public String getGroupPrefix(String world, String groupName) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		return group != null ? group.getPrefix() : null;
	}

	@Override
	public void setGroupPrefix(String world, String groupName, String prefix) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group != null) {
			group.setPrefix(prefix);
		}
	}

	@Override
	public String getGroupSuffix(String world, String groupName) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		return group != null ? group.getSuffix() : null;
	}

	@Override
	public void setGroupSuffix(String world, String groupName, String suffix) {
		Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
		if (group != null) {
			group.setPrefix(suffix);
		}
	}

}