package simpleuserperms.integration.perms;

import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;
import simpleuserperms.SimpleUserPerms;

public abstract class BasicPermImpl extends Permission {

	@Override
	public String getName() {
		return JavaPlugin.getPlugin(SimpleUserPerms.class).getName();
	}

	@Override
	public boolean hasGroupSupport() {
		return true;
	}

	@Override
	public boolean hasSuperPermsCompat() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String[] getGroups() {
		return SimpleUserPerms.getGroupsStorage().getGroupNames().toArray(new String[0]);
	}
	
}