package org.hurricanegames.simpleuserperms.commands.player;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandBasic;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;

public class SimpleUserPermsUserPrefixSetCommand extends SimpleUserPermsCommandBasic {

	public SimpleUserPermsUserPrefixSetCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	protected class CommandArgumentPrefix extends CommandArgumentColorizedString {

		@Override
		protected String getHelpMessage() {
			return "{prefix}";
		}

	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOfflinePlayer.class) BukkitPlayerInfo playerInfo,
		@CommandArgumentDefinition(SimpleUserPermsUserPrefixSetCommand.CommandArgumentPrefix.class) String prefix
	) {
		helper.getUsersStorage().getUser(playerInfo.getUUID()).setPrefix(prefix);

		throw new CommandResponseException(ChatColor.GREEN + "Set user {0} prefix to {1}", playerInfo.getName(), prefix);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "set user prefix";
	}

}
