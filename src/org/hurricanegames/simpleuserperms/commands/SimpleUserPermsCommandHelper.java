package org.hurricanegames.simpleuserperms.commands;

import org.hurricanegames.commandlib.commands.CommandHelper;
import org.hurricanegames.commandlib.commands.CommandMessages;
import org.hurricanegames.commandlib.providers.messages.DefaultMessages;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfoProvider;
import org.hurricanegames.simpleuserperms.handler.BukkitPermissionsHandler;
import org.hurricanegames.simpleuserperms.storage.GroupsStorage;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

public class SimpleUserPermsCommandHelper extends CommandHelper<CommandMessages, BukkitPlayerInfo, BukkitPlayerInfoProvider> {

	protected final GroupsStorage groups;
	protected final UsersStorage users;
	protected final BukkitPermissionsHandler bperms;

	public SimpleUserPermsCommandHelper(GroupsStorage groups, UsersStorage users, BukkitPermissionsHandler bperms) {
		super(DefaultMessages.IMMUTABLE, BukkitPlayerInfoProvider.INSTANCE);
		this.groups = groups;
		this.users = users;
		this.bperms = bperms;
	}

	public GroupsStorage getGroupsStorage() {
		return groups;
	}

	public UsersStorage getUsersStorage() {
		return users;
	}

	public BukkitPermissionsHandler getBukkitPermissions() {
		return bperms;
	}

}
