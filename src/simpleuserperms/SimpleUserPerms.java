package simpleuserperms;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import simpleuserperms.commands.Commands;
import simpleuserperms.handler.BukkitPermissions;
import simpleuserperms.handler.PlayerListener;
import simpleuserperms.integration.Integration.IntegrationListener;
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

	@Override
	public void onEnable() {
		groupsStorage = new GroupsStorage(this);
		usersStorage = new UsersStorage(this);
		bPerms = new BukkitPermissions(this);
		groupsStorage.load();
		DefaultUserPermsCache.recalculateDefaultPerms();
		usersStorage.load();
		Bukkit.getOnlinePlayers().forEach(player -> usersStorage.getUser(player.getUniqueId()).setPlayerRef(player));
		usersStorage.recalculateAll();
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new IntegrationListener(), this);
		getCommand("simpleuserperms").setExecutor(new Commands());
	}

	@Override
	public void onDisable() {
		groupsStorage.save();
		usersStorage.save();
	}

}
