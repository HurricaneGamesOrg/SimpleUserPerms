package org.hurricanegames.simpleuserperms.integration.chat;

import org.bukkit.OfflinePlayer;
import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.storage.Group;
import org.hurricanegames.simpleuserperms.storage.GroupsStorage;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

import net.milkbowl.vault.permission.Permission;

public abstract class ModernChatImpl extends UnsupportedNodesChatImpl {

	public ModernChatImpl(SimpleUserPerms plugin, GroupsStorage groups, UsersStorage users, Permission perms) {
		super(plugin, groups, users, perms);
	}

	@Override
	public String getPlayerPrefix(String worldName, OfflinePlayer player) {
		return users.getUser(player.getUniqueId()).getEffectivePrefix();
	}

	@Override
	public String getPlayerSuffix(String worldName, OfflinePlayer player) {
		return users.getUser(player.getUniqueId()).getEffectiveSuffix();
	}

	@Override
	public String getGroupPrefix(String world, String groupName) {
		Group group = groups.getGroup(groupName);
		return group != null ? group.getPrefix() : null;
	}

	@Override
	public String getGroupSuffix(String world, String groupName) {
		Group group = groups.getGroup(groupName);
		return group != null ? group.getSuffix() : null;
	}

	@Override
	public void setPlayerPrefix(String worldName, OfflinePlayer player, String prefix) {
		users.getUser(player.getUniqueId()).setPrefix(prefix);
	}

	@Override
	public void setPlayerSuffix(String worldName, OfflinePlayer player, String suffix) {
		users.getUser(player.getUniqueId()).setSuffix(suffix);
	}

	@Override
	public void setGroupPrefix(String world, String groupName, String prefix) {
		Group group = groups.getGroup(groupName);
		if (group != null) {
			group.setPrefix(prefix);
		}
	}

	@Override
	public void setGroupSuffix(String world, String groupName, String suffix) {
		Group group = groups.getGroup(groupName);
		if (group != null) {
			group.setPrefix(suffix);
		}
	}

}