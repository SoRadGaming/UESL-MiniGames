package com.soradgaming.ueslminigames.listeners;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.managment.gameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class playerJoin implements Listener {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        gameManager.addPlayer("All", player);
        if (plugin.startedCommand) {
            player.teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
        }
    }
}
