package de.cyzetlc.mcs.commands;

import de.cyzetlc.mcs.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UpdateConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (player.hasPermission("*")) {
            Main.getInstance().reloadConfig();

            this.updatePlayerPrefixes();

            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            player.sendMessage("§aConfig neugeladen!");
        }
        return true;
    }

    private void updatePlayerPrefixes() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("*")) {
                if (Main.getInstance().getConfig().getBoolean("displayAdminPrefix")) {
                    player.setPlayerListName("§cA: §r" + player.getName());
                }
            }
        }
    }
}
