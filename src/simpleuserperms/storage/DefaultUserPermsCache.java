package simpleuserperms.storage;

import java.util.Map;

public class DefaultUserPermsCache {

	private static Map<String, Boolean> defaultPerms;

	public static void applyDefaultPermsTo(User user) {
		user.activePermissions.putAll(defaultPerms);
	}

	public static void recalculateDefaultPerms() {
		User user = new User(false);
		user.calculateGroupPerms(user.group);
		defaultPerms = user.activePermissions;
	}

}
