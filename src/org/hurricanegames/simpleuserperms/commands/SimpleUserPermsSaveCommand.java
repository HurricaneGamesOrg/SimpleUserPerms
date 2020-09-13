package org.hurricanegames.simpleuserperms.commands;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandResponseException;

public class SimpleUserPermsSaveCommand extends SimpleUserPermsCommandBasic {

	public SimpleUserPermsSaveCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand() {
		helper.getGroupsStorage().save();
		helper.getUsersStorage().save();

		throw new CommandResponseException(ChatColor.GOLD + "Groups and users saved");
	}

	@Override
	protected String getHelpExplainMessage() {
		return "saves groups and users to file";
	}

}
