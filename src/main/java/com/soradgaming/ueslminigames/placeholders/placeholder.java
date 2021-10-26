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
                if (leadBoard.size() > 0) {
                    return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(leadBoard.size() - 1))).getName();
                } else return null;
            }

            if(params.equalsIgnoreCase("points2")) {
                if (leadBoard.size() > 1) {
                    return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(leadBoard.size() - 2))).getName();
                } else return null;
            }

            if(params.equalsIgnoreCase("points3")) {
                if (leadBoard.size() > 2) {
                    return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(leadBoard.size() - 3))).getName();
                } else return null;
            }

            if(params.equalsIgnoreCase("points4")) {
                if (leadBoard.size() > 3) {
                    return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(leadBoard.size() - 4))).getName();
                } else return null;
            }

            if(params.equalsIgnoreCase("points5")) {
                if (leadBoard.size() > 4) {
                    return Objects.requireNonNull(Bukkit.getPlayer(leadBoard.get(leadBoard.size() - 5))).getName();
                } else return null;
            }

            return null; // Placeholder is unknown by the Expansion
        }
    }
}
