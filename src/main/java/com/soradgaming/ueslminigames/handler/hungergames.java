package com.soradgaming.ueslminigames.handler;

import com.soradgaming.ueslminigames.UESLMiniGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import tk.shanebee.hg.events.GameEndEvent;
import tk.shanebee.hg.events.GameStartEvent;
import tk.shanebee.hg.events.PlayerDeathGameEvent;

import java.util.*;


public class hungergames implements Listener {
    private static final UESLMiniGames plugin = UESLMiniGames.plugin;
    private static ArrayList<UUID> playerList;
    private static ArrayList<UUID> playerListHungerGames;
    private static boolean Started = false;

    public static void startHungerGames(ArrayList<UUID> input) {
        playerList = input;
        Started = true;
        for (UUID uuid : playerList) {
            Player p = Bukkit.getPlayer(uuid);
            Objects.requireNonNull(p).teleport(Objects.requireNonNull(plugin.getConfig().getLocation("Lobby")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("hungergames_message"))));
            Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("hungergames_start_command")));
            Objects.requireNonNull(p).sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("hungergames_rule"))));
        }
    }

    public static void endHungerGames() {
        if (Started) {
            Started = false;
            for (UUID uuid : playerList) {
                Player p = Bukkit.getPlayer(uuid);
                int killCount = plugin.data.getInt(uuid + ".hungergamesKills");
                int killPoints = killCount * plugin.getConfig().getInt("hungergames_points_per_kills");
                int oldPoints = plugin.data.getInt(uuid + ".points");
                plugin.data.set(uuid + ".points", killPoints + oldPoints);
                Objects.requireNonNull(p).performCommand(Objects.requireNonNull(plugin.getConfig().getString("hungergames_end_command")));
            }
            playerListHungerGames = new ArrayList<>();
        }
    }

    @EventHandler
    public void GameStartEvent(@NotNull GameStartEvent event) {
        playerListHungerGames = (ArrayList<UUID>) event.getGame().getGamePlayerData().getPlayers();
    }

    @EventHandler
    public void PlayerDeathGameEvent(PlayerDeathGameEvent event) {
        if (Started) {
        Player player = event.getEntity();

        //Get Killer From Death Message
        String deathMessage = event.getDeathMessage();
        String[] arr = Objects.requireNonNull(deathMessage).split(" ", 9);
        if (arr.length == 9 && !arr[8].equals("Stray!") && !arr[8].equals("Skeleton")) {
            String fourthWordIsKiller = arr[4];
            String KillerFormatFix = fourthWordIsKiller.substring(2);
            Player killer = Bukkit.getServer().getPlayer(KillerFormatFix);
            //HungerGames Data
            assert killer != null;
            int oldKills = plugin.data.getInt(killer.getUniqueId() + ".hungergamesKills");
            plugin.data.set(killer.getUniqueId() + ".hungergamesKills", 1 + oldKills);
        }
        //Get 2nd and 3rd Place
            playerListHungerGames.remove(player.getUniqueId());
            if (playerListHungerGames.size() == 2) {
                //Third Place
                int oldPoints = plugin.data.getInt(player.getUniqueId() + ".points");
                plugin.data.set(player.getUniqueId() + ".points", plugin.getConfig().getInt("hungergames_third") + oldPoints);
                plugin.saveFile();
            } else if (playerListHungerGames.size() == 1) {
                //Second Place
                int oldPoints = plugin.data.getInt(player.getUniqueId() + ".points");
                plugin.data.set(player.getUniqueId() + ".points", plugin.getConfig().getInt("hungergames_second") + oldPoints);
                plugin.saveFile();
            }
        }
    }

    @EventHandler
    public void GameEndEvent(GameEndEvent event) {
        if (Started) {
            Collection<Player> player = event.getWinners();
            Bukkit.getConsoleSender().sendMessage("Winner is " + player );
            Player p = (Player) event.getWinners();
            int oldPoints = plugin.data.getInt(p.getUniqueId() + ".points");
            plugin.data.set(p.getUniqueId() + ".points", plugin.getConfig().getInt("hungergames_first") + oldPoints);
            plugin.saveFile();
            endHungerGames();
        }
    }
}
