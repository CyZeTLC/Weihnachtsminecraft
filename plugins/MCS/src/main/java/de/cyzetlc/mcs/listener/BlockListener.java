package de.cyzetlc.mcs.listener;

import de.cyzetlc.mcs.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

public class BlockListener implements Listener {
    private static final Set<Material> VALUABLE_BLOCKS = EnumSet.of(
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.ANCIENT_DEBRIS,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE
    );

    @EventHandler
    public void handlePlaceBlock(BlockPlaceEvent e) {
        if (e.getPlayer().getLocation().distance(e.getPlayer().getWorld().getSpawnLocation()) <= 32D) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getLocation().distance(e.getPlayer().getWorld().getSpawnLocation()) <= 32D) {
            e.setCancelled(true);
        }

        if (VALUABLE_BLOCKS.contains(e.getBlock().getType())) {
            for (Player admin : Main.getInstance().getViewBlockBreakList()) {
                admin.sendMessage(Main.BLOCK_LOG_PREFIX + "§7Spieler " + e.getPlayer().getName() + " §c-> §7" + e.getBlock().getType().name() + " bei " + e.getBlock().getLocation().toVector());
            }
        }

        if (isChest(e.getBlock().getType())) {
            if (Main.getInstance().getConfig().getBoolean("enableChestBreakLog")) {
                logChestBreak(e.getPlayer(), e.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    public void handleChestInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() == null) return;

        Material type = e.getClickedBlock().getType();

        if (!isChest(type)) return;

        if (Main.getInstance().getConfig().getBoolean("enableChestInteractionLog")) {
            logChestInteract(e.getPlayer(), e.getClickedBlock().getLocation());
        }
    }

    public void logChestInteract(Player player, Location loc) {
        String id = String.valueOf(System.currentTimeMillis());
        FileConfiguration interactionCfg = Main.getInstance().getInteractionCfg();

        interactionCfg.set("interactions." + id + ".player", player.getName());
        interactionCfg.set("interactions." + id + ".uuid", player.getUniqueId().toString());
        interactionCfg.set("interactions." + id + ".world", loc.getWorld().getName());
        interactionCfg.set("interactions." + id + ".x", loc.getBlockX());
        interactionCfg.set("interactions." + id + ".y", loc.getBlockY());
        interactionCfg.set("interactions." + id + ".z", loc.getBlockZ());
        interactionCfg.set("interactions." + id + ".time", formatNow());
        interactionCfg.set("interactions." + id + ".action", "OPEN");
    }

    private void logChestBreak(Player player, Location loc) {
        String id = String.valueOf(System.currentTimeMillis());
        FileConfiguration cfg = Main.getInstance().getBreakCfg();

        cfg.set("breaks." + id + ".player", player.getName());
        cfg.set("breaks." + id + ".uuid", player.getUniqueId().toString());
        cfg.set("breaks." + id + ".world", loc.getWorld().getName());
        cfg.set("breaks." + id + ".x", loc.getBlockX());
        cfg.set("breaks." + id + ".y", loc.getBlockY());
        cfg.set("breaks." + id + ".z", loc.getBlockZ());
        cfg.set("breaks." + id + ".time", formatNow());
    }

    private boolean isChest(Material material) {
        return material == Material.CHEST
                || material == Material.TRAPPED_CHEST
                || material == Material.BARREL;
    }

    private String formatNow() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
    }
}
