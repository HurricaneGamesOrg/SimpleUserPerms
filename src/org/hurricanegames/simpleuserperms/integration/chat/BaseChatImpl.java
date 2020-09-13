package org.hurricanegames.simpleuserperms.integration.chat;

import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.storage.GroupsStorage;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public abstract class BaseChatImpl extends Chat {

	protected final SimpleUserPerms plugin;
	protected final GroupsStorage groups;
	protected final UsersStorage users;

	protected BaseChatImpl(SimpleUserPerms plugin, GroupsStorage groups, UsersStorage users, Permission perms) {
		super(perms);
		this.plugin = plugin;
		this.groups = groups;
		this.users = users;
	}

	@Override
	public String getName() {
		return plugin.getName();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}