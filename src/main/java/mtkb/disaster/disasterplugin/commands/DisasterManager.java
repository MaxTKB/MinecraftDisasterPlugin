package mtkb.disaster.disasterplugin.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DisasterManager {
    private static Random random;
    private final BukkitScheduler scheduler;
    private static Double time;
    private static List<Runnable> disasterList;

    public DisasterManager(BukkitScheduler scheduler, Double time) {
        this.random = new Random();
        this.scheduler = scheduler;
        this.time = time;
        this.disasterList = new ArrayList<>();
        disasterList.add(this::testDisaster);
    }

    public static void performDisaster() {
        int randomNumber = random.nextInt(disasterList.size());
        disasterList.get(randomNumber).run();
    }

    private void testDisaster() {
        Bukkit.broadcast(new TextComponent("DISASTER AHH"));
    }
}
