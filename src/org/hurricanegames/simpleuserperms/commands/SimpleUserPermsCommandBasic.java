package org.hurricanegames.simpleuserperms.commands;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.simpleuserperms.storage.Group;

public abstract class SimpleUserPermsCommandBasic extends CommandBasic<SimpleUserPermsCommandHelper> {

	public SimpleUserPermsCommandBasic(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	protected class CommandArgumentGroup extends CommandArgumentPositional<Group> {

		@Override
		protected Group parseValue(String arg) {
			return helper.validateNotNull(helper.getGroupsStorage().getGroup(arg), "Group {0} doesnt exist", arg);
		}

		@Override
		protected List<String> complete(String arg) {
			return
				helper.getGroupsStorage().getGroupNames().stream()
				.filter(gName -> gName.startsWith(arg))
				.collect(Collectors.toList());
		}

		@Override
		protected String getHelpMessage() {
			return "{group}";
		}

	}

	protected class CommandArgumentPermission extends CommandArgumentPositional<String> {

		@Override
		protected String parseValue(String arg) {
			return arg;
		}

		@Override
		protected List<String> complete(String arg) {
			return Collections.emptyList();
		}

		@Override
		protected String getHelpMessage() {
			return "{permission}";
		}

	}

}
