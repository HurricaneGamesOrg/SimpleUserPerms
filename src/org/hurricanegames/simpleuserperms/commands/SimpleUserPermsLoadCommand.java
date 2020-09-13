package org.hurricanegames.simpleuserperms.commands;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandResponseException;

public class SimpleUserPermsLoadCommand extends SimpleUserPermsCommandBasic {

	public SimpleUserPermsLoadCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand() {
		helper.getGroupsStorage().load();
		helper.getUsersStorage().load();
		helper.getBukkitPermissions().updatePermissions();

		throw new CommandResponseException(ChatColor.GOLD + "Groups and users loaded");
	}

	@Override
	protected String getHelpExplainMessage() {
		return "loads groups and users from file";
	}

}
