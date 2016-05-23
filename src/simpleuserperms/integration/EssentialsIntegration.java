package simpleuserperms.integration;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.perm.PermissionsHandler;

import simpleuserperms.SimpleUserPerms;
import simpleuserperms.storage.Group;
import simpleuserperms.storage.User;

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

		@Override
		public String getGroup(Player base) {
			return SimpleUserPerms.getUsersStorage().getUser(base.getUniqueId()).getMainGroup().getName();
		}

		@Override
		public List<String> getGroups(Player base) {
			User user = SimpleUserPerms.getUsersStorage().getUser(base.getUniqueId());
			return SharedUtils.getUserGroupsL(user);
		}

		@Override
		public boolean canBuild(Player base, String group) {
			return true;
		}

		@Override
		public boolean inGroup(Player base, String groupName) {
			Group group = SimpleUserPerms.getGroupsStorage().getGroup(groupName);
			if (group == null) {
				return false;
			}
			User user = SimpleUserPerms.getUsersStorage().getUser(base.getUniqueId());
			return user.getMainGroup() == group || user.hasSubGroup(group);
		}

		@Override
		public boolean hasPermission(Player base, String node) {
			User user = SimpleUserPerms.getUsersStorage().getUser(base.getUniqueId());
			return SharedUtils.hasPermission(user, node);
		}

		@Override
		public String getPrefix(Player base) {
			return SimpleUserPerms.getUsersStorage().getUser(base.getUniqueId()).getPrefix();
		}

		@Override
		public String getSuffix(Player base) {
			return SimpleUserPerms.getUsersStorage().getUser(base.getUniqueId()).getSuffix();
		}

		@Override
		public void checkPermissions() {
		}

	}

}
