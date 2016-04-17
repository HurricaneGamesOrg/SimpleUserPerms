package simpleuserperms.integration;

import java.util.HashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public abstract class Integration implements Listener {

	protected static final HashMap<String, Class<? extends Integration>> integrations = new HashMap<>();
	static {
		integrations.put("Vault", VaultIntegration.class);
		integrations.put("Essentials", EssentialsIntegration.class);
		integrations.put("WorldEdit", WEPIFIntegration.class);
	}

	protected abstract void load();

	public static class IntegrationListener implements Listener {

		@EventHandler
		public void onPluginLoad(PluginEnableEvent event) {
			Class<? extends Integration> integration = integrations.get(event.getPlugin().getName());
			if (integration != null) {
				try {
					integration.newInstance().load();
				} catch (Throwable e) {
					System.err.println("Unable to load integration for "+event.getPlugin().getName());
					e.printStackTrace();
				}
			}
		}
		
	}
	

}
