package com.soradgaming.ueslminigames.managment;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.handler.bedwars;
import com.soradgaming.ueslminigames.handler.buildbattle;
import com.soradgaming.ueslminigames.handler.paintball;
import com.soradgaming.ueslminigames.handler.spleef;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.soradgaming.ueslminigames.handler.hungergames.startHungerGames;

public class bracketManager implements Listener {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;
    private static ArrayList<Integer> TeamCount = new ArrayList<>();

    public static void dividePlayers(@NotNull ArrayList<UUID> input) {
        int playerCount = input.size();
        int paintballCount = plugin.getConfig().getInt("paintball_max_player_count");
        int spleefCount = plugin.getConfig().getInt("spleef_max_player_count");
        int tntrunCount = plugin.getConfig().getInt("tntrun_max_player_count");
        int parkourCount = plugin.getConfig().getInt("parkour_max_player_count");
        int buildbattleCount = plugin.getConfig().getInt("buildbattle_max_player_count");
        int bedwarsCount = plugin.getConfig().getInt("bedwars_max_player_count");
        int hungergamesCount = plugin.getConfig().getInt("hungergames_max_player_count");

        for (int i = 2; (double) playerCount > i; i++) {
            double y = (double) playerCount / i;
            if( y % 1 == 0) {
                //isWhole = true;
                TeamCount.add((int) y);
            }
        }
        /*
        for (int i = 0; TeamCount.size() > i; i++) {
            for (int x = 0; TeamCount.get(i) > x; x++) {
                team1.remove(input.get(x));
                team2.add(input.get(x));
            }
        }
        for (int i = 0; TeamCount.size() > i; i++) {
            if (hungergamesCount > TeamCount.get(i)) {
                startHungerGames(team1);
            }
        }
         */
    }

    //Start command
    public static void playerBracket(@NotNull ArrayList<UUID> input) {
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
            //HungerGames Kills
            plugin.data.set(uuid + ".hungergamesKills", 0);
            //Save Data
            plugin.saveFile();
        }
    }
}
