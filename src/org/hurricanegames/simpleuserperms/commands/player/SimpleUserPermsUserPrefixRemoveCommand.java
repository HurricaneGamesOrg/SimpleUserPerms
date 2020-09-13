package org.hurricanegames.simpleuserperms.commands.player;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandBasic;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;

public class SimpleUserPermsUserPrefixRemoveCommand extends SimpleUserPermsCommandBasic {

	public SimpleUserPermsUserPrefixRemoveCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOfflinePlayer.class) BukkitPlayerInfo playerInfo
	) {
		helper.getUsersStorage().getUser(playerInfo.getUUID()).setPrefix(null);

		throw new CommandResponseException(ChatColor.GREEN + "Removed user {0} prefix", playerInfo.getName());
	}

	@Override
	protected String getHelpExplainMessage() {
		return "remove user prefix";
	}

}
