package org.hurricanegames.simpleuserperms.commands.player;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandBasic;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;

public class SimpleUserPermsUserSuffixSetCommand extends SimpleUserPermsCommandBasic {

	public SimpleUserPermsUserSuffixSetCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	protected class CommandArgumentSuffix extends CommandArgumentColorizedString {

		@Override
		protected String getHelpMessage() {
			return "{suffix}";
		}

	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOfflinePlayer.class) BukkitPlayerInfo playerInfo,
		@CommandArgumentDefinition(SimpleUserPermsUserSuffixSetCommand.CommandArgumentSuffix.class) String prefix
	) {
		helper.getUsersStorage().getUser(playerInfo.getUUID()).setSuffix(prefix);

		throw new CommandResponseException(ChatColor.GREEN + "Set user {0} suffix to {1}", playerInfo.getName(), prefix);
	}

	@Override
	protected String getHelpExplainMessage() {
		return "set user suffix";
	}

}
