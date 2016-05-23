package simpleuserperms.integration;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.sk89q.wepif.PermissionsResolver;
import com.sk89q.wepif.PermissionsResolverManager;

import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.Group;
import simpleuserperms.storage.User;

public class WEPIFIntegration extends Integration {

	private final WEPIFImpl impl = new WEPIFImpl();

	@Override
	protected void load() {
		PermissionsResolverManager manager = PermissionsResolverManager.getInstance();
		try {
			Field field = PermissionsResolverManager.class.getDeclaredField("permissionResolver");
			field.setAccessible(true);
			field.set(manager, impl);
		} catch (Throwable t) {
			System.err.println("Unable to load WorldEdit WEPIF integration");
			t.printStackTrace();
		}
	}

	public static class WEPIFImpl implements PermissionsResolver {

		@SuppressWarnings("deprecation")
		@Override
		public String[] getGroups(String playerName) {
			return getGroups(Bukkit.getOfflinePlayer(playerName));
		}

		@Override
		public String[] getGroups(OfflinePlayer player) {
			User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
			return SharedUtils.getUserGroupsA(user);
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean hasPermission(String player, String permission) {
			return hasPermission(Bukkit.getOfflinePlayer(player), permission);
		}

		@Override
		public boolean hasPermission(String worldName, String playerName, String permission) {
			return hasPermission(worldName, playerName);
		}

		@Override
		public boolean hasPermission(String worldName, OfflinePlayer player, String permission) {
			return hasPermission(player, permission);
		}

		@Override
		public boolean hasPermission(OfflinePlayer player, String permission) {
			User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
			return SharedUtils.hasPermission(user, permission);
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean inGroup(String playerName, String group) {
			return inGroup(Bukkit.getOfflinePlayer(playerName), group);
		}

		@Override
		public boolean inGroup(OfflinePlayer player, String groupName) {
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group == null) {
				return false;
			}
			User user = SimpleUserPerms.getUsersStorage().getUser(player.getUniqueId());
			return user.getMainGroup() == group || user.hasSubGroup(group);
		}

		@Override
		public String getDetectionMessage() {
			return "";
		}

		@Override
		public void load() {
		}

	}

}
