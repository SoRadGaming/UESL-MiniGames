package com.soradgaming.ueslminigames.managment;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.soradgaming.ueslminigames.managment.bracketManager.dividePlayers;
import static com.soradgaming.ueslminigames.managment.bracketManager.playerBracket;

public class gameManager {

    public static final HashMap<String, ArrayList<UUID>> playerList = new HashMap<>();

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
}
