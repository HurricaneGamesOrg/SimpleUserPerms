package simpleuserperms.integration;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.permission.Permission;
import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.Group;
import simpleuserperms.storage.User;

public class VaultIntegration extends Integration {

	private final Permission impl = new DeprecatedPermImpl();

	@Override
	protected void load() {
		Bukkit.getServicesManager().register(Permission.class, impl, JavaPlugin.getPlugin(Vault.class), ServicePriority.Highest);
	}

	public static abstract class BasicPermImpl extends Permission {

		@Override
		public String getName() {
			return JavaPlugin.getPlugin(SimpleUserPerms.class).getName();
		}

		@Override
		public boolean hasGroupSupport() {
			return true;
		}

		@Override
		public boolean hasSuperPermsCompat() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public String[] getGroups() {
			return SimpleUserPerms.getGroupsStorage().getGroupNames().toArray(new String[0]);
		}
		
	}

	public static abstract class ModernPermImpl extends BasicPermImpl {

		@Override
		public boolean groupAdd(String worldName, String groupName, String permission) {
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group != null) {
				group.addPermission(worldName);
				return true;
			}
			return false;
		}

		@Override
		public boolean groupHas(String worldName, String groupName, String permission) {
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group != null) {
				return group.hasPermission(permission);
			}
			return false;
		}

		@Override
		public boolean groupRemove(String worldName, String groupName, String permission) {
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group != null) {
				group.removePermission(permission);
				return true;
			}
			return false;
		}

		public boolean playerAdd(String world, OfflinePlayer player, String permission) {
			SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId()).addAdditionalPermission(permission);
			return true;
		}

		public boolean playerRemove(String world, OfflinePlayer player, String permission) {
			User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
			if (user.hasAdditionalPermission(permission)) {
				user.removeAdditionalPermission(permission);
			} else {
				user.addAdditionalPermission("-"+permission);
			}
			return true;
		}

		public boolean playerInGroup(String world, OfflinePlayer player, String groupName) {
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group == null) {
				return false;
			}
			User user = SimpleUserPerms.getUsersStorage().getUserIfPresent(player.getUniqueId());
			if (user != null) {
				return user.getMainGroup() == group || user.hasSubGroup(group);
			}
			return false;
		}

		public boolean playerAddGroup(String world, OfflinePlayer player, String groupName) {
			User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group == null) {
				return false;
			}
			if (user.getMainGroup() == SimpleUserPerms.getGroupsStorage().getDefaultGroup()) {
				user.setMainGroup(group);
			} else {
				user.addSubGroup(group);
			}
			return true;
		}

		public boolean playerRemoveGroup(String world, OfflinePlayer player, String groupName) {
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group == null) {
				return false;
			}
			User user = SimpleUserPerms.getUsersStorage().getUserIfPresent(player.getUniqueId());
			if (user != null) {
				user.removeSubGroup(group);
				if (user.getMainGroup() == group) {
					user.setMainGroup(SimpleUserPerms.getGroupsStorage().getDefaultGroup());
				}
				return true;
			}
			return false;
		}

		public String[] getPlayerGroups(String world, OfflinePlayer player) {
			User user = SimpleUserPerms.getUsersStorage().getUserIfPresent(player.getUniqueId());
			if (user != null) {
				return SharedUtils.getUserGroupsA(user);
			}
			return new String[0];
		}

		public String getPrimaryGroup(String world, OfflinePlayer player) {
			User user = SimpleUserPerms.getUsersStorage().getUserIfPresent(player.getUniqueId());
			if (user != null) {
				return user.getMainGroup().getName();
			}
			return SimpleUserPerms.getGroupsStorage().getDefaultGroup().getName();
		}

		@Override
		public boolean playerHas(String world, OfflinePlayer player, String permission) {
			User user = SimpleUserPerms.getUsersStorage().getUserIfPresent(player.getUniqueId());
			if (user != null) {
				SharedUtils.hasPermission(user, permission);
			}
			return false;
		}

	}

	public static class DeprecatedPermImpl extends ModernPermImpl {

		@Deprecated
		@Override
		public String[] getPlayerGroups(String worldName, String playerName) {
			return getPlayerGroups(worldName, Bukkit.getOfflinePlayer(playerName));
		}

		@Deprecated
		@Override
		public String getPrimaryGroup(String worldName, String playerName) {
			return getPrimaryGroup(worldName, Bukkit.getOfflinePlayer(playerName));
		}

		@Deprecated
		@Override
		public boolean playerAdd(String worldName, String playerName, String permission) {
			return playerAdd(worldName, Bukkit.getOfflinePlayer(playerName), permission);
		}

		@Deprecated
		@Override
		public boolean playerAddGroup(String worldName, String playerName, String groupName) {
			return playerAddGroup(worldName, Bukkit.getOfflinePlayer(playerName), groupName);
		}

		@Deprecated
		@Override
		public boolean playerHas(String worldName, String playerName, String permission) {
			return playerHas(worldName, Bukkit.getOfflinePlayer(playerName), permission);
		}

		@Deprecated
		@Override
		public boolean playerInGroup(String worldName, String playerName, String groupName) {
			return playerInGroup(worldName, Bukkit.getOfflinePlayer(playerName), groupName);
		}

		@Deprecated
		@Override
		public boolean playerRemove(String worldName, String playerName, String permission) {
			return playerRemove(worldName, Bukkit.getOfflinePlayer(playerName), permission);
		}

		@Deprecated
		@Override
		public boolean playerRemoveGroup(String worldName, String playerName, String groupName) {
			return playerRemoveGroup(worldName, Bukkit.getOfflinePlayer(playerName), groupName);
		}

	}

}
