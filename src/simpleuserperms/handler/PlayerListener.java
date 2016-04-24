package simpleuserperms.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.User;

public class PlayerListener implements Listener {

	//add permissions to player as early as possible
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLoginLowest(PlayerLoginEvent event) {
		BukkitPermissions bperms = SimpleUserPerms.getBukkitPermissions();
		//remove old attachment in case some shit happened
		bperms.cleanup(event.getPlayer());
		//calculate new permissions
		bperms.updatePermissions(event.getPlayer());
	}

	//cleanup attachments in case event was cancelled
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLoginMonitor(PlayerLoginEvent event) {
		if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
			SimpleUserPerms.getBukkitPermissions().cleanup(event.getPlayer());
		}
	}

	//update last name
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		SimpleUserPerms.getUsersStorage().getUser(event.getPlayer().getUniqueId()).setLastName(event.getPlayer().getName());
	}

	//cleanup attachments on quit
	//remove user with only default group from user list
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		SimpleUserPerms.getBukkitPermissions().cleanup(event.getPlayer());
		SimpleUserPerms.getUsersStorage().deleteUserIf(event.getPlayer().getUniqueId(), User::isDefault);
	}

}
