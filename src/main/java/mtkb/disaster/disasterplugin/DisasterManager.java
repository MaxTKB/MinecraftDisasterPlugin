package mtkb.disaster.disasterplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
        disasterList.add(DisasterManager::doubleDisaster);
        disasterList.add(DisasterManager::teleportSwapDisaster);
        disasterList.add(DisasterManager::mlgWaterDisaster);
        disasterList.add(DisasterManager::wolfDisaster);
        disasterList.add(DisasterManager::undeadInvasionDisaster);
        disasterList.add(DisasterManager::shuffleInventoryDisaster);
        disasterList.add(DisasterManager::swapInventoryDisaster);
        disasterList.add(DisasterManager::sunburnDisaster);
        disasterList.add(DisasterManager::realSpiderDisaster);
        disasterList.add(DisasterManager::giantDisaster);
        disasterList.add(DisasterManager::killerBunnyDisaster);
        disasterList.add(DisasterManager::timeWarpDisaster);
        disasterList.add(DisasterManager::noJumpDisaster);
        disasterList.add(DisasterManager::shortArmsDisaster);
        disasterList.add(DisasterManager::wardenDisaster);
        disasterList.add(DisasterManager::tinyDisaster);
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
        // Infested 10

        for (Player player : Bukkit.getOnlinePlayers()) {
            int duration = 15 * 20;
            int effectId = random.nextInt(11);
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
                    potionEffect = new PotionEffect(PotionEffectType.MINING_FATIGUE, duration, 1);
                    break;
                case 5:
                    potionEffect = new PotionEffect(PotionEffectType.NAUSEA, duration, 0);
                    break;
                case 6:
                    potionEffect = new PotionEffect(PotionEffectType.POISON, duration, 0);
                    break;
                case 7:
                    potionEffect = new PotionEffect(PotionEffectType.SLOWNESS, duration, 1);
                    break;
                case 8:
                    potionEffect = new PotionEffect(PotionEffectType.WEAKNESS, duration, 1);
                    break;
                case 9:
                    potionEffect = new PotionEffect(PotionEffectType.WITHER, duration, 0);
                    break;
                case 10:
                    potionEffect = new PotionEffect(PotionEffectType.INFESTED, duration, 0);
                    break;
                default:
                    Bukkit.getServer().sendMessage(Component.text("§cUnknown effect ID"));
                    return;
            }

            if (player.getGameMode()!= GameMode.SPECTATOR) {
                player.addPotionEffect(potionEffect);
            }
        }
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: RANDOM POTION EFFECT"));
    }

    private static void teleportDisaster() {
        int teleportRadius = 50;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode()!= GameMode.SPECTATOR) {
                Location newLocation = getSpawnableLocation(player, teleportRadius, true);
                player.teleport(newLocation);
            }
        }
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: TELEPORT TO RANDOM LOCATION"));
    }

    private static void raidDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: MAX LEVEL RAID"));
        List<Player> onlinePlayers = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.SPECTATOR) {
                onlinePlayers.add(player);
            }
        }

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
            if (player.getGameMode() != GameMode.SPECTATOR) {
                player.getInventory().setItemInMainHand(null);
            }
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
            if (player.getGameMode() != GameMode.SPECTATOR) {
                Location spawnLocation = getSpawnableLocation(player, radius, false);
                Creeper creeper = (Creeper) world.spawnEntity(spawnLocation, EntityType.CREEPER);
                creeper.setPowered(true); // Set creeper to be charged
            }
        }
    }

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
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            Location spawnLocation = getSpawnableGhast(player, radius);
            for (int i = 0; i < 3; i++) {
                Ghast ghast = (Ghast) world.spawnEntity(spawnLocation, EntityType.GHAST);
                ghast.setTarget(player);
            }
        }
    }

    private static void healthDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: MAX HEALTH = 1 HEART"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            double newMaxHealth = 2.0;
            player.setHealth(newMaxHealth);
            player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(newMaxHealth);

            if(player.getHealth() > newMaxHealth) {
                player.setHealth(newMaxHealth);
            }

            scheduler.runTaskLater(plugin, () -> restorePlayerHealth(player), (long) (20*time*60));
        }
        scheduler.runTaskLater(plugin, () -> Bukkit.getServer().sendMessage(Component.text("§aRESTORING HEALTH")), (long) (20*time*60));
    }

    public static void restorePlayerHealth(Player player) {
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20.0);
    }

    private static void doubleDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: DOUBLE DISASTER"));
        performDisaster();
        performDisaster();
    }

    private static void teleportSwapDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: SWAPPING LOCATIONS"));

        // Create a player list that filters out all players that are in spectator mode
        List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).collect(Collectors.toList());

        Player[] playerArray = onlinePlayers.toArray(new Player[0]);
        if (playerArray.length <= 1) {
            Bukkit.getServer().sendMessage(Component.text("§cNot enough players online to swap locations."));
            return;
        }
        Location[] initialLocations = new Location[playerArray.length];
        for (int i = 0; i < playerArray.length; i++) {
            initialLocations[i] = playerArray[i].getLocation().clone();
        }

        for (int i = 0; i < playerArray.length; i++) {
            int nextIndex = (i+1) % playerArray.length;
            playerArray[i].teleport(initialLocations[nextIndex]);
        }
    }

    private static void mlgWaterDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: MLG WATER"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            Location playerLocation = player.getLocation();
            World world = player.getWorld();
            if (!world.getName().equals("world_nether")) { // If player is not in Nether
                player.getInventory().setItemInMainHand(ItemStack.of(Material.WATER_BUCKET));
                Block highestBlock = world.getHighestBlockAt(playerLocation);
                while (highestBlock.getType() == Material.AIR) {
                    highestBlock = highestBlock.getRelative(0,-1,0);
                }
                Location newLocation = highestBlock.getLocation().add(0.5,50,0.5);
                newLocation.setPitch(90.0f);
                player.teleport(newLocation);
            }
            else {
                player.sendMessage(Component.text("§cYou got lucky this time."));
            }
        }
    }

    private static void wolfDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: ANGRY WOLVES"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            World world = player.getWorld();
            Location playerLocation = player.getLocation();
            for (int i = 0; i < 5; i++) {
                Wolf wolf = (Wolf) world.spawnEntity(playerLocation, EntityType.WOLF);
                wolf.setAngry(true);
                wolf.setTarget(player);
            }
        }
    }

    private static void undeadInvasionDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: UNDEAD INVASION"));
        for (Player player: Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();
            if (world.getDifficulty()==Difficulty.PEACEFUL){
                Bukkit.getServer().sendMessage(Component.text("§cDifficulty set to peaceful, cannot spawn mobs."));
                break;
            }
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            Location spawnLocation = getSpawnableLocation(player, 10, false);
            for (int i = 0; i < 1; i++) {
                ItemStack helmet = ItemStack.of(Material.LEATHER_HELMET);
                Zombie zombie = (Zombie) world.spawnEntity(spawnLocation, EntityType.ZOMBIE);
                zombie.getEquipment().setHelmet(helmet);
                Skeleton skeleton = (Skeleton) world.spawnEntity(spawnLocation, EntityType.SKELETON);
                skeleton.getEquipment().setHelmet(helmet);
                Drowned drowned = (Drowned) world.spawnEntity(spawnLocation, EntityType.DROWNED);
                drowned.getEquipment().setHelmet(helmet);
                world.spawnEntity(spawnLocation, EntityType.HUSK);
                Stray stray = (Stray) world.spawnEntity(spawnLocation, EntityType.STRAY);
                stray.getEquipment().setHelmet(helmet);
                world.spawnEntity(spawnLocation, EntityType.WITHER_SKELETON);
                PigZombie pigZombie = (PigZombie) world.spawnEntity(spawnLocation, EntityType.ZOMBIFIED_PIGLIN);
                pigZombie.setAngry(true);
                pigZombie.setTarget(player);
                Bogged bogged = (Bogged) world.spawnEntity(spawnLocation, EntityType.BOGGED);
                bogged.getEquipment().setHelmet(helmet);
            }
        }
    }

    private static void shuffleInventoryDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: SHUFFLING INVENTORY"));
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            Inventory inventory = player.getInventory();
            int inventorySize = inventory.getSize();

            for (int i = 0; i < inventorySize; i++) {
                int randomSlot = random.nextInt(inventorySize-1);

                ItemStack currentItem = inventory.getItem(i);
                ItemStack randomItem = inventory.getItem(randomSlot);

                inventory.setItem(i, randomItem);
                inventory.setItem(randomSlot, currentItem);
            }
        }
    }

    private static void swapInventoryDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: SWAPPING INVENTORIES"));

        // Create a player list that filters out all players that are in spectator mode
        List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).collect(Collectors.toList());
        if (onlinePlayers.size() <= 1) {
            Bukkit.getServer().sendMessage(Component.text("§cNot enough players online to swap inventories."));
            return;
        }

        ItemStack[][] contentsArray = new ItemStack[onlinePlayers.size()][];
        for (int i = 0; i< onlinePlayers.size(); i++) {
            ItemStack[] contents = onlinePlayers.get(i).getInventory().getContents();
            contentsArray[i] = contents;
        }

        for (int i = 0; i < onlinePlayers.size(); i++) {
            int nextIndex = (i+1) % onlinePlayers.size();
            onlinePlayers.get(i).getInventory().setContents(contentsArray[nextIndex]);
        }
    }

    private static void sunburnDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: SUNBURN"));

        new BukkitRunnable() {
            double sunburnTime = time;
            @Override
            public void run() {
                long timeOfDay = Bukkit.getWorlds().get(0).getTime() % 24000;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.SPECTATOR) {
                        continue; // Skip if the player is in spectator mode
                    }
                    boolean underSun = player.getLocation().getBlock().getLightFromSky() == 15;
                    if (underSun && (timeOfDay < 12542) && !isWearingFullArmor(player)) {
                        player.setFireTicks(40);
                    }
                }
                sunburnTime = sunburnTime-1.0/60;
                if(sunburnTime<=0) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private static void realSpiderDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: ARACHNOPHOBIA"));
        for (Player player: Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();
            if (world.getDifficulty() == Difficulty.PEACEFUL) {
                Bukkit.getServer().sendMessage(Component.text("§cDifficulty set to peaceful, cannot spawn mobs."));
                break;
            }

            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }

            // Set scale between 0.1 and 0.3 for normal spiders
            // Set scale between 0.2 and 0.4 for cave spiders

            Location spawnLocation = getSpawnableLocation(player, 5, false);

            for (int i=0; i<3; i++) {
                double spiderSize = 0.1 + (0.3-0.1) * random.nextDouble();
                double caveSpiderSize = 0.2 + (0.4-0.2) * random.nextDouble();
                Spider spider = (Spider) world.spawnEntity(spawnLocation, EntityType.SPIDER);
                CaveSpider caveSpider = (CaveSpider) world.spawnEntity(spawnLocation, EntityType.CAVE_SPIDER);
                spider.getAttribute(Attribute.SCALE).setBaseValue(spiderSize);
                spider.getAttribute(Attribute.MAX_HEALTH).setBaseValue(1);
                caveSpider.getAttribute(Attribute.SCALE).setBaseValue(caveSpiderSize);
                caveSpider.getAttribute(Attribute.MAX_HEALTH).setBaseValue(1);
                spider.setAggressive(true);
                spider.setTarget(player);
                caveSpider.setAggressive(true);
                caveSpider.setTarget(player);
            }
        }
    }

    private static void giantDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: GIANT ATTACK"));
        for (Player player: Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();
            if (world.getDifficulty() == Difficulty.PEACEFUL) {
                Bukkit.getServer().sendMessage(Component.text("§cDifficulty set to peaceful, cannot spawn mobs."));
                break;
            }
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            Location spawnLocation = getSpawnableGiant(player, 10);
            Zombie zombie = (Zombie) world.spawnEntity(spawnLocation, EntityType.ZOMBIE);
            zombie.getAttribute(Attribute.MAX_HEALTH).setBaseValue(100);
            zombie.setHealth(100);
            zombie.setAge(0);
            zombie.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(26);
            zombie.getAttribute(Attribute.SCALE).setBaseValue(6);
            zombie.customName(Component.text("Giant"));
            zombie.setCustomNameVisible(true);
            zombie.setCanPickupItems(false);
            EntityEquipment zombieInv = zombie.getEquipment();
            zombieInv.setItemInMainHand(null);
            zombieInv.setHelmet(null);
            zombieInv.setChestplate(null);
            zombieInv.setLeggings(null);
            zombieInv.setBoots(null);
        }
    }

    private static void killerBunnyDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: KILLER BUNNY ATTACK"));
        for (Player player: Bukkit.getOnlinePlayers()) {
            World world = player.getWorld();
            if (world.getDifficulty() == Difficulty.PEACEFUL) {
                Bukkit.getServer().sendMessage(Component.text("§cDifficulty set to peaceful, cannot spawn mobs."));
                break;
            }
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            Location spawnLocation = getSpawnableLocation(player, 5, false);
            Rabbit rabbit = (Rabbit) world.spawnEntity(spawnLocation, EntityType.RABBIT);
            rabbit.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
        }
    }

    private static void timeWarpDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: TIME WARP"));
        int tickSpeed = 20;
        double multiplyer = 0.5 + (2.0 - 0.5)*Math.random();
        double newTickSpeed = Math.round(tickSpeed*multiplyer*10.0)/10.0;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tick rate "+newTickSpeed);
        if (multiplyer>1.0) {
            for (Player player: Bukkit.getOnlinePlayers()) {
                if (player.getGameMode() == GameMode.SPECTATOR) {
                    continue; // Skip if the player is in spectator mode
                }
                double defaultSpeed = 0.10000000149011612;
                double newSpeed = defaultSpeed*multiplyer;
                int hasteLvl = (int) Math.round(5*(multiplyer-1));
                PotionEffect hasteEffect = new PotionEffect(PotionEffectType.HASTE, Integer.MAX_VALUE, hasteLvl, false, false, true);
                player.addPotionEffect(hasteEffect);
                player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(newSpeed);
                scheduler.runTaskLater(plugin, () -> {
                    player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(defaultSpeed);
                    player.removePotionEffect(PotionEffectType.HASTE);
                }, (long) (newTickSpeed*time*60));
            }
        }
        scheduler.runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tick rate 20"), (long) (newTickSpeed*time*60));
    }

    public static void resetSpeed() {
        // Perhaps make this runnable with the /disaster command
        for (Player player: Bukkit.getOnlinePlayers()) {
            double defaultSpeed = 0.10000000149011612;
            player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(defaultSpeed);
        }
    }

    private static void noJumpDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: NO JUMPING"));
        double defaultJump = 0.41999998688697815;
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            player.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(0);
            scheduler.runTaskLater(plugin, () -> player.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(defaultJump), (long) (20*time*60));
        }
    }

    private static void shortArmsDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: SHORT ARMS"));
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE).setBaseValue(2);
            player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE).setBaseValue(0.5);
            scheduler.runTaskLater(plugin, () -> resetReach(player), (long) (20*time*60));
        }
        scheduler.runTaskLater(plugin, () -> Bukkit.getServer().sendMessage(Component.text("§aRESTORING REACH")), (long) (20*time*60));
    }

    public static void resetReach(Player player) {
        // Default values are:
        // block interaction range = 4.5
        // entity interaction range = 3
        player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE).setBaseValue(4.5);
        player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE).setBaseValue(3);
    }

    private static void wardenDisaster() {
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: WARDEN"));
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            Location playerLocation = player.getLocation();
            Location spawnLocation = getSpawnableLocation(player, 10, false);
            World world = player.getWorld();
            Warden warden = (Warden) world.spawnEntity(spawnLocation, EntityType.WARDEN);
            world.playSound(spawnLocation, "entity.warden.emerge", 5.0f, 1.0f);
            warden.setAnger(player, 80);
            warden.setDisturbanceLocation(playerLocation);
            scheduler.runTaskLater(plugin, () -> {
                Location newLocation = player.getLocation();
                world.playSound(newLocation, "entity.warden.dig", 5.0f, 1.0f);
                warden.remove();
            }, (long) (20*time*60));
        }
    }

    private static void tinyDisaster() { // WORKSHOP IT
        Bukkit.getServer().sendMessage(Component.text("§aDISASTER: TINY AND FRAGILE"));
        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                continue; // Skip if the player is in spectator mode
            }
            player.getAttribute(Attribute.SCALE).setBaseValue(0.5); // Default 1.0
            player.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(0.5); // Default 1.0
            player.setHealth(10.0);
            player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(10.0); // Default 20.0
            scheduler.runTaskLater(plugin, () -> {
                resetSize(player);
            }, (long) (20*time*60));
        }
        scheduler.runTaskLater(plugin, () -> Bukkit.getServer().sendMessage(Component.text("§aRESTORING SIZE")), (long) (20*time*60));
    }

    public static void resetSize(Player player) {
        player.getAttribute(Attribute.SCALE).setBaseValue(1.0); // Default 1.0
        player.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(1.0); // Default 1.0
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20.0); // Default 20.0
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
        while(!isAir(newLocation.getBlock()) && count<maxAttempts && !airSurrounds(newLocation)) {
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

    private static Location getSpawnableGiant(Player player, int radius) {
        int count = 0;
        int maxAttempts = 100;
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        double randX = playerLocation.getX() + random.nextInt(2*radius) - radius;
        double randY = playerLocation.getY() + random.nextInt(2*radius) - radius;
        double randZ = playerLocation.getZ() + random.nextInt(2*radius) - radius;
        Location newLocation = new Location(world, randX, randY, randZ);
        while(!hasGiantSpace(newLocation) && count<maxAttempts) {
            randX = playerLocation.getX() + random.nextInt(2*radius) - radius;
            randY = playerLocation.getY() + random.nextInt(2*radius) - radius;
            randZ = playerLocation.getZ() + random.nextInt(2*radius) - radius;
            newLocation = new Location(world, randX, randY, randZ);
            count++;
        }

        if (!isSpawnable(newLocation) && !hasGiantSpace(newLocation)) {
            Block highestBlock = world.getHighestBlockAt(newLocation);
            while (highestBlock.getType() == Material.AIR) {
                highestBlock = highestBlock.getRelative(0,-1,0);
            }
            newLocation = highestBlock.getLocation().add(0.5,1,0.5);
        }
        return newLocation;
    }

    private static boolean hasGiantSpace(Location location) {
        World world = location.getWorld();
        int locationX = location.getBlockX();
        int locationY = location.getBlockY();
        int locationZ = location.getBlockZ();

        for (int x = locationX -1 ; x<= locationX + 4; x++){
            for (int y = locationY; y <= locationY +13; y++) {
                for (int z = locationZ -1; z <= locationZ + 4; z++) {
                    if(!isAir(world.getBlockAt(x,y,z))){
                        return false;
                    }
                }
            }
        }
        Block blockBeneath = world.getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
        return blockBeneath.getType().isSolid();
    }

    // Used to check if an area is big enough to spawn a ghast, only usage right now is in the ghast disaster so can be left as is for now
    private static boolean airSurrounds(Location location) {
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

    private static boolean isWearingFullArmor(Player player) {
        PlayerInventory playerInv = player.getInventory();
        return(playerInv.getHelmet()!= null && playerInv.getChestplate() != null && playerInv.getLeggings() != null && playerInv.getBoots() != null);
    }
}
