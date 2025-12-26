package de.cyzetlc.mcs.listener;

import de.cyzetlc.mcs.Main;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    @EventHandler
    public void handleChat(AsyncChatEvent e) {
        if (Main.getInstance().getConfig().getBoolean("enableChatFormat")) {
            e.renderer((player, sourceDisplayName, message, audience) -> {
                if (!(audience instanceof Player)) {
                    return message;
                }

                Component formattedMessage = sourceDisplayName.append(Component.text(" ⨠ ")).append(message).color(NamedTextColor.GRAY);
                return formattedMessage;
            });
        }
    }
}
