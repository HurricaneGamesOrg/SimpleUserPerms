package org.hurricanegames.simpleuserperms.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.hurricanegames.commandlib.configurations.ConfigurationUtils;
import org.hurricanegames.simpleuserperms.SimpleUserPerms;
import org.hurricanegames.simpleuserperms.utils.MiscUtils;

public class GroupsStorage {

	protected final SimpleUserPerms plugin;

	public GroupsStorage(SimpleUserPerms plugin) {
		this.plugin = plugin;
	}

	protected volatile Group defaultGroup;
	protected final ConcurrentHashMap<String, Group> groups = new ConcurrentHashMap<>();

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

	protected File getStorageFile() {
		return new File(plugin.getDataFolder(), "groups.yml");
	}

	protected static final String PARENT_CFG_STR = "parents";
	protected static final String PERMS_CFG_STR = "permissions";
	protected static final String PREFIX_CFG_STR = "prefix";
	protected static final String SUFFIX_CFG_STR = "suffix";
	protected static final String DEFAULT_CFG_STR = "default";

	public void load() {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getStorageFile());
		groups.clear();
		defaultGroup = null;
		for (String groupName : config.getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection(groupName);
			Group group = new Group(groupName);
			for (String permission : section.getStringList(PERMS_CFG_STR)) {
				group.addPermission(permission);
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
					group.addParentGroup(pgroup);
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
			section.set(PARENT_CFG_STR, MiscUtils.sort(group.getParentGroups().stream().map(Group::getName).collect(Collectors.toCollection(ArrayList::new))));
			section.set(PERMS_CFG_STR, MiscUtils.sort(new ArrayList<>(group.getPermissions())));
			section.set(PREFIX_CFG_STR, group.getPrefix());
			section.set(SUFFIX_CFG_STR, group.getSuffix());
			if (defaultGroup == group) {
				section.set(DEFAULT_CFG_STR, true);
			}
		}
		ConfigurationUtils.safeSave(config, getStorageFile());
	}

}
