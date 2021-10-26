package com.soradgaming.ueslminigames.managment;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.placeholders.scoreboard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.soradgaming.ueslminigames.managment.bracketManager.playerBracket;

public class gameManager {

    public static final HashMap<String, ArrayList<UUID>> playerList = new HashMap<>();
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;

    public static void Initialise() {
        playerBracket(playerList.get("All"));
    }

    //public static void Start() { dividePlayers(playerList); }

    public static void addAllOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayer("All", player);
        }
    }

    public static ArrayList<UUID> getPlayerList(String key) { return playerList.get(key); }

    public static synchronized boolean removePlayer(@NotNull String mapKey, @NotNull Player player) {
        UUID myItem = player.getUniqueId();
        ArrayList<UUID> itemsList = playerList.get(mapKey);
        if(itemsList.contains(myItem)) {
            itemsList.remove(myItem);
            return true;
        }
        return false;
    }

    public static synchronized boolean addPlayer(@NotNull String mapKey, @NotNull Player player) {
        UUID myItem = player.getUniqueId();
        ArrayList<UUID> itemsList = playerList.get(mapKey);

        // if list does not exist create it
        if(itemsList == null) {
            itemsList = new ArrayList<>();
            itemsList.add(myItem);
            playerList.put(mapKey, itemsList);
            return true;
        } else {
            // add if item is not already in list
            if(!itemsList.contains(myItem)) {
                itemsList.add(myItem);
                return true;
            }
        }
        return false;
    }

    public static boolean finish() {
        List<UUID> players = getPlayerList("All");
        Set<UUID> leadBoardSet = scoreboard.grabData().keySet();
        List<UUID> leadBoard = new ArrayList<>(leadBoardSet);
        String first;
        String second;
        String third;
        if (leadBoard.size() > 2) {
            first = Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(leadBoard.size() - 1))).getName();
            second = Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(leadBoard.size() - 2))).getName();
            third = Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(leadBoard.size() - 3))).getName();
        } else {
            return false;
        }
        //Spectators
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            Objects.requireNonNull(player).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("spectators")));
            String message = PlaceholderAPI.setPlaceholders(player, plugin.getConfig().getString("welcome_message"));
            String sendMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(message));
            Objects.requireNonNull(player).sendMessage(sendMessage);
        }
        //3rd Place
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                String message = PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(third), plugin.getConfig().getString("third_place_message"));
                String sendMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(message));
                Bukkit.getServer().broadcastMessage(sendMessage);
                Objects.requireNonNull(Bukkit.getPlayer(third)).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("third_place")));
            }
        };
        // Run the task on this plugin in X seconds (20 ticks)
        task.runTaskLater(plugin, 20L * plugin.getConfig().getInt("third_timer"));
        //2nd
        BukkitRunnable task1 = new BukkitRunnable() {
            @Override
            public void run() {
                String message = PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(second), plugin.getConfig().getString("second_place_message"));
                String sendMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(message));
                Bukkit.getServer().broadcastMessage(sendMessage);
                Objects.requireNonNull(Bukkit.getPlayer(second)).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("second_place")));
            }
        };
        // Run the task on this plugin in X seconds (20 ticks)
        task1.runTaskLater(plugin, 20L * plugin.getConfig().getInt("second_timer"));
        //3rd
        BukkitRunnable task2 = new BukkitRunnable() {
            @Override
            public void run() {
                String message = PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(first), plugin.getConfig().getString("first_place_message"));
                String sendMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(message));
                Bukkit.getServer().broadcastMessage(sendMessage);
                Objects.requireNonNull(Bukkit.getPlayer(first)).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("first_place")));
            }
        };
        // Run the task on this plugin in X seconds (20 ticks)
        task2.runTaskLater(plugin, 20L * plugin.getConfig().getInt("first_timer"));
        return true;
    }
}
