package simpleuserperms.storage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import simpleuserperms.SimpleUserPerms;

public class GroupsStorage {

	private final SimpleUserPerms plugin;
	public GroupsStorage(SimpleUserPerms plugin) {
		this.plugin = plugin;
	}

	private Group defaultGroup;
	private final ConcurrentHashMap<String, Group> groups = new ConcurrentHashMap<>();

	public List<String> getGroupNames() {
		return Collections.list(groups.keys());
	}

	public Group getDefaultGroup() {
		return defaultGroup;
	}

	public Group getGroup(String name) {
		return groups.get(name);
	}

	public Group getGroupOrDefault(String name) {
		Group group = getGroup(name);
		if (group == null) {
			group = getDefaultGroup();
		}
		return group;
	}

	private File getDataFile() {
		return new File(plugin.getDataFolder(), "groups.yml");
	}

	private static final String PARENT_CFG_STR = "parents";
	private static final String PERMS_CFG_STR = "permissions";
	private static final String PREFIX_CFG_STR = "prefix";
	private static final String SUFFIX_CFG_STR = "suffix";
	private static final String DEFAULT_CFG_STR = "default";

	public void load() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getDataFile());
		groups.clear();
		defaultGroup = null;
		for (String groupName : config.getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection(groupName);
			Group group = new Group(groupName);
			for (String permission : section.getStringList(PERMS_CFG_STR)) {
				group.addPermission(permission, false);
			}
			group.setPrefix(section.getString(PREFIX_CFG_STR));
			group.setSuffix(section.getString(SUFFIX_CFG_STR));
			groups.put(groupName, group);
			if (section.getBoolean(DEFAULT_CFG_STR)) {
				defaultGroup = group;
			}
		}
		for (Group group : groups.values()) {
			ConfigurationSection section = config.getConfigurationSection(group.getName());
			for (String parentGroupName : section.getStringList(PARENT_CFG_STR)) {
				Group pgroup = getGroup(parentGroupName);
				if (pgroup != null) {
					group.addParentGroup(pgroup, false);
				}
			}
		}
		if (defaultGroup == null) {
			if (groups.isEmpty()) {
				defaultGroup = new Group("default");
				groups.put(defaultGroup.getName(), defaultGroup);
			} else {
				defaultGroup = groups.entrySet().iterator().next().getValue();
			}
		}
	}

	public void save() {
		YamlConfiguration config = new YamlConfiguration();
		for (Group group : groups.values()) {
			ConfigurationSection section = config.createSection(group.getName());
			section.set(PARENT_CFG_STR, group.getParentGroups().stream().map(Group::getName).collect(Collectors.toList()));
			section.set(PERMS_CFG_STR, group.getPermissions());
			section.set(PREFIX_CFG_STR, group.prefix);
			section.set(SUFFIX_CFG_STR, group.suffix);
			if (defaultGroup == group) {
				section.set(DEFAULT_CFG_STR, true);
			}
		}
		try {
			config.save(getDataFile());
		} catch (IOException e) {
		}
	}

}
