package com.soradgaming.ueslminigames.managment;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.handler.bedwars;
import com.soradgaming.ueslminigames.handler.buildbattle;
import com.soradgaming.ueslminigames.handler.paintball;
import com.soradgaming.ueslminigames.handler.spleef;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class bracketManager {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;

    //Start command
    public static void playerBracket(ArrayList<UUID> input) {
        //Active Players

        for (UUID uuid : input) {
            Player p = Bukkit.getPlayer(uuid);
            assert p != null;

            //Data
            plugin.data.set(uuid + ".points", 0);
            //Spleef Data
            plugin.data.set(uuid + ".initialSpleefWins", spleef.getWins(p));
            plugin.data.set(uuid + ".initialSpleefLoses", spleef.getLoses(p));
            plugin.data.set(uuid + ".finalSpleefWins", 0);
            plugin.data.set(uuid + ".finalSpleefLoses", 0);
            //Parkour Data
            plugin.data.set(uuid + ".parkourCheckPoint", 0);
            //BuildBattle Data
            plugin.data.set(uuid + ".initialBuildBattleWins", buildbattle.getWins(p));
            plugin.data.set(uuid + ".initialBuildBattlePoints", buildbattle.getPoints(p));
            //BedWars Data
            plugin.data.set(uuid + ".initialBedwarsWins", bedwars.getWins(p));
            plugin.data.set(uuid + ".initialBedwarsKills", bedwars.getKills(p));
            plugin.data.set(uuid + ".initialBedwarsLoses", bedwars.getLoses(p));
            plugin.data.set(uuid + ".initialBedwarsBed", bedwars.getBed(p));
            plugin.data.set(uuid + ".finalBedwarsKills", 0);
            plugin.data.set(uuid + ".finalBedwarsWins", 0);
            plugin.data.set(uuid + ".finalBedwarsLoses", 0);
            plugin.data.set(uuid + ".finalBedwarsBed", 0);
            //Paintball Data
            plugin.data.set(uuid + ".initialPaintballKills", paintball.getKills(p));
            plugin.data.set(uuid + ".initialPaintballWins", paintball.getWins(p));
            plugin.data.set(uuid + ".initialPaintballLoses", paintball.getLoses(p));
            plugin.data.set(uuid + ".initialPaintballTies", paintball.getTies(p));
            plugin.data.set(uuid + ".finalPaintballKills", 0);
            plugin.data.set(uuid + ".finalPaintballWins", 0);
            plugin.data.set(uuid + ".finalPaintballLoses", 0);
            plugin.data.set(uuid + ".finalPaintballTies", 0);
            //Save Data
            plugin.saveFile();
        }
    }
}
