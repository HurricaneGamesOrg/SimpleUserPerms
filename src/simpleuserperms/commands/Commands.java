package simpleuserperms.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.DefaultUserPermsCache;
import simpleuserperms.storage.Group;

public class Commands implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("simpleuserperms.admin")) {
			sender.sendMessage(ChatColor.RED + "No perms");
			return true;
		}

		//help
		if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.YELLOW + label + " save "+ChatColor.GRAY + " - " + ChatColor.AQUA + "Saves users and groups to file");
			sender.sendMessage(ChatColor.YELLOW + label + " reload "+ChatColor.GRAY + " - " + ChatColor.AQUA + "Loads users and groups from file");
			sender.sendMessage(ChatColor.YELLOW + label + " setgroup {NAME} {GROUP} "+ ChatColor.GRAY + " - " + ChatColor.AQUA + "Sets user {NAME} main group to {GROUP}");
			sender.sendMessage(ChatColor.YELLOW + label + " addgroup {NAME} {GROUP} "+ChatColor.GRAY + " - " + ChatColor.AQUA + "Adds sub group {GROUP} to user {NAME}");
			sender.sendMessage(ChatColor.YELLOW + label + " remgroup {NAME} {GROUP} "+ChatColor.GRAY + " - " + ChatColor.AQUA + "Removes sub group {GROUP} from user {NAME}");
			sender.sendMessage(ChatColor.YELLOW + label + " addperm {NAME} {PERM} "+ChatColor.GRAY + " - " + ChatColor.AQUA + "Adds permission {PERM} to user {NAME}");
			sender.sendMessage(ChatColor.YELLOW + label + " remperm {NAME} {PERM} "+ChatColor.GRAY + " - " + ChatColor.AQUA + "Removes permission {PERM} from user {NAME}");
			sender.sendMessage(ChatColor.YELLOW + label + " del {NAME} "+ChatColor.GRAY + " - " + ChatColor.AQUA + "Deletes user {NAME}");
			sender.sendMessage(ChatColor.YELLOW + label + " setprefix {NAME} {PREFIX} " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Sets user {NAME} prefix to {PREFIX}");
			sender.sendMessage(ChatColor.YELLOW + label + " remprefix {NAME}" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Removes user {NAME} prefix");
			sender.sendMessage(ChatColor.YELLOW + label + " setsuffix {NAME} {SUFFIX} " + ChatColor.GRAY + " - " + ChatColor.AQUA + "Sets user {NAME} suffix to {SUFFIX}");
			sender.sendMessage(ChatColor.YELLOW + label + " remsuffix {NAME}" + ChatColor.GRAY + " - " + ChatColor.AQUA + "Removes user {NAME} suffix");
			return true;
		}

		//save configs
		if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
			SimpleUserPerms.getGroupsStorage().save();
			SimpleUserPerms.getUsersStorage().save();
			sender.sendMessage(ChatColor.GOLD + "Groups and users saved");
			return true;
		}

		//reload configs
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			SimpleUserPerms.getGroupsStorage().load();
			DefaultUserPermsCache.recalculateDefaultPerms();
			SimpleUserPerms.getUsersStorage().load();
			SimpleUserPerms.getUsersStorage().recalculateAll();
			sender.sendMessage(ChatColor.GOLD + "Grops and users reloaded");
			return true;
		}

		//set main group
		if (args.length == 3 && args[0].equalsIgnoreCase("setgroup")) {
			String playerName = args[1];
			String groupName = args[2];
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group == null) {
				sender.sendMessage(ChatColor.RED + "Group " + groupName + " doesn't exist");
				return true;
			}
			SimpleUserPerms.getUsersStorage().getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).setMainGroup(group);
			sender.sendMessage(ChatColor.GOLD + "Group set");
			return true;
		}

		//add sub group
		if (args.length == 3 && args[0].equalsIgnoreCase("addgroup")) {
			String playerName = args[1];
			String groupName = args[2];
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group == null) {
				sender.sendMessage(ChatColor.RED + "Group " + groupName + " doesn't exist");
				return true;
			}
			SimpleUserPerms.getUsersStorage().getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).addSubGroup(group);
			sender.sendMessage(ChatColor.GOLD + "Sub group added");
			return true;
		}

		//remove sub group
		if (args.length == 3 && args[0].equalsIgnoreCase("remgroup")) {
			String playerName = args[1];
			String groupName = args[2];
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group == null) {
				sender.sendMessage(ChatColor.RED + "Group " + groupName + " doesn't exist");
				return true;
			}
			SimpleUserPerms.getUsersStorage().getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).removeSubGroup(group);
			sender.sendMessage(ChatColor.GOLD + "Sub group removed");
			return true;
		}

		//add permission
		if (args.length == 3 && args[0].equalsIgnoreCase("addperm")) {
			String playerName = args[1];
			String permName = args[2];
			SimpleUserPerms.getUsersStorage().getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).addAdditionalPermission(permName);
			sender.sendMessage(ChatColor.GOLD + "Perm added");
			return true;
		}

		//remove permission
		if (args.length == 3 && args[0].equalsIgnoreCase("remperm")) {
			String playerName = args[1];
			String permName = args[2];
			SimpleUserPerms.getUsersStorage().getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).removeAdditionalPermission(permName);
			sender.sendMessage(ChatColor.GOLD + "Perm removed");
			return true;
		}

		//delete all user info
		if (args.length == 2 && args[0].equalsIgnoreCase("del")) {
			String playerName = args[1];
			SimpleUserPerms.getUsersStorage().deleteUser(Bukkit.getOfflinePlayer(playerName).getUniqueId());
			sender.sendMessage(ChatColor.GOLD + "User deleted");
			return true;
		}

		//set user prefix
		if (args.length >= 3 && args[0].equalsIgnoreCase("setprefix")) {
			String playerName = args[1];
			String prefix = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).replace("\"", "");
			SimpleUserPerms.getUsersStorage().getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).setPrefix(prefix);
			sender.sendMessage(ChatColor.GOLD + "Prefix set");
			return true;
		}

		//remove user prefix
		if (args.length == 2 && args[0].equalsIgnoreCase("remprefix")) {
			String playerName = args[1];
			SimpleUserPerms.getUsersStorage().getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).setPrefix(null);
			sender.sendMessage(ChatColor.GOLD + "Prefix set");
			return true;
		}

		//set user suffix
		if (args.length >= 3 && args[0].equalsIgnoreCase("setsuffix")) {
			String playerName = args[1];
			String suffix = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).replace("\"", "");
			SimpleUserPerms.getUsersStorage().getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).setSuffix(suffix);
			sender.sendMessage(ChatColor.GOLD + "Suffix set");
			return true;
		}

		//remove user suffix
		if (args.length == 2 && args[0].equalsIgnoreCase("remsuffix")) {
			String playerName = args[1];
			SimpleUserPerms.getUsersStorage().getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).setSuffix(null);
			sender.sendMessage(ChatColor.GOLD + "Prefix set");
			return true;
		}

		return false;
	}

}
