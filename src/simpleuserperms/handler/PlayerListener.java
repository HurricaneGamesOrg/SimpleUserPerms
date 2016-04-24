package simpleuserperms.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import simpleuserperms.SimpleUserPerms;

public class PlayerListener implements Listener {

	//add permissions to player as early as possible
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLoginLowest(PlayerLoginEvent event) {
		//remove old attachment in case some shit happened
		SimpleUserPerms.getBukkitPermissions().cleanup(event.getPlayer());
		//calculate new permissions
		SimpleUserPerms.getBukkitPermissions().updatePermissions(event.getPlayer());
	}

	//cleanup attachments in case event was cancelled
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLoginMonitor(PlayerLoginEvent event) {
		if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
			SimpleUserPerms.getBukkitPermissions().cleanup(event.getPlayer());
		}
	}

	//cleanup attachments on quit
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		SimpleUserPerms.getBukkitPermissions().cleanup(event.getPlayer());
	}

}
