package org.hurricanegames.simpleuserperms.storage;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Group {

	private final String name;

	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	protected final Set<Group> parentGroups = Collections.newSetFromMap(new ConcurrentHashMap<>());
	protected final Set<String> permissions = Collections.newSetFromMap(new ConcurrentHashMap<>());
	protected String prefix = "";
	protected String suffix = "";

	public Set<Group> getParentGroups() {
		return Collections.unmodifiableSet(parentGroups);
	}

	public boolean hasParentGroup(Group group) {
		return parentGroups.contains(group);
	}

	public void addParentGroup(Group group) {
		parentGroups.add(group);
	}

	public void removeParentGroup(Group group) {
		parentGroups.remove(group);
	}

	public boolean hasPermission(String permission) {
		return permissions.contains(permission);
	}

	public Set<String> getPermissions() {
		return Collections.unmodifiableSet(permissions);
	}

	public void addPermission(String permission) {
		permissions.add(permission);
	}

	public void removePermission(String permission) {
		permissions.remove(permission);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object otherObj) {
		if (!(otherObj instanceof Group)) {
			return false;
		}
		Group other = (Group) otherObj;
		return other.name.equals(name);
	}

	public static String getNameOrNull(Group group) {
		return group != null ? group.getName() : "null";
	}

}
