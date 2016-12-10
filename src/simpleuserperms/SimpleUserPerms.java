package simpleuserperms;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import simpleuserperms.commands.Commands;
import simpleuserperms.handler.BukkitPermissions;
import simpleuserperms.handler.PlayerListener;
import simpleuserperms.integration.VaultIntegration;
import simpleuserperms.storage.DefaultUserPermsCache;
import simpleuserperms.storage.GroupsStorage;
import simpleuserperms.storage.UsersStorage;

public class SimpleUserPerms extends JavaPlugin {

	private static BukkitPermissions bPerms;

	public static BukkitPermissions getBukkitPermissions() {
		return bPerms;
	}

	private static UsersStorage usersStorage;

	public static UsersStorage getUsersStorage() {
		return usersStorage;
	}

	private static GroupsStorage groupsStorage;

	public static GroupsStorage getGroupsStorage() {
		return groupsStorage;
	}

	private VaultIntegration vintergration;

	@Override
	public void onEnable() {
		groupsStorage = new GroupsStorage(this);
		usersStorage = new UsersStorage(this);
		bPerms = new BukkitPermissions(this);
		groupsStorage.load();
		DefaultUserPermsCache.recalculateDefaultPerms();
		usersStorage.load();
		usersStorage.recalculateAll();
		if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
			vintergration = new VaultIntegration();
			vintergration.load();
		}
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getCommand("simpleuserperms").setExecutor(new Commands());
	}

	@Override
	public void onDisable() {
		if (vintergration != null) {
			vintergration.unload();
		}
		groupsStorage.save();
		usersStorage.save();
	}

}
