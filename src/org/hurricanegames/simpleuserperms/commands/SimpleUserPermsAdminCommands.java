package org.hurricanegames.simpleuserperms.commands;

import org.hurricanegames.commandlib.commands.CommandRouter;
import org.hurricanegames.simpleuserperms.commands.player.SimpleUserPermsUserCommands;

public class SimpleUserPermsAdminCommands extends CommandRouter<SimpleUserPermsCommandHelper> {

	public SimpleUserPermsAdminCommands(SimpleUserPermsCommandHelper helper) {
		super(helper);
		addCommand("save", new SimpleUserPermsSaveCommand(helper));
		addCommand("load", new SimpleUserPermsLoadCommand(helper));
		addCommand("user", new SimpleUserPermsUserCommands(helper));
	}

}
