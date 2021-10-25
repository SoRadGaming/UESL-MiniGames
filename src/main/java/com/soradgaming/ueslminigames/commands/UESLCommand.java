package com.soradgaming.ueslminigames.commands;

import com.soradgaming.ueslminigames.UESLMiniGames;
import com.soradgaming.ueslminigames.managment.gameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.soradgaming.ueslminigames.handler.bedwars.endBedwars;
import static com.soradgaming.ueslminigames.handler.bedwars.startBedwars;
import static com.soradgaming.ueslminigames.handler.buildbattle.endBuildBattle;
import static com.soradgaming.ueslminigames.handler.buildbattle.startBuildBattle;
import static com.soradgaming.ueslminigames.handler.hungergames.endHungerGames;
import static com.soradgaming.ueslminigames.handler.hungergames.startHungerGames;
import static com.soradgaming.ueslminigames.handler.paintball.endPaintball;
import static com.soradgaming.ueslminigames.handler.paintball.startPaintball;
import static com.soradgaming.ueslminigames.handler.parkour.endParkour;
import static com.soradgaming.ueslminigames.handler.parkour.startParkour;
import static com.soradgaming.ueslminigames.handler.spleef.endSpleef;
import static com.soradgaming.ueslminigames.handler.spleef.startSpleef;
import static com.soradgaming.ueslminigames.handler.tntrunX.endTNTRun;
import static com.soradgaming.ueslminigames.handler.tntrunX.startTNTRun;

public class UESLCommand implements CommandExecutor {

    private final UESLMiniGames plugin;

