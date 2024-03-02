package mtkb.disaster.disasterplugin.commands;

import mtkb.disaster.disasterplugin.DisasterPlugin;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class DisasterCommand implements CommandExecutor, TabCompleter {

    private final DisasterPlugin plugin;

    public DisasterCommand(DisasterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //sender.sendMessage("We in here");
            if(args[0].equalsIgnoreCase("enable")) {
                //sender.sendMessage("We in enabled");
                // Enable disaster plugin here
                if (plugin.isDisasterEnabled()) {
                    sender.sendMessage("Disaster plugin is already enabled.");
                }
                else {
                    plugin.setDisasterEnabled(true);
                    Bukkit.broadcast(new TextComponent("Disaster plugin enabled."));
                    plugin.startDisasterCycle();
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("disable")) {
                //sender.sendMessage("We in disabled");
                if(plugin.isDisasterEnabled()){
                    plugin.setDisasterEnabled(false);
                    Bukkit.broadcast(new TextComponent("Disaster plugin disabled"));
                    plugin.stopDisasterCycle();
                }
                else {
                    sender.sendMessage("Disaster plugin is already disabled.");
                }
                return true;
            }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String [] args) {
        List<String> completions = new ArrayList<>();

        if (args.length==1){
            completions.add("enable");
            completions.add("disable");
        }
        return completions;
    }
}
