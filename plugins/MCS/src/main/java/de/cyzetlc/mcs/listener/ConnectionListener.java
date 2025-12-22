/*
 * Copyright 2025 Tom Coombs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            if (Main.getInstance().getConfig().getBoolean("displayAdminPrefix")) {
                p.setPlayerListName("§cA: §r" + p.getName());
            }

            if (Main.getInstance().getViewBlockBreakList().contains(p)) {
                p.sendMessage(Main.BLOCK_LOG_PREFIX + "§7Der §eBlock-Log §7ist für dich §aaktiviert§7!");
            }
        }
    }
}
