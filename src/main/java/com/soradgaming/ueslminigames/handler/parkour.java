package com.soradgaming.ueslminigames.handler;

import com.soradgaming.ueslminigames.UESLMiniGames;
import io.github.a5h73y.parkour.event.PlayerAchieveCheckpointEvent;
import io.github.a5h73y.parkour.event.PlayerFinishCourseEvent;
import io.github.a5h73y.parkour.event.PlayerJoinCourseEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class parkour implements Listener {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;
    private static ArrayList<UUID> playerList;
    private static final ArrayList<UUID> playerListParkour = new ArrayList<>();
    private static boolean Started = false;
    private static final BukkitScheduler scheduler = Bukkit.getScheduler();

    public static void startParkour(ArrayList<UUID> input) {
        playerList = input;
        Started = true;
        for (UUID uuid : playerList) {
            Player p = Bukkit.getPlayer(uuid);
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("parkour_message"))));
            Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("parkour_start_command")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("parkour_rule"))));
        }
        // With BukkitScheduler
        scheduler.runTaskLater(plugin, parkour::endParkour, 20L * plugin.getConfig().getInt("parkour_timer"));
    }

    public static void endParkour() {
        scheduler.cancelTasks(plugin);
        if (Started) {
            Started = false;
            for (UUID value : playerList) {
                Player player = Bukkit.getPlayer(value);
                int checkpoints = plugin.data.getInt(value + ".parkourCheckPoint");
                int newPoints = checkpoints * plugin.getConfig().getInt("parkour_points_per_checkpoint");
                int oldPoints = plugin.data.getInt(value + ".points");
                plugin.data.set(value + ".points", newPoints + oldPoints);
                //Save Data
                plugin.saveFile();
                Objects.requireNonNull(player).performCommand(Objects.requireNonNull(plugin.getConfig().getString("parkour_end_command")));
                player.teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            }
            playerListParkour.clear();
        }
    }

    @EventHandler
    public void PlayerJoinCourseEvent(@NotNull PlayerJoinCourseEvent event) {
        Player player = event.getPlayer();
        if (Started && event.getCourseName().equals(plugin.getConfig().getString("parkour_arena"))) {
            playerListParkour.add(player.getUniqueId());
        }
    }
    @EventHandler
    public void PlayerAchieveCheckpointEvent(@NotNull PlayerAchieveCheckpointEvent event) {
        Player player = event.getPlayer();
        if (Started && event.getCourseName().equals(plugin.getConfig().getString("parkour_arena"))) {
            int oldPoints = plugin.data.getInt(player.getUniqueId() + ".parkourCheckPoint");
            plugin.data.set(player.getUniqueId() + ".parkourCheckPoint", 1 + oldPoints);
        }
    }
    @EventHandler
    public void PlayerFinishCourseEvent(@NotNull PlayerFinishCourseEvent event) {
        Player player = event.getPlayer();
        if (Started && event.getCourseName().equals(plugin.getConfig().getString("parkour_arena"))) {
            if (playerListParkour.size() == playerList.size()) {
                playerListParkour.remove(player.getUniqueId());
                //First Place
                int oldPoints = plugin.data.getInt(player.getUniqueId() + ".points");
                plugin.data.set(player.getUniqueId() + ".points", plugin.getConfig().getInt("parkour_first") + oldPoints);
                plugin.saveFile();
                player.teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            } else if (1 + playerListParkour.size() == playerList.size()) {
                playerListParkour.remove(player.getUniqueId());
                //Second Place
                int oldPoints = plugin.data.getInt(player.getUniqueId() + ".points");
                plugin.data.set(player.getUniqueId() + ".points", plugin.getConfig().getInt("parkour_second") + oldPoints);
                plugin.saveFile();
                player.teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            } else if (2 + playerListParkour.size() == playerList.size()) {
                playerListParkour.remove(player.getUniqueId());
                //Third Place
                int oldPoints = plugin.data.getInt(player.getUniqueId() + ".points");
                plugin.data.set(player.getUniqueId() + ".points", plugin.getConfig().getInt("parkour_third") + oldPoints);
                plugin.saveFile();
                player.teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            } else {
                playerListParkour.remove(player.getUniqueId());
                //Finished
                int oldPoints = plugin.data.getInt(player.getUniqueId() + ".points");
                plugin.data.set(player.getUniqueId() + ".points", plugin.getConfig().getInt("parkour_finish") + oldPoints);
                plugin.saveFile();
                player.teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            }
        }
    }
}
