package org.hurricanegames.simpleuserperms.integration.perms;

import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.handler.BukkitPermissionsHandler;
import org.hurricanegames.simpleuserperms.storage.GroupsStorage;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

import net.milkbowl.vault.permission.Permission;

public abstract class BasicPermImpl extends Permission {

	protected final SimpleUserPerms plugin;
	protected final GroupsStorage groups;
	protected final UsersStorage users;
	protected final BukkitPermissionsHandler bperms;

	protected BasicPermImpl(SimpleUserPerms plugin, GroupsStorage groups, UsersStorage users, BukkitPermissionsHandler bperms) {
		this.plugin = plugin;
		this.groups = groups;
		this.users = users;
		this.bperms = bperms;
	}

	@Override
	public String getName() {
		return plugin.getName();
	}

	@Override
	public boolean hasGroupSupport() {
		return true;
	}

	@Override
	public boolean hasSuperPermsCompat() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String[] getGroups() {
		return groups.getGroupNames().toArray(new String[0]);
	}

}