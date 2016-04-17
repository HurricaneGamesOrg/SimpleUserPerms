package simpleuserperms.storage;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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

	public User getUserIfPresent(UUID uuid) {
		return users.get(uuid);
	}

	public User getUser(UUID uuid) {
		return users.compute(uuid, (euuid, euser) -> euser != null ? euser : new User());
	}

	public void deleteUser(UUID uuid) {
		users.compute(uuid, (euuid, euser) -> {
			User tuser = new User();
			if (euser != null) {
				tuser.setPlayerRef(euser.getPlayerRef());
			}
			return tuser;
		});
	}

	private File getDataFile() {
		return new File(plugin.getDataFolder(), "users.yml");
	}

	private static final String GROUP_CFG_STR = "group";
	private static final String SUBS_CFG_STR = "subgroups";
	private static final String PERMS_CFG_STR = "permissions";
	private static final String PREFIX_CFG_STR = "prefix";

	public void load() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getDataFile());
		GroupsStorage groups = SimpleUserPerms.getGroupsStorage();
		users.clear();
		for (String uuidstr : config.getKeys(false)) {
			try {
				UUID uuid = UUID.fromString(uuidstr);
				ConfigurationSection section = config.getConfigurationSection(uuidstr);
				User user = new User();

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

				users.put(uuid, user);
				user.recalculatePermissions();
			} catch (Throwable t) {
			}
		}
	}

	public void save() {
		YamlConfiguration config = new YamlConfiguration();
		for (Entry<UUID, User> entry : users.entrySet()) {
			User user = entry.getValue();
			if (
				user.group == SimpleUserPerms.getGroupsStorage().getDefaultGroup() &&
				user.subGroups.isEmpty() &&
				user.additionalPerms.isEmpty() &&
				user.prefix == null
			) {
				continue;
			}
			ConfigurationSection section = config.createSection(entry.getKey().toString());
			section.set(GROUP_CFG_STR, user.getMainGroup().getName());
			section.set(SUBS_CFG_STR, user.getSubGroups().stream().map(Group::getName).collect(Collectors.toList()));
			section.set(PERMS_CFG_STR, user.getAdditionalPermissions());
			section.set(PREFIX_CFG_STR, user.getRawPrefix());
		}
		try {
			config.save(getDataFile());
		} catch (IOException e) {
		}
	}

}