    public UESLCommand() {
        plugin = UESLMiniGames.plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        Player ploc = (Player) sender;
        Location loc = ploc.getLocation();
        if (args.length == 0) {
            sender.sendMessage(ChatColor.BLUE + "=============={" + ChatColor.GREEN + "UESL-MiniGames" + ChatColor.BLUE + "}==============");
            sender.sendMessage(ChatColor.BLUE + "Plugin developed by:" + ChatColor.GREEN + " SoRadGaming");
            sender.sendMessage(ChatColor.BLUE + "Version: " + ChatColor.GREEN + String.format("%s", plugin.getDescription().getVersion()));
            sender.sendMessage(ChatColor.BLUE + "Plugin:" + ChatColor.GREEN + " https://github.com/SoRadGaming/UESL-MiniGames");
            sender.sendMessage(ChatColor.BLUE + "Do " + ChatColor.GREEN + "/umg help " + ChatColor.BLUE + "for the list of commands!");
            sender.sendMessage(ChatColor.BLUE + "=============={" + ChatColor.GREEN + "UESL-MiniGames" + ChatColor.BLUE + "}==============");

        } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
            sender.sendMessage(ChatColor.BLUE + "-----------------=[" + ChatColor.GREEN + "UESL-MiniGames" + ChatColor.BLUE + "]=-----------------");
            sender.sendMessage(ChatColor.GREEN + "/umg help" + ChatColor.BLUE + "  The help command.");
            sender.sendMessage(ChatColor.GREEN + "/umg reload" + ChatColor.BLUE + "  To reload the plugin");
            sender.sendMessage(ChatColor.GREEN + "/umg add player group" + ChatColor.BLUE + " Add player to Data Base");
            sender.sendMessage(ChatColor.GREEN + "/umg remove player group" + ChatColor.BLUE + " Remove player to Data Base");
            sender.sendMessage(ChatColor.GREEN + "/umg addall" + ChatColor.BLUE + " Add All Online players to Universal Group All");
            sender.sendMessage(ChatColor.GREEN + "/umg list" + ChatColor.BLUE + " See all players in Data Base");
            sender.sendMessage(ChatColor.GREEN + "/umg Initialise" + ChatColor.BLUE + " Start Data Base Creation " + ChatColor.RED + "REQUIRED");
            sender.sendMessage(ChatColor.GREEN + "/umg start" + ChatColor.BLUE + " Start the Plugin " + ChatColor.RED + "REQUIRED");
            sender.sendMessage(ChatColor.GREEN + "/umg start minigames group" + ChatColor.BLUE + " start a MiniGame with certain group of players");
            sender.sendMessage(ChatColor.GREEN + "/umg end minigames" + ChatColor.BLUE + " End test MiniGame");
            sender.sendMessage(ChatColor.GREEN + "/umg set location" + ChatColor.BLUE + " Set Location Values " + ChatColor.RED + "REQUIRED");
            sender.sendMessage(ChatColor.GREEN + "Plugin made by: " + ChatColor.BLUE + "SoRadGaming");
            sender.sendMessage(ChatColor.BLUE + "---------------------------------------------------");

        } else if (args.length == 1 && args[0].equalsIgnoreCase("addall")) {
            if (sender.isOp()) {
                gameManager.addAllOnlinePlayers();
                sender.sendMessage(ChatColor.GREEN + "Added all Players");
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }  else if (args.length == 1 && args[0].equalsIgnoreCase("finish")) {
            if (sender.isOp()) {
                plugin.startedCommand = false;
                endTNTRun();
                endSpleef();
                endPaintball();
                endBedwars();
                endParkour();
                endHungerGames();
                endBuildBattle();
                gameManager.finish();
                sender.sendMessage(ChatColor.GREEN + "Starting Finish Event");
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }  else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.isOp()) {
                plugin.reloadConfig();
                plugin.getLogger().info("Reloaded");
                sender.sendMessage(ChatColor.GREEN + "Reloaded");
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }   else if (args.length == 1 && args[0].equalsIgnoreCase("Initialise")) {
            if (sender.isOp()) {
                gameManager.Initialise();
                sender.sendMessage(ChatColor.GREEN + "All Players Initialised");
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }   else if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
            if (sender.isOp()) {
                plugin.startedCommand = true;
                //Start();
                sender.sendMessage(ChatColor.GREEN + "Started");
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (sender.isOp()) {
                List<UUID> playerList = gameManager.getPlayerList("All");
                for (UUID uuid : playerList) {
                    sender.sendMessage(ChatColor.BLUE + Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }  else if (args.length == 2 && args[0].equalsIgnoreCase("list")) {
            if (sender.isOp()) {
                List<UUID> playerList = gameManager.getPlayerList(args[1]);
                for (UUID uuid : playerList) {
                    sender.sendMessage(ChatColor.BLUE + Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
            if (sender.isOp()) {
                Player player = Bukkit.getServer().getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player can not be null!");
                    return true;
                }
                if (gameManager.addPlayer(args[2], player)) {
                    sender.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.GREEN + " Added");
                    return true;
                }
                sender.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.RED + " Already Added");
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("remove")) {
            if (sender.isOp()) {
                Player player = Bukkit.getServer().getPlayer(args[1]);

                if (player == null) {
                        sender.sendMessage(ChatColor.RED + "Player can not be null!");
                        return true;
                    }
                if (gameManager.removePlayer(args[2], player)) {
                    sender.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.GREEN + " Removed");
                    return true;
                }
                sender.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.RED + " Not in List Already");
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }  else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            if (sender.isOp()) {
                if (Objects.equals(args[1], "lobby")) {
                    plugin.getConfig().set("Lobby",loc);
                    sender.sendMessage(ChatColor.GREEN + "Lobby Set to " + loc);
                } else if (Objects.equals(args[1], "first")) {
                    plugin.getConfig().set("first_place",loc);
                    sender.sendMessage(ChatColor.GREEN + "First Place Location Set to " + loc);
                }  else if (Objects.equals(args[1], "second")) {
                    plugin.getConfig().set("second_place",loc);
                    sender.sendMessage(ChatColor.GREEN + "Second Place Location Set to " + loc);
                }  else if (Objects.equals(args[1], "third")) {
                    plugin.getConfig().set("third_place",loc);
                    sender.sendMessage(ChatColor.GREEN + "Third Place Location Set to " + loc);
                }  else if (Objects.equals(args[1], "spectator")) {
                    plugin.getConfig().set("spectators",loc);
                    sender.sendMessage(ChatColor.GREEN + "Spectator Location Set to " + loc);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }   else if (args.length == 3 && args[0].equalsIgnoreCase("start")) {
            if (sender.isOp()) {
                if (Objects.equals(args[1], "bedwars")) {
                    startBedwars(gameManager.getPlayerList(args[2]));
                    sender.sendMessage(ChatColor.GREEN + "Started BedWars");
                } else if (Objects.equals(args[1], "buildbattle")) {
                    startBuildBattle(gameManager.getPlayerList(args[2]));
                    sender.sendMessage(ChatColor.GREEN + "Started BuildBattle");
                }  else if (Objects.equals(args[1], "hungergames")) {
                    startHungerGames(gameManager.getPlayerList(args[2]));
                    sender.sendMessage(ChatColor.GREEN + "Started HungerGames");
                }  else if (Objects.equals(args[1], "paintball")) {
                    startPaintball(gameManager.getPlayerList(args[2]));
                    sender.sendMessage(ChatColor.GREEN + "Started PaintBall");
                }  else if (Objects.equals(args[1], "parkour")) {
                    startParkour(gameManager.getPlayerList(args[2]));
                    sender.sendMessage(ChatColor.GREEN + "Started Parkour");
                }  else if (Objects.equals(args[1], "spleef")) {
                    startSpleef(gameManager.getPlayerList(args[2]));
                    sender.sendMessage(ChatColor.GREEN + "Started Spleef");
                }  else if (Objects.equals(args[1], "tntrun")) {
                    startTNTRun(gameManager.getPlayerList(args[2]));
                    sender.sendMessage(ChatColor.GREEN + "Started TNTRun");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }   else if (args.length == 2 && args[0].equalsIgnoreCase("end")) {
            if (sender.isOp()) {
                if (Objects.equals(args[1], "bedwars")) {
                    endBedwars();
                    sender.sendMessage(ChatColor.GREEN + "Ended BedWars");
                } else if (Objects.equals(args[1], "buildbattle")) {
                    endBuildBattle();
                    sender.sendMessage(ChatColor.GREEN + "Ended BuildBattle");
                }  else if (Objects.equals(args[1], "hungergames")) {
                    endHungerGames();
                    sender.sendMessage(ChatColor.GREEN + "Ended HungerGames");
                }  else if (Objects.equals(args[1], "paintball")) {
                    endPaintball();
                    sender.sendMessage(ChatColor.GREEN + "Ended PaintBall");
                }  else if (Objects.equals(args[1], "parkour")) {
                    endParkour();
                    sender.sendMessage(ChatColor.GREEN + "Ended Parkour");
                }  else if (Objects.equals(args[1], "spleef")) {
                    endSpleef();
                    sender.sendMessage(ChatColor.GREEN + "Ended Spleef");
                }  else if (Objects.equals(args[1], "tntrun")) {
                    endTNTRun();
                    sender.sendMessage(ChatColor.GREEN + "Ended TNTRun");
                }   else if (Objects.equals(args[1], "all")) {
                    plugin.startedCommand = false;
                    endTNTRun();
                    endSpleef();
                    endPaintball();
                    endBedwars();
                    endParkour();
                    endHungerGames();
                    endBuildBattle();
                    sender.sendMessage(ChatColor.GREEN + "Stopped All");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return true;
            }
        }
        return false;
    }
}
