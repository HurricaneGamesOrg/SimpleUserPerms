package org.hurricanegames.simpleuserperms.integration;

import org.bukkit.plugin.ServicePriority;
import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.handler.BukkitPermissionsHandler;
import org.hurricanegames.simpleuserperms.integration.chat.DeprecatedChatImpl;
import org.hurricanegames.simpleuserperms.integration.perms.DeprecatedPermImpl;
import org.hurricanegames.simpleuserperms.storage.GroupsStorage;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class VaultIntegration {

	protected final SimpleUserPerms plugin;

	private final Permission pimpl;
	private final Chat cimpl;

	public VaultIntegration(SimpleUserPerms plugin, GroupsStorage groups, UsersStorage users, BukkitPermissionsHandler bperms) {
		this.plugin = plugin;
		this.pimpl = new DeprecatedPermImpl(plugin, groups, users, bperms);
		this.cimpl = new DeprecatedChatImpl(plugin, groups, users, pimpl);
	}

	public void load() {
		plugin.getServer().getServicesManager().register(Permission.class, pimpl, plugin, ServicePriority.High);
		plugin.getServer().getServicesManager().register(Chat.class, cimpl, plugin, ServicePriority.High);
	}

	public void unload() {
		plugin.getServer().getServicesManager().unregister(pimpl);
		plugin.getServer().getServicesManager().unregister(cimpl);
	}

}
