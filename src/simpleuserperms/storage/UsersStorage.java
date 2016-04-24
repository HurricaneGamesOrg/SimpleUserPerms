package simpleuserperms.storage;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import simpleuserperms.SimpleUserPerms;

public class UsersStorage {

	private final SimpleUserPerms plugin;
	public UsersStorage(SimpleUserPerms plugin) {
		this.plugin = plugin;
	}

	private final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();

	public void recalculateAll() {
		users.values().forEach(User::recalculatePermissions);
	}

	public User getUserIfPresent(UUID uuid) {
		return users.get(uuid);
	}

	public User getUser(UUID uuid) {
		return users.compute(uuid, (euuid, euser) -> euser != null ? euser : new User(euuid));
	}

	public void deleteUser(UUID uuid) {
		users.remove(uuid);
	}

	public void deleteUserIf(UUID uuid, Function<User, Boolean> func) {
		users.compute(uuid, (euuid, euser) -> {
			if (euser != null) {
				return func.apply(euser) ? null : euser;
			}
			return null;
		});
	}

	private File getDataFile() {
		return new File(plugin.getDataFolder(), "users.yml");
	}

	private static final String GROUP_CFG_STR = "group";
	private static final String SUBS_CFG_STR = "subgroups";
	private static final String PERMS_CFG_STR = "permissions";
	private static final String PREFIX_CFG_STR = "prefix";
	private static final String SUFFIX_CFG_STR = "suffix";
	private static final String LASTNAME_CFG_STR = "lastname";

	public void load() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getDataFile());
		GroupsStorage groups = SimpleUserPerms.getGroupsStorage();
		users.clear();
		for (String uuidstr : config.getKeys(false)) {
			try {
				UUID uuid = UUID.fromString(uuidstr);
				ConfigurationSection section = config.getConfigurationSection(uuidstr);
				User user = new User(uuid);

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

				user.setLastName(section.getString(LASTNAME_CFG_STR));

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
			section.set(GROUP_CFG_STR, user.getMainGroup().getName());
			section.set(SUBS_CFG_STR, user.getSubGroups().stream().map(Group::getName).collect(Collectors.toList()));
			section.set(PERMS_CFG_STR, Utils.sort(user.getAdditionalPermissions()));
			section.set(PREFIX_CFG_STR, user.prefix);
			section.set(SUFFIX_CFG_STR, user.suffix);
			section.set(LASTNAME_CFG_STR, user.getLastName());
		}
		try {
			config.save(getDataFile());
		} catch (IOException e) {
		}
	}

}
