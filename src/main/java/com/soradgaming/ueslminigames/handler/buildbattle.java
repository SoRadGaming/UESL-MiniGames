package com.soradgaming.ueslminigames.handler;

import com.soradgaming.ueslminigames.UESLMiniGames;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import plugily.projects.buildbattle.api.event.game.BBGameEndEvent;
import plugily.projects.buildbattle.api.event.game.BBGameStartEvent;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class buildbattle implements Listener {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;
    private static ArrayList<UUID> playerList;
    private static boolean Started = false;

    public static void startBuildBattle(ArrayList<UUID> input) {
        playerList = input;
        Started = true;
        for (UUID uuid : playerList) {
            Player p = Bukkit.getPlayer(uuid);
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("buildbattle_message"))));
            Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("buildbattle_start_command")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("buildbattle_rule"))));
        }
    }

    public static void endBuildBattle() {
        if (Started) {
            Started = false;
            for (UUID uuid : playerList) {
                Player p = Bukkit.getPlayer(uuid);
                int nonScaledPoints = getNewPoints(p);
                int finalPoints = nonScaledPoints * plugin.getConfig().getInt("buildbattle_points_per_point");
                int oldPoints = plugin.data.getInt(uuid + ".points");
                if (getNewWins(p) > 0) {
                    plugin.data.set(uuid + ".points", finalPoints + oldPoints + plugin.getConfig().getInt("buildbattle_first_bonus"));
                } else {
                    plugin.data.set(uuid + ".points", finalPoints + oldPoints);
                }
                plugin.saveFile();
                Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("buildbattle_end_command")));
                Objects.requireNonNull(p).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            }
        }
    }

    @EventHandler
    public void onGameStart(@NotNull BBGameStartEvent event){
        event.getArena().getAllArenaPlayers();
        event.getArena().getPlayers();
    }

    @EventHandler
    public void onGameStop(BBGameEndEvent event){
        if (Started) {
            endBuildBattle();
        }
    }

    public static int getWins(Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%buildbattle_wins%"));
    }

    public static int getPoints(Player player) {
        return Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%buildbattle_total_points_earned%"));
    }

    public static int getNewWins(Player player) {
        int newWins = getWins(player);
        int oldWins = plugin.data.getInt(player.getUniqueId() + ".initialBuildBattleWins");
        return newWins - oldWins;
    }

    public static int getNewPoints(Player player) {
        int newLoses = getPoints(player);
        int oldLoses = plugin.data.getInt(player.getUniqueId() + ".initialBuildBattlePoints");
        return newLoses - oldLoses;
    }
}
