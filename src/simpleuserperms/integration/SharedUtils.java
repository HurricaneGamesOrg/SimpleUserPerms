package simpleuserperms.integration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

	public static boolean hasPermission(User user, String permission) {
		Player player = Bukkit.getPlayer(user.getUUID());
		if (player != null) {
			if (player.isPermissionSet(permission)) {
				return player.hasPermission(permission);
			}
		}
		try (ReadLockedEffectivePermissions perms = user.getDirectEffectivePermissions()) {
			Map<String, Boolean> effective = perms.getEffectivePermissions();
			Boolean hasAllResult = effective.get("*");
			if (hasAllResult != null) {
				return hasAllResult;
			}
			Boolean simpleResult = effective.get(permission);
			if (simpleResult != null) {
				return simpleResult;
			}
			for (String part : new StringPartIterator(permission, '.')) {
				Boolean result = effective.get(part + ".*");
				if (result != null) {
					return result;
				}
			}
			return false;
		}
	}

	public static class StringPartIterator implements Iterator<String>, Iterable<String> {

		private final char[] string;
		private final char partDelim;
		public StringPartIterator(String string, char partDelim) {
			this.string = string.toCharArray();
			this.partDelim = partDelim;
			this.delimIndex = string.length() - 1;
			findNextDelimIndex();
		}

		private int delimIndex;

		private void findNextDelimIndex() {
			while (delimIndex > 0) {
				if (string[delimIndex--] == partDelim) {
					break;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return delimIndex > 0;
		}

		@Override
		public String next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			int lastIndex = delimIndex + 1;
			findNextDelimIndex();
			return new String(string, 0, lastIndex);
		}

		@Override
		public Iterator<String> iterator() {
			return this;
		}

	}

}
