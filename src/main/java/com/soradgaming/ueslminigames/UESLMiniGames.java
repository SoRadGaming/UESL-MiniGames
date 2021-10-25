package com.soradgaming.ueslminigames;

import com.soradgaming.ueslminigames.commands.CommandTabCompleter;
import com.soradgaming.ueslminigames.commands.UESLCommand;
import com.soradgaming.ueslminigames.handler.*;
import com.soradgaming.ueslminigames.listeners.playerJoin;
import com.soradgaming.ueslminigames.listeners.playerLeave;
import com.soradgaming.ueslminigames.managment.bracketManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class UESLMiniGames extends JavaPlugin implements Listener {

    public static UESLMiniGames plugin;
    public boolean startedCommand = false;
    public File dataFile = new File(getDataFolder() + "/data/players.yml");
    public FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        //Check Dependencies
        checkPlugins();

        //Load EventHandler and Commands
        loadMethod();

        //Load Data
        loadFile();

        //Config
        registerConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("The plugin has been disabled correctly!");
    }

    //Loads all the Events and Commands
    public void loadMethod() {
        //Registers Commands
        Objects.requireNonNull(getCommand("umg")).setExecutor(new UESLCommand());
        Objects.requireNonNull(getCommand("umg")).setTabCompleter(new CommandTabCompleter());
        //Handlers
        Bukkit.getServer().getPluginManager().registerEvents(new tntrunX(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new parkour(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new hungergames(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new bedwars(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new buildbattle(), this);
        //Listener
        Bukkit.getServer().getPluginManager().registerEvents(new playerJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new playerLeave(), this);
        //Custom Events
        Bukkit.getServer().getPluginManager().registerEvents(new bracketManager(), this);
    }

    //Save the data file.
    public void saveFile() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    //Load the data file
    public void loadFile() {
        if (dataFile.exists()) {
            try {
                data.load(dataFile);

            } catch (IOException | InvalidConfigurationException e) {

                e.printStackTrace();
            }
        } else {
            try {
                data.save(dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Config
    private void registerConfig() {
        saveDefaultConfig();
    }

    private void checkPlugins() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Hooked into PlaceholderAPI");
        } else {
            throw new RuntimeException("Could not find PlaceholderAPI! Plugin can not work without it!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PaintballBattle")) {
            getLogger().info("Hooked into PaintballBattle");
        } else {
            throw new RuntimeException("Could not find PaintballBattle! Plugin can not work without it!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("HungerGames")) {
            getLogger().info("Hooked into HungerGames");
        } else {
            throw new RuntimeException("Could not find HungerGames! Plugin can not work without it!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("TNTRun_reloaded")) {
            getLogger().info("Hooked into TNTRun_reloaded");
        } else {
            throw new RuntimeException("Could not find TNTRun_Reloaded! Plugin can not work without it!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Parkour")) {
            getLogger().info("Hooked into Parkour");
        } else {
            throw new RuntimeException("Could not find Parkour! Plugin can not work without it!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("BedWars")) {
            getLogger().info("Hooked into BedWars");
        } else {
            throw new RuntimeException("Could not find ScreamingBedWars! Plugin can not work without it!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Spleef")) {
            getLogger().info("Hooked into Spleef");
        } else {
            throw new RuntimeException("Could not find Spleef! Plugin can not work without it!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("BuildBattle")) {
            getLogger().info("Hooked into BuildBattle");
        } else {
            throw new RuntimeException("Could not find BuildBattle! Plugin can not work without it!");
        }
    }
}
