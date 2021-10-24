package com.soradgaming.ueslminigames.managment;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

import static com.soradgaming.ueslminigames.managment.bracketManager.playerBracket;

public class gameManager {

    private static final ArrayList<UUID> playerList = new ArrayList<>();

    public static void Initialise() { playerBracket(playerList); }

    public static void addAllOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!playerList.contains(player.getUniqueId())) {
                playerList.add(player.getUniqueId());
            }
        }
    }

    public static ArrayList<UUID> getPlayerList() { return playerList; }

    public static boolean removePlayer(@NotNull Player player) {
        if (playerList.contains(player.getUniqueId())) {
            playerList.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    public static boolean addPlayer(@NotNull Player player) {
        if (!playerList.contains(player.getUniqueId())) {
            playerList.add(player.getUniqueId());
            return true;
        }
        return false;
    }

}
