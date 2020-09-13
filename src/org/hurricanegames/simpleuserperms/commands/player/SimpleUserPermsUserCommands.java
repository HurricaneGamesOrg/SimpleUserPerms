package org.hurricanegames.simpleuserperms.commands.player;

import org.hurricanegames.commandlib.commands.CommandRouter;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;

public class SimpleUserPermsUserCommands extends CommandRouter<SimpleUserPermsCommandHelper> {

	public SimpleUserPermsUserCommands(SimpleUserPermsCommandHelper helper) {
		super(helper);
		addCommand("info", new SimpleUserPermsUserInfoCommand(helper));
		addCommand("setgroup", new SimpleUserPermsUserMainGroupSetCommand(helper));
		addCommand("addgroup", new SimpleUserPermsUserSubGroupAddCommand(helper));
		addCommand("remgroup", new SimpleUserPermsUserSubGroupRemoveCommand(helper));
		addCommand("addperm", new SimpleUserPermsUserPermissionAddCommand(helper));
		addCommand("remperm", new SimpleUserPermsUserPermissionRemoveCommand(helper));
		addCommand("setprefix", new SimpleUserPermsUserPrefixSetCommand(helper));
		addCommand("remprefix", new SimpleUserPermsUserPrefixRemoveCommand(helper));
		addCommand("setsuffix", new SimpleUserPermsUserSuffixSetCommand(helper));
		addCommand("remsuffix", new SimpleUserPermsUserSuffixRemoveCommand(helper));
		addCommand("delete", new SimpleUserPermsUserDeleteCommand(helper));
	}

}
