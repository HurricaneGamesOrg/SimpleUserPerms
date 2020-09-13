package org.hurricanegames.simpleuserperms.commands.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.hurricanegames.commandlib.commands.CommandBasic;
import org.hurricanegames.commandlib.commands.CommandResponseException;
import org.hurricanegames.commandlib.providers.playerinfo.BukkitPlayerInfo;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandBasic;
import org.hurricanegames.simpleuserperms.commands.SimpleUserPermsCommandHelper;
import org.hurricanegames.simpleuserperms.storage.Group;
import org.hurricanegames.simpleuserperms.storage.User;

public class SimpleUserPermsUserInfoCommand extends SimpleUserPermsCommandBasic {

	public SimpleUserPermsUserInfoCommand(SimpleUserPermsCommandHelper helper) {
		super(helper);
	}

	@CommandHandler
	protected void handleCommand(
		@CommandArgumentDefinition(CommandBasic.CommandArgumentOfflinePlayer.class) BukkitPlayerInfo playerInfo
	) {
		User user = helper.getUsersStorage().getUser(playerInfo.getUUID());

		if (user.isDefault()) {
			throw new CommandResponseException(ChatColor.GREEN + "Player {0} is default", playerInfo.getName());
		} else {
			List<String> response = new ArrayList<>();
			response.add(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH.toString() + "                                                  ");
			response.add(ChatColor.GREEN.toString() + "UUID: " + playerInfo.getUUID().toString());
			response.add(ChatColor.GREEN.toString() + "Name: " + playerInfo.getName().toString());
			response.add(ChatColor.GREEN.toString() + "Main group: " + Group.getNameOrNull(user.getMainGroup()));
			response.add(ChatColor.GREEN.toString() + "Sub groups: " + user.getSubGroups().stream().map(Group::getName).collect(Collectors.toList()));
			response.add(ChatColor.GREEN.toString() + "Additional permissions:");
			for (String additionalPermission : user.getAdditionalPermissions()) {
				response.add(ChatColor.GREEN.toString() + "  " + additionalPermission);
			}
			response.add(ChatColor.GREEN.toString() + "Prefix: " + user.getPrefix());
			response.add(ChatColor.GREEN.toString() + "Suffix: " + user.getSuffix());
			response.add(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH.toString() + "                                                  ");
			throw new CommandResponseException(String.join("\n", response));
		}
	}

	@Override
	protected String getHelpExplainMessage() {
		return "lists user info";
	}

}
