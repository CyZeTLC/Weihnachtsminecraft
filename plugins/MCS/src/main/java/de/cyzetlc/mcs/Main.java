package de.cyzetlc.mcs;

import de.cyzetlc.mcs.commands.StatsCommand;
import de.cyzetlc.mcs.commands.ViewBlocksCommand;
import de.cyzetlc.mcs.listener.BlockListener;
import de.cyzetlc.mcs.listener.ConnectionListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {
    private static Main instance;

    private List<Player> viewBlockBreakList = new ArrayList<>();

    public static final String BLOCK_LOG_PREFIX = "§e§lBlock-Log §8§l⨠ ";

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        this.getLogger().info("Plugin enabled!");
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        this.getCommand("stats").setExecutor(new StatsCommand());
        this.getCommand("viewblocks").setExecutor(new ViewBlocksCommand());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.getLogger().info("Plugin disabled!");
    }

    @EventHandler
    public void handleDmg(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getEntity().getLocation().distance(e.getEntity().getWorld().getSpawnLocation()) <= 32D) {
                e.setCancelled(true);
            }
        }
    }

    public List<Player> getViewBlockBreakList() {
        return viewBlockBreakList;
    }

    public static Main getInstance() {
        return instance;
    }
}