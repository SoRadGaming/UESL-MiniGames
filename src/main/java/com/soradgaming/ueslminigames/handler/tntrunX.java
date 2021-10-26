package com.soradgaming.ueslminigames.handler;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.managment.gameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import tntrun.events.PlayerSpectateArenaEvent;
import tntrun.events.PlayerWinArenaEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class tntrunX implements Listener {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;
    private static ArrayList<UUID> playerList;
    private static List<UUID> playerListTNTRun;
    private static boolean Started = false;

    public static void startTNTRun(ArrayList<UUID> input) {
        playerList = input;
        playerListTNTRun = input;
        Started = true;
        for (UUID uuid : playerList) {
            Player p = Bukkit.getPlayer(uuid);
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("tntrun_message"))));
            Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("tntrun_start_command")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("tntrun_rule"))));
        }
    }

    public static void endTNTRun() {
        if (Started) {
            Started = false;
            for (UUID uuid : playerList) {
                Player player = Bukkit.getPlayer(uuid);
                Objects.requireNonNull(player).performCommand(Objects.requireNonNull(plugin.getConfig().getString("tntrun_end_command")));
                Objects.requireNonNull(player).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            }
            gameManager.addAllOnlinePlayers();
        }
    }

    @EventHandler
    public void PlayerSpectateArenaEvent(@NotNull PlayerSpectateArenaEvent event) {
        Player player = event.getPlayer();
        if (Started) {
            playerListTNTRun.remove(player.getUniqueId());
            if (playerListTNTRun.size() == 2) {
                //Third Place
                int oldPoints = plugin.data.getInt(player.getUniqueId() + ".points");
                plugin.data.set(player.getUniqueId() + ".points", plugin.getConfig().getInt("tntrun_third") + oldPoints);
                plugin.saveFile();
            } else if (playerListTNTRun.size() == 1) {
                //Second Place
                int oldPoints = plugin.data.getInt(player.getUniqueId() + ".points");
                plugin.data.set(player.getUniqueId() + ".points", plugin.getConfig().getInt("tntrun_second") + oldPoints);
                plugin.saveFile();
            }
        }
    }

    @EventHandler
    public void PlayerWinArenaEvent(PlayerWinArenaEvent event) {
        if (Started) {
            Player p = event.getPlayer();
            int oldPoints = plugin.data.getInt(p.getUniqueId() + ".points");
            plugin.data.set(p.getUniqueId() + ".points", plugin.getConfig().getInt("tntrun_first") + oldPoints);
            plugin.saveFile();
            endTNTRun();
        }
    }
}
