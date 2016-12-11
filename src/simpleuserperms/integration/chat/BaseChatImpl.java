package simpleuserperms.integration.chat;

import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import simpleuserperms.SimpleUserPerms;

public abstract class BaseChatImpl extends Chat {

	public BaseChatImpl(Permission perms) {
		super(perms);
	}

	@Override
	public String getName() {
		return JavaPlugin.getPlugin(SimpleUserPerms.class).getName();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}