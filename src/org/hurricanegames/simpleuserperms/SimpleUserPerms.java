package org.hurricanegames.simpleuserperms;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.hurricanegames.commandlib.commands.BukkitCommandExecutor;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsAdminCommands;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;
import org.hurricanegames.simpleuserperms.handler.BukkitPermissionsHandler;
import org.hurricanegames.simpleuserperms.handler.PlayerHandler;
import org.hurricanegames.simpleuserperms.integration.VaultIntegration;
import org.hurricanegames.simpleuserperms.storage.GroupsStorage;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

public class SimpleUserPerms extends JavaPlugin {

	private static SimpleUserPerms instance;

	public static SimpleUserPerms getInstance() {
		return instance;
	}

	public SimpleUserPerms() {
		instance = this;
	}

	private final GroupsStorage groupsStorage = new GroupsStorage(this);

	public GroupsStorage getGroupsStorage() {
		return groupsStorage;
	}

	private final UsersStorage usersStorage = new UsersStorage(this, groupsStorage);

	public UsersStorage getUsersStorage() {
		return usersStorage;
	}

	private final BukkitPermissionsHandler bperms = new BukkitPermissionsHandler(this, usersStorage);

	public BukkitPermissionsHandler getBukkitPermissions() {
		return bperms;
	}

	private VaultIntegration vintergration;

	@Override
	public void onEnable() {
		groupsStorage.load();
		usersStorage.load();
		usersStorage.recalculateAll();
		bperms.updatePermissions();
		getServer().getPluginManager().registerEvents(new PlayerHandler(usersStorage, bperms), this);

		SimpleUserPermsCommandHelper commandHelper = new SimpleUserPermsCommandHelper(groupsStorage, usersStorage, bperms);
		getCommand("simpleuserperms").setExecutor(new BukkitCommandExecutor(new SimpleUserPermsAdminCommands(commandHelper), "simpleuserperms.admin"));

		try {
			vintergration = new VaultIntegration(this, groupsStorage, usersStorage, bperms);
			vintergration.load();
			getLogger().log(Level.INFO, "Enabled Vault integration");
		} catch (Throwable t) {
			getLogger().log(Level.INFO, "Unable to enable Vault integration: " + t.getMessage());
		}
	}

	@Override
	public void onDisable() {
		groupsStorage.save();
		usersStorage.save();

		bperms.removePermissions();

		if (vintergration != null) {
			vintergration.unload();
		}
	}

}
