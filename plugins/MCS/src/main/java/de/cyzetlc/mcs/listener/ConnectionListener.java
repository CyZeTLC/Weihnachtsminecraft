package de.cyzetlc.mcs.listener;

import de.cyzetlc.mcs.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectionListener implements Listener {
    @EventHandler
    public void handleJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (p.hasPermission("*")) {
            p.setPlayerListName("§cA: §r" + e.getPlayer().getName());

            if (Main.getInstance().getViewBlockBreakList().contains(p)) {
                p.sendMessage(Main.BLOCK_LOG_PREFIX + "§7Der §eBlock-Log §7ist für dich §aaktiviert§7!");
            }
        }
    }
}
