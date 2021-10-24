package com.soradgaming.ueslminigames.listeners;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.managment.gameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class playerLeave implements Listener {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        if (plugin.startedCommand) {
            Player player = event.getPlayer();
            gameManager.removePlayer(player);
        }
    }
}

