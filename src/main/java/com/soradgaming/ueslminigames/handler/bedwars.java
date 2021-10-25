package com.soradgaming.ueslminigames.handler;

import com.soradgaming.ueslminigames.UESLMiniGames;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.events.BedwarsGameEndEvent;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class bedwars implements Listener {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;
    private static ArrayList<UUID> playerList;
    private static int tiedPlayers;
    private static boolean Started = false;
    private static boolean gameIsPlayed = false;
    private static List<RunningTeam> teams;

    public static void startBedwars(ArrayList<UUID> input) {
        playerList = input;
        tiedPlayers = 0;
        Started = true;
        gameIsPlayed = false;
        for (UUID uuid : playerList) {
            Player p = Bukkit.getPlayer(uuid);
            Objects.requireNonNull(p).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("bedwars_message"))));
            Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("bedwars_start_command")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("bedwars_rule"))));
        }
    }

    public static void endBedwars() {
        if (Started) {
            Started = false;
            for (UUID item : playerList) {
                Player p = Bukkit.getPlayer(item);
                plugin.data.set(item + ".finalBedwarsKills", getNewKills(p));
                plugin.data.set(item + ".finalBedwarsWins", getNewWins(p));
                plugin.data.set(item + ".finalBedwarsLoses", getNewLoses(p));
                plugin.data.set(item + ".finalBedwarsBed", getNewBed(p));
                if (plugin.data.getInt(item + ".finalBedwarsBed") > 0) {
                    for (RunningTeam team : teams) {
                        List<Player> playerInTeam = team.getConnectedPlayers();
                        for (int x = 0; x < playerInTeam.size(); x++) {
                            if (playerInTeam.get(x).getPlayer() == Bukkit.getPlayer(item)) {
                                for (Player player : playerInTeam) {
                                    UUID playerUUID = player.getUniqueId();
                                    plugin.data.set(playerUUID + ".finalBedwarsBed", plugin.data.getInt(item + ".finalBedwarsBed"));
                                    plugin.saveFile();
                                }
                            }
                        }
                    }
                }
                //Save Data
                plugin.saveFile();
            }
            for (UUID value : playerList) {
                Player p = Bukkit.getPlayer(value);
                int killCount = plugin.data.getInt(value + ".finalBedwarsKills");
                int killPoints = killCount * plugin.getConfig().getInt("bedwars_points_per_kills");
                int oldPoints = plugin.data.getInt(value + ".points");
                if (plugin.data.getInt(value + ".finalBedwarsWins") > 0 && gameIsPlayed) {
                    plugin.data.set(value + ".points", plugin.getConfig().getInt("bedwars_first") + killPoints + oldPoints);
                    plugin.saveFile();
                } else if (plugin.data.getInt(value + ".finalBedwarsLoses") == 0 && plugin.data.getInt(value + ".finalBedwarsWins") == 0 && gameIsPlayed) {
                    plugin.data.set(value + ".points", killPoints + oldPoints);
                    plugin.data.set(value + ".finalBedwarsTies", 1);
                    tiedPlayers = tiedPlayers + 1;
                    plugin.saveFile();
                } else {
                    plugin.data.set(value + ".points", killPoints + oldPoints);
                    plugin.saveFile();
                }
                Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("bedwars_end_command")));
            }
            for (UUID uuid : playerList) {
                int oldPoints = plugin.data.getInt(uuid + ".points");
                int bedPoints = (plugin.data.getInt(uuid + ".finalBedwarsBed") * plugin.getConfig().getInt("bedwars_points_per_bed"));
                if (plugin.data.getInt(uuid + ".finalBedwarsTies") > 0) {
                    int tiedPoints = (plugin.getConfig().getInt("bedwars_first") / tiedPlayers);
                    plugin.data.set(uuid + ".points", oldPoints + tiedPoints);
                    plugin.saveFile();
                }
                if (plugin.data.getInt(uuid + ".finalBedwarsBed") > 0) {
                    plugin.data.set(uuid + ".points", oldPoints + bedPoints);
                    plugin.saveFile();
                }
            }
            gameIsPlayed = false;
        }
    }

    @EventHandler
    public void BedwarsGameStartedEvent(BedwarsGameStartedEvent event) {
        if (Started) {
            teams = event.getGame().getRunningTeams();
            gameIsPlayed = true;
        }
    }

    @EventHandler
    public void BedwarsGameEndEvent(BedwarsGameEndEvent event) {
        if (Started) {
            endBedwars();
        }
    }

    public static int getKills(@NotNull Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%bedwars_stats_kills%"));
    }

    public static int getWins(@NotNull Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%bedwars_stats_wins%"));
    }

    public static int getLoses(@NotNull Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%bedwars_stats_loses%"));
    }

    public static int getBed(@NotNull Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%bedwars_stats_destroyed_beds%"));
    }

    public static int getNewKills(Player player) {
        int newKills = getKills(player);
        int oldKills = plugin.data.getInt(player.getUniqueId() + ".initialBedwarsKills");
        return newKills - oldKills;
    }

    public static int getNewWins(Player player) {
        int newWins = getWins(player);
        int oldWins = plugin.data.getInt(player.getUniqueId() + ".initialBedwarsWins");
        return newWins - oldWins;
    }

    public static int getNewLoses(Player player) {
        int newLoses = getLoses(player);
        int oldLoses = plugin.data.getInt(player.getUniqueId() + ".initialBedwarsLoses");
        return newLoses - oldLoses;
    }

    public static int getNewBed(Player player) {
        int newBed = getBed(player);
        int oldBed = plugin.data.getInt(player.getUniqueId() + ".initialBedwarsBed");
        return newBed - oldBed;
    }
}
