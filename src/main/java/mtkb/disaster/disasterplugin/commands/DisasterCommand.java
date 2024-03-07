package mtkb.disaster.disasterplugin.commands;

import mtkb.disaster.disasterplugin.DisasterManager;
import mtkb.disaster.disasterplugin.DisasterPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisasterCommand implements CommandExecutor, TabCompleter {

    private final DisasterPlugin plugin;

    public DisasterCommand(DisasterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1) {
            if (args[0].equalsIgnoreCase("enable")) {
                // Enable disaster plugin here
                if (plugin.isDisasterEnabled()) {
                    sender.sendMessage(Component.text("Disaster plugin is already enabled"));
                }
                else {
                    if(args.length >=2) {
                        try {
                            double newTime = Double.parseDouble(args[1]);
                            if(newTime<=0) {
                                sender.sendMessage(Component.text("§cInvalid time value. Please provide a valid number."));
                                return false;
                            }
                            else {
                                plugin.setTime(newTime);
                                Bukkit.getServer().sendMessage(Component.text("Disaster timer set to " + newTime + " minutes. A random disaster will occur every " + newTime + " minutes."));
                            }
                        }
                        catch (NumberFormatException e) {
                            sender.sendMessage(Component.text("§cInvalid time value. Please provide a valid number."));
                            return false;
                        }
                    }

                    if(args.length >=3) {
                        try {
                            int newCountdown = Integer.parseInt(args[2]);
                            if(newCountdown<0) {
                                sender.sendMessage(Component.text("§cInvalid countdown value. Please provide a valid number."));
                                return false;
                            }
                            else {
                                plugin.setCountdown(newCountdown);
                                Bukkit.getServer().sendMessage(Component.text("Disaster countdown set to " + newCountdown + " seconds. Disaster timer will start in " + newCountdown + " seconds."));
                            }
                        }
                        catch (NumberFormatException e){
                            sender.sendMessage(Component.text("§cInvalid countdown value. Please provide a valid number."));
                            return false;
                        }
                    }
                    Bukkit.getServer().sendMessage(Component.text("Disaster plugin enabled."));
                    plugin.setDisasterEnabled(true);
                    plugin.startDisasterCycle();
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("disable")) {
                if (plugin.isDisasterEnabled()) {
                    plugin.setDisasterEnabled(false);
                    Bukkit.getServer().sendMessage(Component.text("Disaster plugin disabled"));
                    plugin.stopDisasterCycle();
                } else {
                    sender.sendMessage(Component.text("Disaster plugin is already disabled."));
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("force")) {
                if (args.length == 1) {
                    DisasterManager.performDisaster();
                }
                if (args.length >= 2) {
                    String disasterType = args[1].toLowerCase();
                    switch (disasterType) {
                        case "potion":
                            if (args.length >= 3) {
                                int effectId = getEffectId(args[2].toLowerCase());
                                DisasterManager.forceDisaster(Arrays.asList(0, effectId));
                            } else {
                                DisasterManager.forceDisaster(Arrays.asList(0));
                            }
                            break;
                        case "teleport":
                            DisasterManager.forceDisaster(Arrays.asList(1));
                            break;
                        case "raid":
                            DisasterManager.forceDisaster(Arrays.asList(2));
                            break;
                        case "delete":
                            DisasterManager.forceDisaster(Arrays.asList(3));
                            break;
                        default:
                            sender.sendMessage(Component.text("§cDisaster type not recognized."));
                            break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static int getEffectId(String effectName) {
        switch (effectName) {
            // Blindness 0
            // Darkness 1
            // Hunger 2
            // Levitation 3
            // Mining fatigue 4
            // Nausea 5
            // Poison 6
            // Slowness 7
            // Weakness 8
            // Wither 9
            case "blindness":
                return 0;
            case "darkness":
                return 1;
            case "hunger":
                return 2;
            case "levitation":
                return 3;
            case "fatigue":
                return 4;
            case "nausea":
                return 5;
            case "poison":
                return 6;
            case "slowness":
                return 7;
            case "weakness":
                return 8;
            case "wither":
                return 9;
            default:
                //Bukkit.broadcastMessage("Unknown effect ID");
                return 10;
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String [] args) {
        List<String> completions = new ArrayList<>();

        if (args.length==1){
            completions.add("enable");
            completions.add("disable");
            completions.add("force");
        }
        else if (args.length==2) {
            if (args[0].equalsIgnoreCase("enable")) {
                completions.add("5"); //Time between disasters
            }
            else if (args[0].equalsIgnoreCase("force")) {
                completions.add("potion");
                completions.add("teleport");
                completions.add("raid");
                completions.add("delete");
            }
        }
        else if (args.length==3) {
            if (args[0].equalsIgnoreCase("enable")) {
                completions.add("10"); //Countdown time
            }
            else if (args[0].equalsIgnoreCase("force") && args[1].equalsIgnoreCase("potion")){
                completions.add("blindness");
                completions.add("darkness");
                completions.add("hunger");
                completions.add("levitation");
                completions.add("fatigue");
                completions.add("nausea");
                completions.add("poison");
                completions.add("slowness");
                completions.add("weakness");
                completions.add("wither");
            }
        }
        return completions;
    }
}
