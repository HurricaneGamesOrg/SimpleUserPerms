package simpleuserperms.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import simpleuserperms.storage.Group;
import simpleuserperms.storage.User;
import simpleuserperms.storage.User.ReadLockedEffectivePermissions;

public class SharedUtils {

	public static List<String> getUserGroupsL(User user) {
		ArrayList<String> result = new ArrayList<String>();
		result.add(user.getMainGroup().getName());
		result.addAll(user.getSubGroups().stream().map(Group::getName).collect(Collectors.toList()));
		return result;
	}

	public static String[] getUserGroupsA(User user) {
		return getUserGroupsL(user).toArray(new String[0]);
	}

	private static final Pattern splitP = Pattern.compile("[.]"); 
	public static boolean hasPermission(User user, String permission) {
		Player player = Bukkit.getPlayer(user.getUUID());
		if (player != null) {
			if (player.isPermissionSet(permission)) {
				return player.hasPermission(permission);
			}
		}
		try (ReadLockedEffectivePermissions perms = user.getDirectEffectivePermissions()) {
			Map<String, Boolean> effective = perms.getEffectivePermissions();
			Boolean simpleResult = effective.get(permission);
			if (simpleResult != null) {
				return simpleResult;
			}
			Boolean allResult = effective.get("*");
			if (allResult != null) {
				return allResult;
			}
			String[] split = splitP.split(permission);
			for (int i = split.length - 1; i > 0; i--) {
				Boolean result = effective.get(makeWildCard(split, i));
				if (result != null) {
					return result;
				}
			}
			return false;
		}
	}
	private static String makeWildCard(String[] split, int len) {
		ArrayList<String> parts = new ArrayList<String>();
		parts.addAll(Arrays.asList(Arrays.copyOfRange(split, 0, len)));
		parts.add("*");
		return String.join(".", parts);
	}

}
