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

public class paintball {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;
    private static ArrayList<UUID> playerList;
    private static boolean Started = false;
    private static final BukkitScheduler scheduler = Bukkit.getScheduler();

    public static void startPaintball (ArrayList<UUID> input) {
        playerList = input;
        Started = true;
        for (UUID uuid : playerList) {
            Player p = Bukkit.getPlayer(uuid);
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("paintball_message"))));
            Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("paintball_start_command")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("paintball_rule"))));
        }
        // With BukkitScheduler
        scheduler.runTaskTimer(plugin, paintball::gameStatusLoop, 20L * 10L /*<-- the initial delay */, 20L * 5L /*<-- the interval */);
    }

    public static void endPaintball() {
        scheduler.cancelTasks(plugin);
        if (Started) {
            Started = false;
            int newWins = 0;
            int newLoses = 0;
            int newTies = 0;

            for (UUID uuid : playerList) {
                Player p = Bukkit.getPlayer(uuid);
                newWins = newWins + getNewWins(p);
                newLoses = newLoses + getNewLoses(p);
                newTies = newTies + getNewTies(p);
                plugin.data.set(uuid + ".finalPaintballKills", getNewKills(p));
                plugin.data.set(uuid + ".finalPaintballWins", getNewWins(p));
                plugin.data.set(uuid + ".finalPaintballLoses", getNewLoses(p));
                plugin.data.set(uuid + ".finalPaintballTies", getNewTies(p));
                //Save Data
                plugin.saveFile();
            }
            if (newTies > 0) {
                //Both Teams Tied
                for (UUID uuid : playerList) {
                    Player p = Bukkit.getPlayer(uuid);
                    int killCount = plugin.data.getInt(uuid + ".finalPaintballKills");
                    int killPoints = killCount * plugin.getConfig().getInt("paintball_points_per_kills");
                    int oldPoints = plugin.data.getInt(uuid + ".points");
                    plugin.data.set(uuid + ".points", plugin.getConfig().getInt("paintball_tie") + killPoints + oldPoints);
                    Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("paintball_end_command")));
                    Objects.requireNonNull(p).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
                }
            } else if (newWins == newLoses) {
                //Even Teams
                for (UUID uuid : playerList) {
                    Player p = Bukkit.getPlayer(uuid);
                    int killCount = plugin.data.getInt(uuid + ".finalPaintballKills");
                    int killPoints = killCount * plugin.getConfig().getInt("paintball_points_per_kills");
                    int oldPoints = plugin.data.getInt(uuid + ".points");
                    if (plugin.data.getInt(uuid + ".finalPaintballWins") > 0) {
                        plugin.data.set(uuid + ".points", plugin.getConfig().getInt("paintball_win") + killPoints + oldPoints);
                    } else {
                        plugin.data.set(uuid + ".points", killPoints + oldPoints);
                    }
                    Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("paintball_end_command")));
                    Objects.requireNonNull(p).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
                }
            } else if (newWins > newLoses) {
                //Winners had more players
                for (UUID uuid : playerList) {
                    Player p = Bukkit.getPlayer(uuid);
                    int killCount = plugin.data.getInt(uuid + ".finalPaintballKills");
                    int killPoints = killCount * plugin.getConfig().getInt("paintball_points_per_kills");
                    int oldPoints = plugin.data.getInt(uuid + ".points");
                    if (plugin.data.getInt(uuid + ".finalPaintballWins") > 0) {
                        plugin.data.set(uuid + ".points", plugin.getConfig().getInt("paintball_win") + killPoints + oldPoints);
                    } else {
                        plugin.data.set(uuid + ".points", plugin.getConfig().getInt("paintball_lose_disadvantage") + killPoints + oldPoints);
                    }
                    Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("paintball_end_command")));
                    Objects.requireNonNull(p).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
                }
            } else {
                //Winners had fewer players
                for (UUID uuid : playerList) {
                    Player p = Bukkit.getPlayer(uuid);
                    int killCount = plugin.data.getInt(uuid + ".finalPaintballKills");
                    int killPoints = killCount * plugin.getConfig().getInt("paintball_points_per_kills");
                    int oldPoints = plugin.data.getInt(uuid + ".points");
                    if (plugin.data.getInt(uuid + ".finalPaintballWins") > 0) {
                        plugin.data.set(uuid + ".points", plugin.getConfig().getInt("paintball_win") + killPoints + oldPoints);
                    } else {
                        plugin.data.set(uuid + ".points", killPoints + oldPoints);
                    }
                    Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("paintball_end_command")));
                    Objects.requireNonNull(p).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
                }
            }
            //Save Data
            plugin.saveFile();
        }
    }

    public static void gameStatusLoop() {
        Player player = Bukkit.getPlayer(playerList.get(0));
        if (getGameStatus(player).contains("FINISHING")) {
            scheduler.cancelTasks(plugin);
            endPaintball();
        }
    }

    public static String getGameStatus(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%paintball_arena_status_" + Objects.requireNonNull(plugin.getConfig().getString("paintball_arena")).trim() + "%");
    }

    public static int getKills(Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%paintball_kills%"));
    }

    public static int getWins(Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%paintball_wins%"));
    }

    public static int getLoses(Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%paintball_loses%"));
    }

    public static int getTies(Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%paintball_ties%"));
    }

    public static int getNewKills(Player player) {
        int newKills = getKills(player);
        int oldKills = plugin.data.getInt(player.getUniqueId() + ".initialPaintballKills");
        return newKills - oldKills;
    }

    public static int getNewWins(Player player) {
        int newWins = getWins(player);
        int oldWins = plugin.data.getInt(player.getUniqueId() + ".initialPaintballWins");
        return newWins - oldWins;
    }

    public static int getNewLoses(Player player) {
        int newLoses = getLoses(player);
        int oldLoses = plugin.data.getInt(player.getUniqueId() + ".initialPaintballLoses");
        return newLoses - oldLoses;
    }

    public static int getNewTies(Player player) {
        int newTies = getTies(player);
        int oldTies = plugin.data.getInt(player.getUniqueId() + ".initialPaintballTies");
        return newTies - oldTies;
    }
}
