package de.cyzetlc.mcs.commands;

import de.cyzetlc.mcs.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewBlocksCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (player.hasPermission("*")) {
            if (Main.getInstance().getViewBlockBreakList().contains(player)) {
                Main.getInstance().getViewBlockBreakList().remove(player);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                player.sendMessage(Main.BLOCK_LOG_PREFIX + "§7Du siehst nun §cnicht §7mehr den §eBlock-Log!");
            } else {
                Main.getInstance().getViewBlockBreakList().add(player);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                player.sendMessage(Main.BLOCK_LOG_PREFIX + "§7Du siehst §anun §7den §eBlock-Log!");
            }
        }
        return true;
    }
}
