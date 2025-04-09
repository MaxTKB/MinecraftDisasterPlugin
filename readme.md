# Minecraft Disaster Plugin

The **Minecraft Disaster Plugin** is a Paper plugin that introduces random disasters into your Minecraft server, bringing a new sense of challenge and unpredictability to gameplay.

Disasters range from:
- Giving every player a random negative potion effect
- Shuffling the items around in each players inventory
- Spawning a swarm of tiny, realistically-sized spiders that attack the players

  ... and much more!

## 📖 Features
- Over 20 unique random disasters which trigger at regular, set intervals
- Configurable disaster frequency and pre-start countdown
- Easy-to-use commands to start, stop, or trigger specific disasters
- Supports command aliases for faster typing

## ⚙️ Configuration

Users are able to customise the plugin behaviour in two ways:
- **Config File**: Manually adjusting the **default** time and countdown settings. These values will be used each time the server starts.

    *For Example, setting the time to 1.0 and the countdown to 5.0 in the config means that, by default, a disaster will happen every minute, there will be a 5-second countdown before the disaster cycle starts, and you won't have to specify them with a command each time.*
- **In-Game Commands**: Set disaster timing and countdown on the fly, temporarily overriding the config values during gameplay, although the values will be reset to the values in the config if the server is restarted.

By default, disasters occur every **5 minutes** with a **10-second** countdown before the disaster cycle begins. For example, once the command to start is run, there will be a 10-second countdown, then a random disaster will trigger every 5 minutes.

## 🔧 Basic Commands
### Start Disaster Cycle

```
/disaster enable
```

Or use aliases:

```
/d enable | /dis enable | /disaster start | /d start | /dis start
```

### Start with Custom Timing
```
/disaster enable [time] [countdown]
```
Where *"time"* is the time between disasters in minutes and *"countdown"* is the countdown before the disaster cycle starts, in seconds.

Example:
```
/disaster enable 2 15
```
This will start a 15-second countdown before the disaster cycle, then a disaster will occur every 2 minutes.

### Stop Disaster Cycle

```
/disaster disable
```

Or use aliases:

```
/d disable | /dis disable | /disaster stop | /d stop | /dis stop
```

## 📚 Learn More

For a detailed list of disasters, configuration options, and advanced usage, check out the [wiki page](https://github.com/maxbartrip/MinecraftDisasterPlugin/wiki).


## 🛠️ How to Install

1. **Download the plugin:**
   - Head to the [releases](https://github.com/MaxTKB/MinecraftDisasterPlugin/releases) section.
   - Click on the desired version, and click ``disasterplugin.jar`` to download
2. **Ensure compatibility:**
   - Make sure you are using a **Paper Server** that's compatible with the version of the plugin you downloaded.
3. **Install the plugin:**
   - Simply place the downloaded ``disasterplugin.jar`` file into the ``/plugins`` folder of your server.
4. **Restart the server:**
   - Restart your server to load the plugin and apl the changes
5. **Verify installation:**
   - You can check that the plugin is installed and working by checking the ``/disaster`` command or running ``/help disaster`` to see the available commands.