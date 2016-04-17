package simpleuserperms.integration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.perm.PermissionsHandler;

import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.Group;
import simpleuserperms.storage.GroupsStorage;
import simpleuserperms.storage.User;
import simpleuserperms.storage.UsersStorage;

public class EssentialsIntegration extends Integration {

	private final PermHandlerImpl impl = new PermHandlerImpl(JavaPlugin.getPlugin(Essentials.class));

	@Override
	protected void load() {
		Essentials essentials = JavaPlugin.getPlugin(Essentials.class);
		try {
			Field field = Essentials.class.getDeclaredField("permissionsHandler");
			field.setAccessible(true);
			field.set(essentials, impl);
		} catch (Throwable t) {
			System.err.println("Unable to load Essentials integration");
			t.printStackTrace();
		}
	}

	public static class PermHandlerImpl extends PermissionsHandler {

		public PermHandlerImpl(Essentials plugin) {
			super(plugin);
		}

		private final UsersStorage users = SimpleUserPerms.getUsersStorage();
		private final GroupsStorage groups = SimpleUserPerms.getGroupsStorage();

		@Override
		public String getGroup(Player base) {
			User user = users.getUserIfPresent(base.getUniqueId());
			if (user != null) {
				return user.getMainGroup().getName();
			}
			return groups.getDefaultGroup().getName();
		}

		@Override
		public List<String> getGroups(Player base) {
			User user = users.getUserIfPresent(base.getUniqueId());
			if (user != null) {
				return SharedUtils.getUserGroupsL(user);
			}
			return new ArrayList<String>();
		}

		@Override
		public boolean canBuild(Player base, String group) {
			return true;
		}

		@Override
		public boolean inGroup(Player base, String groupName) {
			Group group = groups.getGroup(groupName);
			if (group == null) {
				return false;
			}
			User user = users.getUserIfPresent(base.getUniqueId());
			if (user != null) {
				return user.getMainGroup() == group || user.hasSubGroup(group);
			}
			return false;
		}

		@Override
		public boolean hasPermission(Player base, String node) {
			User user = users.getUserIfPresent(base.getUniqueId());
			if (user != null) {
				return SharedUtils.hasPermission(user, node);
			}
			return false;
		}

		@Override
		public String getPrefix(Player base) {
			return users.getUser(base.getUniqueId()).getPrefix();
		}

		@Override
		public String getSuffix(Player base) {
			return "";
		}

		@Override
		public void checkPermissions() {
		}

	}

}
