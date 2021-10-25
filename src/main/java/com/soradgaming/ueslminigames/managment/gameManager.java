package com.soradgaming.ueslminigames.managment;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.placeholders.scoreboard;
import org.bukkit.Bukkit;
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

    public static void finish() {
        List<UUID> players = getPlayerList("All");
        Set<UUID> leadBoardSet = scoreboard.grabData().keySet();
        List<UUID> leadBoard = new ArrayList<>(leadBoardSet);
        String first = Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(0))).getName();
        String second = Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(1))).getName();
        String third = Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(2))).getName();
        //Spectators
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            Objects.requireNonNull(player).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("spectators")));
            Objects.requireNonNull(player).sendMessage( "Welcome to the Finishing Ceremony " + player.getName() + " !");
        }
        //3rd Place
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().broadcastMessage("3rd place with " + (plugin.data.getInt(Objects.requireNonNull(Bukkit.getPlayer(third)).getUniqueId() + ".points")) + " points is " + third);
                Objects.requireNonNull(Bukkit.getPlayer(third)).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("third_place")));
            }
        };
        // Run the task on this plugin in X seconds (20 ticks)
        task.runTaskLater(plugin, 20 * 5);
        //2nd
        BukkitRunnable task1 = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().broadcastMessage("2nd place with " + (plugin.data.getInt(Objects.requireNonNull(Bukkit.getPlayer(second)).getUniqueId() + ".points")) + " points is " + second);
                Objects.requireNonNull(Bukkit.getPlayer(second)).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("second_place")));
            }
        };
        // Run the task on this plugin in X seconds (20 ticks)
        task1.runTaskLater(plugin, 20 * 11);
        //3rd
        BukkitRunnable task2 = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().broadcastMessage("Finally with " + (plugin.data.getInt(Objects.requireNonNull(Bukkit.getPlayer(first)).getUniqueId() + ".points")) + "points is " + first);
                Objects.requireNonNull(Bukkit.getPlayer(first)).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("second_place")));
            }
        };
        // Run the task on this plugin in X seconds (20 ticks)
        task2.runTaskLater(plugin, 20 * 20);
    }
}
