package org.hurricanegames.simpleuserperms.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.hurricanegames.commandlib.configurations.ConfigurationUtils;
import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.utils.MiscUtils;

public class UsersStorage {

	protected final SimpleUserPerms plugin;
	protected final GroupsStorage groups;

	public UsersStorage(SimpleUserPerms plugin, GroupsStorage groups) {
		this.plugin = plugin;
		this.groups = groups;
	}

	protected final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();

	public void recalculateAll() {
		users.values().forEach(User::recalculate);
	}

	public User getUser(UUID uuid) {
		return users.computeIfAbsent(uuid, k -> new User(groups, k, true));
	}

	public void deleteUser(UUID uuid) {
		users.remove(uuid);
	}

	public void deleteUserIf(UUID uuid, Predicate<User> func) {
		users.compute(uuid, (euuid, euser) -> {
			if (euser != null) {
				return func.test(euser) ? null : euser;
			}
			return null;
		});
	}

	protected File getStorageFile() {
		return new File(plugin.getDataFolder(), "users.yml");
	}

	protected static final String GROUP_CFG_STR = "group";
	protected static final String SUBS_CFG_STR = "subgroups";
	protected static final String PERMS_CFG_STR = "permissions";
	protected static final String PREFIX_CFG_STR = "prefix";
	protected static final String SUFFIX_CFG_STR = "suffix";
	protected static final String LASTNAME_CFG_STR = "lastname";

	public void load() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getStorageFile());
		users.clear();
		for (String uuidstr : config.getKeys(false)) {
			try {
				UUID uuid = UUID.fromString(uuidstr);
				ConfigurationSection section = config.getConfigurationSection(uuidstr);

				User user = new User(groups, uuid, false);
				user.setLastName(section.getString(LASTNAME_CFG_STR));

				user.setMainGroup(groups.getGroupOrDefault(section.getString(GROUP_CFG_STR)), false);

				for (String subGroupName : section.getStringList(SUBS_CFG_STR)) {
					Group subGroup = groups.getGroup(subGroupName);
					if (subGroup != null) {
						user.addSubGroup(subGroup, false);
					}
				}

				for (String permission : section.getStringList(PERMS_CFG_STR)) {
					user.addAdditionalPermission(permission, false);
				}

				user.setPrefix(section.getString(PREFIX_CFG_STR));
				user.setSuffix(section.getString(SUFFIX_CFG_STR));

				user.recalculate();

				users.put(uuid, user);
			} catch (Throwable t) {
			}
		}
	}

	public void save() {
		YamlConfiguration config = new YamlConfiguration();
		for (Entry<UUID, User> entry : users.entrySet()) {
			User user = entry.getValue();
			if (user.isDefault()) {
				continue;
			}
			ConfigurationSection section = config.createSection(entry.getKey().toString());
			section.set(LASTNAME_CFG_STR, user.getLastName());
			section.set(PREFIX_CFG_STR, user.getPrefix());
			section.set(SUFFIX_CFG_STR, user.getSuffix());
			section.set(GROUP_CFG_STR, user.getMainGroup().getName());
			section.set(SUBS_CFG_STR, MiscUtils.sort(user.getSubGroups().stream().map(Group::getName).collect(Collectors.toCollection(ArrayList::new))));
			section.set(PERMS_CFG_STR, MiscUtils.sort(new ArrayList<>(user.getAdditionalPermissions())));
		}
		ConfigurationUtils.safeSave(config, getStorageFile());
	}

}
