package mtkb.disaster.disasterplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DisasterManager {
    private static Random random;
    private static BukkitScheduler scheduler;
    private static Double time;
    private static List<Runnable> disasterList;

    private static Plugin plugin;

    public DisasterManager(Plugin plugin, Double time) {
        random = new Random();
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        DisasterManager.time = time;
        disasterList = new ArrayList<>();
        //disasterList.add(this::testDisaster); How to add disasters to list
        //disasterList.add(()-> exampleDisaster(x)); How to add disaster which takes a parameter
        disasterList.add(()-> potionEffectDisaster(-1));
        disasterList.add(DisasterManager::teleportDisaster);
        disasterList.add(DisasterManager::raidDisaster);
        disasterList.add(DisasterManager::deleteItemDisaster);
        disasterList.add(DisasterManager::chargedCreeperDisaster);
        disasterList.add(DisasterManager::ghastDisaster);
        disasterList.add(DisasterManager::healthDisaster);
    }

    public void setTime(double time){
        DisasterManager.time = time;
    }

    public static void performDisaster() {
        int randomNumber = random.nextInt(disasterList.size());
        disasterList.get(randomNumber).run();
    }

    public static void forceDisaster(int disasterId) {
        disasterList.get(disasterId).run();
    }

    public static void forceDisaster(int disasterId, int effectId) {
        if (disasterId==0) {
            potionEffectDisaster(effectId);
        }
        else {
            forceDisaster(disasterId);
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
            Bukkit.getServer().sendMessage(Component.text("§aDISASTER: RANDOM POTION EFFECT"));
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
                    Bukkit.getServer().sendMessage(Component.text("§cUnknown effect ID"));
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
            Bukkit.getServer().sendMessage(Component.text("§aDISASTER: TELEPORT TO RANDOM LOCATION"));
        }
    }

    private static void raidDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: MAX LEVEL RAID"));
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
                Bukkit.getServer().sendMessage(Component.text("§cDifficulty set to peaceful, cannot spawn raid."));
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

    private static void deleteItemDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: DELETING ITEM IN HAND"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().setItemInMainHand(null);
        }
    }

    private static void chargedCreeperDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: SPAWNING CHARGED CREEPER"));
        int radius = 10;
        for (Player player: Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();
            if (world.getDifficulty()==Difficulty.PEACEFUL){
                Bukkit.getServer().sendMessage(Component.text("§cDifficulty set to peaceful, cannot spawn charged creeper."));
                break;
            }
            Location spawnLocation = getSpawnableLocation(player, radius, false);
            Creeper creeper = (Creeper) world.spawnEntity(spawnLocation, EntityType.CREEPER);
            creeper.setPowered(true); // Set creeper to be charged
        }
    }

    // wardenDisaster

    private static void ghastDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: SPAWNING GHAST"));
        int radius = 20;
        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();
        if (onlinePlayers.isEmpty()) {
            return;
        }
        for (Player player : onlinePlayers) {
            World world = player.getWorld();
            if (world.getDifficulty()==Difficulty.PEACEFUL) {
                Bukkit.getServer().sendMessage(Component.text("§cDifficulty set to peaceful, cannot spawn ghast."));
                break;
            }
            Location spawnLocation = getSpawnableGhast(player, radius);
            for (int i = 0; i < 3; i++) {
                world.spawnEntity(spawnLocation, EntityType.GHAST);
            }
        }
    }

    private static void healthDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: MAX HEALTH = 1 HEART"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            double newMaxHealth = 2.0;
            player.setHealth(newMaxHealth);
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);

            if(player.getHealth() > newMaxHealth) {
                player.setHealth(newMaxHealth);
            }

            scheduler.runTaskLater(plugin, () -> restorePlayerHealth(player), (long) (20*time*60));
        }
    }

    public static void restorePlayerHealth(Player player) {
        Bukkit.getServer().sendMessage(Component.text("§aRESTORING HEALTH"));
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
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

    private static Location getSpawnableGhast(Player player, int radius){
        // Make it so that checks around +-2 blocks in every location to ensure that ghast has enough space to spawn
        int count = 0;
        int maxAttempts = 100;
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        double randX = playerLocation.getX() + random.nextInt(2*radius) - radius;
        double randY = playerLocation.getY() + random.nextInt(2*radius) - radius;
        double randZ = playerLocation.getZ() + random.nextInt(2*radius) - radius;
        Location newLocation = new Location(world, randX, randY, randZ);
        while(!isAir(newLocation.getBlock()) && count<maxAttempts && !airSurrouds(newLocation, 4)) {
            randX = playerLocation.getX() + random.nextInt(2*radius) - radius;
            randY = playerLocation.getY() + random.nextInt(2*radius) - radius;
            randZ = playerLocation.getZ() + random.nextInt(2*radius) - radius;
            newLocation = new Location(world, randX, randY, randZ);
            count++;
        }
        if(!isAir(newLocation.getBlock())) {
            Block highestBlock = world.getHighestBlockAt(newLocation);
            while(highestBlock.getType() == Material.AIR) {
                highestBlock = highestBlock.getRelative(0,-1,0);
            }
            newLocation = highestBlock.getLocation().add(0.5,radius,0.5);
        }
        return newLocation;
    }

    private static boolean airSurrouds(Location location, int radius) {
        int locationX = location.getBlockX();
        int locationY = location.getBlockY();
        int locationZ = location.getBlockZ();
        World world = location.getWorld();

        // Radius = 4 in this circumstance
        // Modify to allow checking for specified radius
        for (int x = locationX - 2; x<= locationX + 1; x++){
            for (int y = locationY; y <= locationY +3; y++) {
                for (int z = locationZ - 2; z <= locationZ + 1; z++) {
                    if(!isAir(world.getBlockAt(x,y,z))){
                        return false;
                    }
                }
            }
        }
        return true;
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
