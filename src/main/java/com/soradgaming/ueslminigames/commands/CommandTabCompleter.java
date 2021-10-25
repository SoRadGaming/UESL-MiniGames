package com.soradgaming.ueslminigames.commands;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.managment.gameManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandTabCompleter implements TabCompleter {

    private final UESLMiniGames plugin;

    public CommandTabCompleter() {
        plugin = UESLMiniGames.plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if(cmd.getName().equalsIgnoreCase("umg")){
            ArrayList<String> completions = new ArrayList<>();
            if (args.length == 1) {
                completions = new ArrayList<>(Arrays.asList("add", "help", "reload", "remove", "list", "set", "Initialise", "start", "end","addall", "finish"));
                completions = getApplicableTabCompletes(args[0], completions);
            } else if (args.length == 2) {
                switch (args[0]) {
                    case "add":
                    case "remove":
                        completions = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            completions.add(player.getName());
                        }
                        completions = getApplicableTabCompletes(args[1], completions);
                        break;
                    case "set":
                        completions = new ArrayList<>(Arrays.asList("lobby", "first", "second", "third", "spectator"));
                        completions = getApplicableTabCompletes(args[1], completions);
                        break;
                    case "start":
                        completions = new ArrayList<>(Arrays.asList("bedwars", "buildbattle", "hungergames", "paintball", "parkour", "spleef", "tntrun"));
                        completions = getApplicableTabCompletes(args[1], completions);
                        break;
                    case "end":
                        completions = new ArrayList<>(Arrays.asList("bedwars", "buildbattle", "hungergames", "paintball", "parkour", "spleef", "tntrun", "all"));
                        completions = getApplicableTabCompletes(args[1], completions);
                        break;
                    case "list":
                        Set<String> oldKeys = gameManager.playerList.keySet();
                        List<String > keys = new ArrayList<>(oldKeys);
                        completions = new ArrayList<>(keys);
                        completions = getApplicableTabCompletes(args[1], completions);
                        break;
                    default:
                        return null;
                }
            } else if (args.length == 3) {
                if (Objects.equals(args[0], "add") || Objects.equals(args[0], "start") || Objects.equals(args[0], "remove")) {
                    Set<String> oldKeys = gameManager.playerList.keySet();
                    List<String > keys = new ArrayList<>(oldKeys);
                    completions = new ArrayList<>(keys);
                }
                completions = getApplicableTabCompletes(args[2], completions);
            }
            Collections.sort(completions);
            return completions;
        }
        return null;
    }

    public ArrayList<String> getApplicableTabCompletes(String arg, ArrayList<String> completions) {
        if (arg == null || arg.equalsIgnoreCase("")) {
            return completions;
        }
        ArrayList<String> valid = new ArrayList<>();
        for (String possibly : completions) {
            if (possibly.startsWith(arg)) {
                valid.add(possibly);
            }
        }
        return valid;
    }
}
