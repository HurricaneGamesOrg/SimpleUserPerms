package org.hurricanegames.simpleuserperms.commands.player;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandBasic;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;
import org.hurricanegames.simpleuserperms.storage.Group;

public class SimpleUserPermsUserSubGroupAddCommand extends SimpleUserPermsCommandBasic{

	public SimpleUserPermsUserSubGroupAddCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOfflinePlayer.class) BukkitPlayerInfo playerInfo,
		@CommandArgumentDefinition(SimpleUserPermsCommandBasic.CommandArgumentGroup.class) Group group
	) {
		helper.getUsersStorage().getUser(playerInfo.getUUID()).addSubGroup(group);
		helper.getBukkitPermissions().updatePermissions(playerInfo.getPlayer());

		throw new CommandResponseException(ChatColor.GREEN + "Added user {0} sub group {1}", playerInfo.getName(), group.getName());
	}

	@Override
	protected String getHelpExplainMessage() {
		return "add user subgroup";
	}

}
