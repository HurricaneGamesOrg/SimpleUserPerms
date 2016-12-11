package simpleuserperms.integration;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import simpleuserperms.integration.chat.DeprecatedChatImpl;
import simpleuserperms.integration.perms.DeprecatedPermImpl;

public class VaultIntegration {

	private final Permission pimpl = new DeprecatedPermImpl();
	private final Chat cimpl = new DeprecatedChatImpl(pimpl);

	public void load() {
		Bukkit.getServicesManager().register(Permission.class, pimpl, JavaPlugin.getPlugin(Vault.class), ServicePriority.Highest);
		Bukkit.getServicesManager().register(Chat.class, cimpl, JavaPlugin.getPlugin(Vault.class), ServicePriority.Highest);
	}

	public void unload() {
		Bukkit.getServicesManager().unregister(pimpl);
		Bukkit.getServicesManager().unregister(cimpl);
	}

}
