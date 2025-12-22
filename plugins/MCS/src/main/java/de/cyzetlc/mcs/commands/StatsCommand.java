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
package de.cyzetlc.mcs.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 0) {
            showStats((Player) sender, player.getUniqueId(), player.getName());
            return true;
        } else if (args.length != 1) {
            sender.sendMessage("§cVerwendung: /stats <Spieler>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (target == null) {
            sender.sendMessage("§cSpieler nicht gefunden.");
            return true;
        }

        showStats((Player) sender, target.getUniqueId(), target.getName());
        return true;
    }

    public void showStats(Player viewer, UUID uuid, String playerName) {
        try {
            File statsFile = new File(
                    Bukkit.getWorldContainer(),
                    "Weihnachtsminecraft Welt/stats/" + uuid + ".json"
            );

            if (!statsFile.exists()) {
                viewer.sendMessage("§cKeine Stats-Datei gefunden.");
                return;
            }

            JsonObject root = JsonParser.parseReader(new FileReader(statsFile)).getAsJsonObject();
            JsonObject stats = root.getAsJsonObject("stats");

            JsonObject custom = stats.getAsJsonObject("minecraft:custom");
            //JsonObject killed = stats.getAsJsonObject("minecraft:killed");

            int deaths = getInt(custom, "minecraft:deaths");
            int mobKills = getInt(custom, "minecraft:mob_kills");
            int damageDealt = getInt(custom, "minecraft:damage_dealt");
            int damageTaken = getInt(custom, "minecraft:damage_taken");

            long playTimeTicks = getLong(custom, "minecraft:play_time");
            long playTimeMinutes = playTimeTicks / 20 / 60;

            int playerKills = getInt(custom, "minecraft:player_kills");

            viewer.sendMessage("§8§m-------------------------");

            viewer.sendMessage("§6Stats von §e" + playerName);
            viewer.sendMessage("§7§lSpielzeit: §a" + playTimeMinutes + " Minuten");
            viewer.sendMessage("§7§lTode: §c" + deaths);
            viewer.sendMessage("§7§lMob-Kills: §a" + mobKills);
            viewer.sendMessage("§7§lSpieler-Kills: §c" + playerKills);
            viewer.sendMessage("§7§lDamage ausgeteilt: §e" + damageDealt);
            viewer.sendMessage("§7§lDamage erhalten: §e" + damageTaken);

            if (viewer.hasPermission("*")) {
                BanList<?> banList = Bukkit.getBanList(BanList.Type.NAME);
                BanEntry<?> banEntry = banList.getBanEntry(playerName);

                if (banEntry != null) {
                    viewer.sendMessage("§8§m-----------");
                    viewer.sendMessage("§7§lStatus: §cSpieler ist gebannt!");
                    viewer.sendMessage("§7§lGrund: §e" + safe(banEntry.getReason()));
                    viewer.sendMessage("§7§lGebannt von: §e" + safe(banEntry.getSource()));
                    viewer.sendMessage("§7§lSeit: §e" + formatDate(banEntry.getCreated()));

                    if (banEntry.getExpiration() != null) {
                        viewer.sendMessage("§7§lBis: §e" + formatDate(banEntry.getExpiration()));
                    } else {
                        viewer.sendMessage("§7§lDauer: §cPermanent");
                    }
                }
            }

            viewer.sendMessage("§8§m-------------------------");

        } catch (Exception e) {
            viewer.sendMessage("§cFehler beim Laden der Stats.");
            e.printStackTrace();
        }
    }

    private String safe(String value) {
        return value != null ? value : "Unbekannt";
    }

    private String formatDate(Date date) {
        if (date == null) return "Unbekannt";

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return sdf.format(date);
    }

    private int getInt(JsonObject obj, String key) {
        if (obj == null || !obj.has(key)) return 0;
        return obj.get(key).getAsInt();
    }

    private long getLong(JsonObject obj, String key) {
        if (obj == null || !obj.has(key)) return 0L;
        return obj.get(key).getAsLong();
    }
}
