package org.hurricanegames.simpleuserperms.commands.player;

import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandBasic;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;

public class SimpleUserPermsUserDeleteCommand extends SimpleUserPermsCommandBasic {

	public SimpleUserPermsUserDeleteCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOfflinePlayer.class) BukkitPlayerInfo playerInfo
	) {
		helper.getUsersStorage().deleteUser(playerInfo.getUUID());
		helper.getBukkitPermissions().updatePermissions(playerInfo.getPlayer());

		throw new CommandResponseException("User {0} data deleted", playerInfo.getName());
	}

	@Override
	protected String getHelpExplainMessage() {
		return "deletes all user data";
	}

}
