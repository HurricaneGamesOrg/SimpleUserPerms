package org.hurricanegames.simpleuserperms.commands.player;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandBasic;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;

public class SimpleUserPermsUserPermissionRemoveCommand extends SimpleUserPermsCommandBasic {

	public SimpleUserPermsUserPermissionRemoveCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOfflinePlayer.class) BukkitPlayerInfo playerInfo,
		@CommandArgumentDefinition(SimpleUserPermsCommandBasic.CommandArgumentPermission.class) String permission
	) {
		helper.getUsersStorage().getUser(playerInfo.getUUID()).removeAdditionalPermission(permission);
		helper.getBukkitPermissions().updatePermissions(playerInfo.getPlayer());

		throw new CommandResponseException(ChatColor.GREEN + "Removed user {0} permission {1}", playerInfo.getName(), permission);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "remove user permission";
	}

}
