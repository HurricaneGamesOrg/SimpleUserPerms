package org.hurricanegames.simpleuserperms.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hurricanegames.simpleuserperms.storage.User;
import org.hurricanegames.simpleuserperms.storage.UsersStorage;

public class PlayerHandler implements Listener {

	protected final UsersStorage users;
	protected final BukkitPermissionsHandler permissions;

	public PlayerHandler(UsersStorage users, BukkitPermissionsHandler permissions) {
		this.users = users;
		this.permissions = permissions;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	protected void onPlayerLoginLowest(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		users.getUser(player.getUniqueId()).setLastName(player.getName());
		permissions.updatePermissions(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	protected void onPlayerLoginMonitor(PlayerLoginEvent event) {
		if (event.getResult() == Result.ALLOWED) {
			return;
		}
		permissions.cleanupPermissions(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	protected void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		users.deleteUserIf(player.getUniqueId(), User::isDefault);
		permissions.cleanupPermissions(player);
	}

}
