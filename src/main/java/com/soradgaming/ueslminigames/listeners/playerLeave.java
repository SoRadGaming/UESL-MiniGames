package com.soradgaming.ueslminigames.listeners;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.managment.gameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class playerLeave implements Listener {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;

    @EventHandler
    public void PlayerQuitEvent(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Set<String> oldKeys = gameManager.playerList.keySet();
        List<String > keys = new ArrayList<>(oldKeys);
        for (String key : keys) {
            gameManager.removePlayer(key, player);
        }
    }
}

