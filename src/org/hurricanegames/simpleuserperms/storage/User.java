package org.hurricanegames.simpleuserperms.storage;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class User {

	protected final GroupsStorage groups;

	protected final UUID uuid;
	protected volatile String lastName;

	protected volatile Group mainGroup;
	protected final Set<Group> subGroups = Collections.newSetFromMap(new ConcurrentHashMap<>());
	protected final Set<String> additionalPermissions = Collections.newSetFromMap(new ConcurrentHashMap<>());
	protected volatile String prefix;
	protected volatile String suffix;

	protected volatile Map<String, Boolean> activePermissions = new ConcurrentHashMap<>();
	protected volatile String activeSuffix;
	protected volatile String activePrefix;

	protected User(GroupsStorage groups, UUID uuid, boolean recalculate) {
		this.groups = groups;
		this.uuid = uuid;
		this.mainGroup = groups.getDefaultGroup();
		if (recalculate) {
			this.recalculate();
		}
	}

	public boolean isDefault() {
		return
			(mainGroup == groups.getDefaultGroup()) &&
			subGroups.isEmpty() &&
			additionalPermissions.isEmpty() &&
			(prefix == null) &&
			(suffix == null);
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setMainGroup(Group group) {
		setMainGroup(group, true);
	}

	public void setMainGroup(Group group, boolean recalculateNow) {
		this.mainGroup = group;
		if (recalculateNow) {
			recalculate();
		}
	}

	public Group getMainGroup() {
		return mainGroup;
	}

	public void addSubGroup(Group group) {
		addSubGroup(group, true);
	}

	public void addSubGroup(Group group, boolean recalculateNow) {
		subGroups.add(group);
		if (recalculateNow) {
			recalculate();
		}
	}

	public void removeSubGroup(Group group) {
		removeSubGroup(group, true);
	}

	public void removeSubGroup(Group group, boolean recalculateNow) {
		subGroups.remove(group);
		if (recalculateNow) {
			recalculate();
		}
	}

	public boolean hasSubGroup(Group group) {
		return subGroups.contains(group);
	}

	public Set<Group> getSubGroups() {
		return Collections.unmodifiableSet(subGroups);
	}

	public boolean hasGroup(Group group) {
		 return (mainGroup == group) || subGroups.contains(group);
	}

	public void addAdditionalPermission(String permission) {
		addAdditionalPermission(permission, true);
	}

	public void addAdditionalPermission(String permission, boolean recalculateNow) {
		additionalPermissions.add(permission);
		if (recalculateNow) {
			calculatePermission(permission, activePermissions);
		}
	}

	public void removeAdditionalPermission(String permission) {
		removeAdditionalPermission(permission, false);
	}

	public void removeAdditionalPermission(String permission, boolean recalculateNow) {
		additionalPermissions.remove(permission);
		if (recalculateNow) {
			activePermissions.remove(permission);
		}
	}

	public boolean hasAdditionalPermission(String permission) {
		return additionalPermissions.contains(permission);
	}

	public Set<String> getAdditionalPermissions() {
		return Collections.unmodifiableSet(additionalPermissions);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
		recalculatePrefix();
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
		recalculateSuffix();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void recalculate() {
		Map<String, Boolean> newActivePermissions = new ConcurrentHashMap<>();

		subGroups.forEach(sGroup -> calculateGroupPerms(sGroup, newActivePermissions));
		calculateGroupPerms(mainGroup, newActivePermissions);
		additionalPermissions.forEach(aPerm -> calculatePermission(aPerm, newActivePermissions));

		this.activePermissions = newActivePermissions;

		recalculatePrefix();
		recalculateSuffix();
	}

	protected void recalculatePrefix() {
		String newActivePrefix = this.prefix;
		if (newActivePrefix == null) {
			newActivePrefix = getGroupEffectivePrefix(mainGroup);
		}

		this.activePrefix = newActivePrefix;
	}

	protected void recalculateSuffix() {
		String newActiveSuffix = this.suffix;
		if (newActiveSuffix == null) {
			newActiveSuffix = getGroupEffectivePrefix(mainGroup);
		}

		this.activeSuffix = newActiveSuffix;
	}

	protected static String getGroupEffectivePrefix(Group group) {
		String prefix = group.getPrefix();
		if (prefix != null) {
			return prefix;
		}
		for (Group pGroup : group.getParentGroups()) {
			prefix = getGroupEffectivePrefix(pGroup);
			if (prefix != null) {
				return prefix;
			}
		}
		return null;
	}

	protected static String getGroupEffectiveSuffix(Group group) {
		String suffix = group.getPrefix();
		if (suffix != null) {
			return suffix;
		}
		for (Group pGroup : group.getParentGroups()) {
			suffix = getGroupEffectiveSuffix(pGroup);
			if (suffix != null) {
				return suffix;
			}
		}
		return null;
	}

	protected static void calculateGroupPerms(Group group, Map<String, Boolean> to) {
		group.parentGroups.forEach(pGroup -> calculateGroupPerms(pGroup, to));
		group.permissions.forEach(gPerms -> calculatePermission(gPerms, to));
	}

	protected static void calculatePermission(String permission, Map<String, Boolean> to) {
		if (permission.startsWith("-")) {
			to.put(permission.substring(1), Boolean.FALSE);
		} else {
			to.put(permission, Boolean.TRUE);
		}
	}

	public Map<String, Boolean> getEffectivePermissions() {
		return Collections.unmodifiableMap(activePermissions);
	}

	public String getEffectivePrefix() {
		return activePrefix;
	}

	public String getEffectiveSuffix() {
		return activeSuffix;
	}

}
