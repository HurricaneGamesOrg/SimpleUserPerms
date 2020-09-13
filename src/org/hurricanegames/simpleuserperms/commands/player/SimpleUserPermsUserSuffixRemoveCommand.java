package org.hurricanegames.simpleuserperms.commands.player;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandBasic;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;

public class SimpleUserPermsUserSuffixRemoveCommand extends SimpleUserPermsCommandBasic {

	public SimpleUserPermsUserSuffixRemoveCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOfflinePlayer.class) BukkitPlayerInfo playerInfo
	) {
		helper.getUsersStorage().getUser(playerInfo.getUUID()).setSuffix(null);

		throw new CommandResponseException(ChatColor.GREEN + "Removed user {0} suffix", playerInfo.getName());
	}

	@Override
	protected String getHelpExplainMessage() {
		return "remove user suffix";
	}

}
