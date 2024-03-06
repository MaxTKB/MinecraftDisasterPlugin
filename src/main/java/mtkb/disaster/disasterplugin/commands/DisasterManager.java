package mtkb.disaster.disasterplugin.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DisasterManager {
    private static Random random;
    private final BukkitScheduler scheduler;
    private static Double time;
    private static List<Runnable> disasterList;

    public DisasterManager(BukkitScheduler scheduler, Double time) {
        random = new Random();
        this.scheduler = scheduler;
        DisasterManager.time = time;
        disasterList = new ArrayList<>();
        //disasterList.add(this::testDisaster); How to add disasters to list
        //disasterList.add(()-> exampleDisaster(x)); How to add disaster which takes a parameter
        disasterList.add(()-> potionEffectDisaster(-1));
        disasterList.add(DisasterManager::teleportDisaster);
        disasterList.add(DisasterManager::raidDisaster);
    }

    public void setTime(double time){
        DisasterManager.time = time;
    }

    public static void performDisaster() {
        int randomNumber = random.nextInt(disasterList.size());
        disasterList.get(randomNumber).run();
    }

    public static void forceDisaster(List<Integer> disasterParams) {
        if (disasterParams.isEmpty()) {
            return;
        }
        int disasterIndex = disasterParams.get(0);
        if (disasterParams.size()>=2) {
            int effectId = disasterParams.get(1);
            potionEffectDisaster(effectId);
        }
        else {
            disasterList.get(disasterIndex).run();
        }
    }

    private static void potionEffectDisaster(int givenId) {

        // List of chosen potion effects and their Id
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

        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.broadcast(new TextComponent("§aDISASTER: RANDOM POTION EFFECT"));
            int duration = 15 * 20;
            int effectId = random.nextInt(10);
            if (givenId != -1) {
                effectId = givenId;
            }

            PotionEffect potionEffect = null;

            switch (effectId) {
                case 0:
                    potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, duration, 0);
                    break;
                case 1:
                    potionEffect = new PotionEffect(PotionEffectType.DARKNESS, duration, 0);
                    break;
                case 2:
                    potionEffect = new PotionEffect(PotionEffectType.HUNGER, duration, 1);
                    break;
                case 3:
                    potionEffect = new PotionEffect(PotionEffectType.LEVITATION, duration, 0);
                    break;
                case 4:
                    potionEffect = new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, 1);
                    break;
                case 5:
                    potionEffect = new PotionEffect(PotionEffectType.CONFUSION, duration, 0);
                    break;
                case 6:
                    potionEffect = new PotionEffect(PotionEffectType.POISON, duration, 0);
                    break;
                case 7:
                    potionEffect = new PotionEffect(PotionEffectType.SLOW, duration, 1);
                    break;
                case 8:
                    potionEffect = new PotionEffect(PotionEffectType.WEAKNESS, duration, 1);
                    break;
                case 9:
                    potionEffect = new PotionEffect(PotionEffectType.WITHER, duration, 0);
                    break;
                default:
                    Bukkit.broadcast(new TextComponent("§cUnknown effect ID"));
                    break;
            }

            if (potionEffect != null) {
                player.addPotionEffect(potionEffect);
            }
        }
    }

    private static void teleportDisaster() {
        int teleportRadius = 50;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location originalLocation = player.getLocation();
            World world = originalLocation.getWorld();
            Location newLocation = getSpawnableLocation(player, 50, true);
            player.teleport(newLocation);
            Bukkit.broadcastMessage("§aDISASTER: TELEPORT TO RANDOM LOCATION");
        }
    }

    private static void raidDisaster() {
        Bukkit.broadcastMessage("§aDISASTER: MAX LEVEL RAID");
        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();
        if (onlinePlayers.isEmpty()) {
            return;
        }

        //Choose a random player
        Player randomPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));

        //Get the player's location
        Location playerLocation = randomPlayer.getLocation();

        //Get the world
        World world = playerLocation.getWorld();

        //Get world difficulty
        Difficulty difficulty = world.getDifficulty();

        int raidRadius = 15;

        //Find a random spawnable block
        Location raidLocation = getSpawnableLocation(randomPlayer, raidRadius, false);

        switch (difficulty) {
            case PEACEFUL:
                Bukkit.broadcastMessage("§cDifficulty set to peaceful, cannot spawn raid.");
                break;
            case EASY:
                for (int i = 0; i < 4; i++) {
                    world.spawnEntity(raidLocation, EntityType.PILLAGER);
                }
                world.spawnEntity(raidLocation, EntityType.VINDICATOR);
                world.spawnEntity(raidLocation, EntityType.RAVAGER);
                break;
            case NORMAL:
                for (int i = 0; i < 5; i++) {
                    world.spawnEntity(raidLocation, EntityType.PILLAGER);
                    world.spawnEntity(raidLocation, EntityType.VINDICATOR);
                }
                for (int i = 0; i < 2; i++) {
                    world.spawnEntity(raidLocation, EntityType.RAVAGER);
                }
                world.spawnEntity(raidLocation, EntityType.WITCH);
                world.spawnEntity(raidLocation, EntityType.EVOKER);
                break;
            case HARD:
                for (int i = 0; i < 4; i++) {
                    world.spawnEntity(raidLocation, EntityType.PILLAGER);
                }
                for (int i = 0; i < 7; i++) {
                    world.spawnEntity(raidLocation, EntityType.VINDICATOR);
                }
                for (int i = 0; i < 2; i++) {
                    world.spawnEntity(raidLocation, EntityType.WITCH);
                    world.spawnEntity(raidLocation, EntityType.EVOKER);
                    Ravager ravager = (Ravager) world.spawnEntity(raidLocation, EntityType.RAVAGER);
                    Vindicator vindicator = (Vindicator) world.spawnEntity(raidLocation, EntityType.VINDICATOR);
                    ravager.addPassenger(vindicator);
                }
                Ravager ravager = (Ravager) world.spawnEntity(raidLocation, EntityType.RAVAGER);
                Evoker evoker = (Evoker) world.spawnEntity(raidLocation, EntityType.EVOKER);
                ravager.addPassenger(evoker);
                break;
        }
    }

    private static Location getSpawnableLocation(Player player, int radius, boolean allowTop) {
        int count=0;
        int maxAttempts=100;
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        double randX = playerLocation.getX() + random.nextInt(2*radius) - radius;
        double randY = playerLocation.getY() + random.nextInt(2*radius) - radius;
        double randZ = playerLocation.getZ() + random.nextInt(2*radius) - radius;
        Location newLocation = new Location(world, randX, randY, randZ);
        while(!isSpawnable(newLocation) && count<maxAttempts) {
            randX = playerLocation.getX() + random.nextInt(2*radius) - radius;
            randY = playerLocation.getY() + random.nextInt(2*radius) - radius;
            randZ = playerLocation.getZ() + random.nextInt(2*radius) - radius;
            newLocation = new Location(world, randX, randY, randZ);
            count++;
        }
        //Bukkit.broadcastMessage("Location found on try "+count);
        if(!isSpawnable(newLocation) && !allowTop){
            newLocation=playerLocation;
        }
        else if(!isSpawnable(newLocation) && allowTop) {
            Block highestBlock = world.getHighestBlockAt(newLocation);
            while (highestBlock.getType() == Material.AIR) {
                highestBlock = highestBlock.getRelative(0,-1,0);
            }
            newLocation = highestBlock.getLocation().add(0.5,1,0.5);
        }
        return newLocation;
    }

    private static boolean isSpawnable(Location location){
        //The block at the feet location
        Block feetBlock = location.getBlock();
        //The block at the head location
        Block headBlock = feetBlock.getRelative(0,1,0);
        //The block that the mob/player will be standing on
        Block standBlock = feetBlock.getRelative(0,-1,0);

        return(isAir(feetBlock) && isAir(headBlock) && standBlock.isSolid());
    }

    private static boolean isAir(Block block) {
        return(block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR);
    }
}
