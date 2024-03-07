package mtkb.disaster.disasterplugin;

import mtkb.disaster.disasterplugin.commands.DisasterCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.atomic.AtomicInteger;

public final class DisasterPlugin extends JavaPlugin {

    private boolean enabled = false; //Default is disabled
    private double time = 5.0;
    private int countdown = 10;
    private int taskId; //To store the ID of the scheduled task
    private DisasterManager disasterManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Disaster Plugin has started...");

        getCommand("disaster").setExecutor(new DisasterCommand(this));
        getCommand("disaster").setTabCompleter(new DisasterCommand(this));
        BukkitScheduler scheduler = getServer().getScheduler();
        disasterManager = new DisasterManager(scheduler, time);
    }

    public boolean isDisasterEnabled() {
        return enabled;
    }

    public void setDisasterEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
        disasterManager.setTime(time);
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public void startDisasterCycle() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        AtomicInteger countdownSeconds = new AtomicInteger(getCountdown());
        taskId = scheduler.scheduleSyncRepeatingTask(this, () -> {
            int remainingCount = countdownSeconds.decrementAndGet()+1;
            if (remainingCount > 0) {
                Bukkit.getServer().sendMessage(Component.text("Disaster cycle starting in "+remainingCount+ " seconds..."));
            }
            else {
                Bukkit.getServer().sendMessage(Component.text("Disaster cycle starting..."));
                Bukkit.getServer().sendMessage(Component.text("Random disaster will occur in "+getTime()+" minutes."));
                scheduler.cancelTask(taskId); // Cancel countdown task
                taskId = scheduler.runTaskTimer(this, this::performRandomDisaster, (long) (time*1200), (long) (time*1200)).getTaskId();
            }
        }, 0L, 20L);
    }

    public void stopDisasterCycle() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public void performRandomDisaster() {
        // Logic for random disasters
        Bukkit.getServer().sendMessage(Component.text("RANDOM DISASTER"));
        DisasterManager.performDisaster();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Disaster Plugin has shut down...");
    }
}
