package de.cyzetlc.mcs.listener;

import de.cyzetlc.mcs.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalListener implements Listener {

    @EventHandler
    public void handlePortalEnter(PlayerPortalEvent event) {
        if (event.getCause() == PlayerPortalEvent.TeleportCause.END_PORTAL) {
            boolean isEndEnabled = Main.getInstance().getConfig().getBoolean("enableEndPortal", false);

            if (!isEndEnabled) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cDas Ende ist momentan deaktiviert!");
            }
        }
    }
}
