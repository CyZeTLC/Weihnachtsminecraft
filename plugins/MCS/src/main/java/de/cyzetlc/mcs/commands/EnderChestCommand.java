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

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTFile;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class EnderChestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length == 0) {
            player.openInventory(player.getEnderChest());
        } else if (args.length == 1 && player.hasPermission("*")) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                this.openOfflineEnderchest(player, args[0]);
                return true;
            }

            player.openInventory(target.getEnderChest());
        }

        return true;
    }

    private void openOfflineEnderchest(Player admin, String targetName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        File worldFolder = Bukkit.getWorlds().get(0).getWorldFolder();
        File playerFile = new File(worldFolder, "playerdata/" + target.getUniqueId() + ".dat");

        if (!playerFile.exists()) {
            admin.sendMessage("§cSpielerdatei nicht gefunden!");
            return;
        }

        try {
            NBTFile nbt = new NBTFile(playerFile);
            NBTCompoundList enderList = nbt.getCompoundList("EnderItems");
            Inventory gui = Bukkit.createInventory(null, 27, "Enderchest: " + targetName);

            for (int i = 0; i < enderList.size(); i++) {
                NBTCompound itemTag = enderList.get(i);
                int slot = itemTag.getInteger("Slot");

                ItemStack item = NBTItem.convertNBTtoItem(itemTag);

                if (item != null && slot >= 0 && slot < 27) {
                    gui.setItem(slot, item);
                }
            }

            admin.openInventory(gui);
        } catch (Exception e) {
            admin.sendMessage("§cFehler beim Laden der Enderchest.");
            e.printStackTrace();
        }
    }
}
