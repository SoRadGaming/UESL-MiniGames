package com.soradgaming.ueslminigames.placeholders;

import com.soradgaming.ueslminigames.UESLMiniGames;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class placeholder extends PlaceholderExpansion {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;

    @Override
    public String getIdentifier() {
        return "UESL-MiniGames";
    }

    @Override
    public String getAuthor() {
        return "SoRadGaming";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params)  {
        {
            Set<UUID> leadBoardSet = scoreboard.grabData().keySet();
            List<UUID> leadBoard = new ArrayList<>(leadBoardSet);
            if(params.equalsIgnoreCase("points")){
                return String.valueOf(plugin.data.getInt(player.getUniqueId() + ".points"));
            }

            if(params.equalsIgnoreCase("points1")) {
                return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(0))).getName();
            }

            if(params.equalsIgnoreCase("points2")) {
                return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(1))).getName();
            }

            if(params.equalsIgnoreCase("points3")) {
                return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(2))).getName();
            }

            if(params.equalsIgnoreCase("points4")) {
                return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(3))).getName();
            }

            if(params.equalsIgnoreCase("points5")) {
                return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(4))).getName();
            }

            return null; // Placeholder is unknown by the Expansion
        }
    }
}
