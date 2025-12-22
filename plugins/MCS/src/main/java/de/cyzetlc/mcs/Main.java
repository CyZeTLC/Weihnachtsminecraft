package de.cyzetlc.mcs;

import de.cyzetlc.mcs.commands.StatsCommand;
import de.cyzetlc.mcs.commands.UpdateConfigCommand;
import de.cyzetlc.mcs.commands.ViewBlocksCommand;
import de.cyzetlc.mcs.listener.BlockListener;
import de.cyzetlc.mcs.listener.ConnectionListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {
    private static Main instance;

    private List<Player> viewBlockBreakList = new ArrayList<>();

    public static final String BLOCK_LOG_PREFIX = "§e§lBlock-Log §8§l⨠ ";

    private File interactionFile;

    private FileConfiguration interactionCfg;

    private File breakFile;

    private FileConfiguration breakCfg;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        this.loadConfigs();
        this.getLogger().info("Plugin enabled!");
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
        this.getCommand("stats").setExecutor(new StatsCommand());
        this.getCommand("updateconfig").setExecutor(new UpdateConfigCommand());
        this.getCommand("viewblocks").setExecutor(new ViewBlocksCommand());
    }

    public void loadConfigs() {
        this.saveDefaultConfig();

        this.interactionFile = new File(getDataFolder(), "chest-interactions.yml");
        this.breakFile = new File(getDataFolder(), "chest-breaks.yml");

        if (!this.interactionFile.exists()) {
            this.saveResource("chest-interactions.yml", false);
        }

        if (!this.breakFile.exists()) {
            this.saveResource("chest-breaks.yml", false);
        }

        this.interactionCfg = YamlConfiguration.loadConfiguration(interactionFile);
        this.breakCfg = YamlConfiguration.loadConfiguration(breakFile);

        this.autoSaveChestLog();
    }

    public void autoSaveChestLog() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            try {
                if (this.getConfig().getBoolean("enableChestInteractionLog")) {
                    this.interactionCfg.save(this.interactionFile);
                }

                if (this.getConfig().getBoolean("enableChestBreakLog")) {
                    this.breakCfg.save(this.breakFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 20L * 60, 20L * 60);
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

    public FileConfiguration getBreakCfg() {
        return breakCfg;
    }

    public FileConfiguration getInteractionCfg() {
        return interactionCfg;
    }

    public List<Player> getViewBlockBreakList() {
        return viewBlockBreakList;
    }

    public static Main getInstance() {
        return instance;
    }
}