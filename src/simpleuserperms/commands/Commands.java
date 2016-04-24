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
import simpleuserperms.storage.GroupsStorage;
import simpleuserperms.storage.UsersStorage;

public class Commands implements CommandExecutor {

	private final UsersStorage users = SimpleUserPerms.getUsersStorage();
	private final GroupsStorage groups = SimpleUserPerms.getGroupsStorage();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("simpleuserperms.admin")) {
			sender.sendMessage(ChatColor.RED + "No perms");
			return true;
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
			groups.save();
			users.save();
			sender.sendMessage(ChatColor.GOLD + "Groups and users saved");
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			groups.load();
			DefaultUserPermsCache.recalculateDefaultPerms();
			users.load();
			users.recalculateAll();
			sender.sendMessage(ChatColor.GOLD + "Grops and users reloaded");
			return true;
		} if (args.length == 3 && args[0].equalsIgnoreCase("setgroup")) {
			String playerName = args[1];
			String groupName = args[2];
			Group group = groups.getGroup(groupName);
			if (group == null) {
				sender.sendMessage(ChatColor.RED + "Group " + groupName + " doesn't exist");
				return true;
			}
			users.getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).setMainGroup(group);
			sender.sendMessage(ChatColor.GOLD + "Group set");
			return true;
		} else if (args.length == 2 && args[0].equalsIgnoreCase("deluser")) {
			String playerName = args[1];
			users.deleteUser(Bukkit.getOfflinePlayer(playerName).getUniqueId());
			sender.sendMessage(ChatColor.GOLD + "User deleted");
			return true;
		} else if (args.length >= 3 && args[0].equalsIgnoreCase("setprefix")) {
			String playerName = args[1];
			String prefix = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).replace("\"", "");
			users.getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).setPrefix(prefix);
			sender.sendMessage(ChatColor.GOLD + "Prefix set");
			return true;
		} else if (args.length >= 3 && args[0].equalsIgnoreCase("setsuffix")) {
			String playerName = args[1];
			String suffix = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).replace("\"", "");
			users.getUser(Bukkit.getOfflinePlayer(playerName).getUniqueId()).setSuffix(suffix);
			sender.sendMessage(ChatColor.GOLD + "Suffix set");
			return true;
		}
		return false;
	}

}
