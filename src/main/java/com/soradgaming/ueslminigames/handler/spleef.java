package com.soradgaming.ueslminigames.handler;

import com.soradgaming.ueslminigames.UESLMiniGames;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class spleef {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;
    private static ArrayList<UUID> playerList;
    private static boolean Started = false;
    private static final BukkitScheduler scheduler = Bukkit.getScheduler();
    private static boolean gamesStats = false;

    public static void startSpleef(ArrayList<UUID> input) {
        playerList = input;
        Started = true;
        gamesStats = false;
        for (UUID uuid : playerList) {
            Player p = Bukkit.getPlayer(uuid);
            Objects.requireNonNull(p).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("spleef_message"))));
            Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("spleef_start_command")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("spleef_rule"))));
        }
        // With BukkitScheduler
        scheduler.runTaskTimer(plugin, spleef::gameStatusLoop, 20L * 30 /*<-- the initial delay */, 20L /*<-- the interval */);
    }

    public static void endSpleef() {
        scheduler.cancelTasks(plugin);
        if (Started) {
            Started = false;
            for (UUID uuid : playerList) {
                Player p = Bukkit.getPlayer(uuid);
                plugin.data.set(uuid + ".finalSpleefWins", getNewWins(p));
                plugin.data.set(uuid + ".finalSpleefLoses", getNewLoses(p));
                //Save Data
                plugin.saveFile();
                if (plugin.data.getInt(uuid + ".finalSpleefWins") > 0) {
                    int oldPoints = plugin.data.getInt(uuid + ".points");
                    plugin.data.set(uuid + ".points", plugin.getConfig().getInt("spleef_first") + oldPoints);
                }
                Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("spleef_end_command")));
            }
        }
    }

    public static void gameStatusLoop() {
        Player player = Bukkit.getPlayer(playerList.get(0));
        if (!gamesStats && getGameStatus(player).equals("§cIn Game")) {
            gamesStats = true;
        }
        if (gamesStats && getGameStatus(player).equals("§aWaiting")) {
            endSpleef();
        }
    }


    public static String getGameStatus(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%spleef_" + Objects.requireNonNull(plugin.getConfig().getString("spleef_arena")).trim() + "_inGame%");
    }

    public static int getWins(Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%spleef_win%"));
    }

    public static int getLoses(Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%spleef_loose%"));
    }

    public static int getNewWins(Player player) {
        int newWins = getWins(player);
        int oldWins = plugin.data.getInt(player.getUniqueId() + ".initialSpleefWins");
        return newWins - oldWins;
    }

    public static int getNewLoses(Player player) {
        int newLoses = getLoses(player);
        int oldLoses = plugin.data.getInt(player.getUniqueId() + ".initialSpleefLoses");
        return newLoses - oldLoses;
    }
}
