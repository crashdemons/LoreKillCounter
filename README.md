# MiningTrophies
Spigot 1.13 plugin that drops rare gems when mining

When a player is very fortunate when mining certain blocks, this plugin drops a named item rewarding them for their efforts.

Drop rates are configurable, 

## Drop rate modificiation
All drop-rates can be changed for this plugin, as well as the rate at which the Fortune enchantment increases the droprate.

Each level of the Fortune modification adds 20% of the base droprate by default. 

For example, a droprate of 5% (0.05) would increase to 6% with Fortune I, 7% with Fortune II, 8% with fortune III. However, default rates are much much lower and see smaller gains from fortune.

By default, droprates are set to 0.01% chance per mined block, but are easily configured.

To change the droprate of blocks change the appropriate `<blockname>droprate` entry in config.yml.

To change the rate at which fortune affects the droprate change `fortunerate` entry in config.yml

## Dropped items
![Image of Perfect Diamond](https://i.imgur.com/p7SmE0E.png)

Block Mined | Trophy received | Trophy type
------------- | ------------- | -------------
Diamond Ore | "Perfect Diamond" | named diamond with enchanted appearance
Emerald Ore | "Perfect Emerald" | named emerald with enchanted appearance
Redstone Ore | "Sparking Redstone" | named redstone dust with enchanted appearance
Lapis Ore | "Marbled Lapis" | named lapis lazuli with enchanted appearance
Nether Quartz Ore | "Rose-Quartz" | named quartz item with enchanted appearance
Clay | "Pure Clay" | named clay ball with enchanted appearance
Glowstone | "Burning Glowstone" | named glowstone dust with enchanted appearance
Turtle Egg | "Scute of Shame" | named scute with enchanted appearance and description
Glass | "What-a-Pane" | named glass pane with enchanted appearance and description - for griefers
Coal Ore | "Fuming Coal" | named coal with enchanted appearance (added in 0.6.6-SNAPSHOT)

## Permissions
 * `miningtrophies.config.reload` allows using the reload command (default: ops)
 * `miningtrophies.canberewarded` allows getting rewards at all (default: enabled for everyone)
 * `miningtrophies.alwaysrewarded` forces you to always have the best dropchance (default: disabled for everyone)

## Commands
 * `/mt reload` - Reload configuration settings, drop rates, etc.

## Notes
Only players in survival or adventure modes can gain rewards.
Use of silktouch tools and shears are disabled so that the block must be actually broken for a reward to be dropped.




# Downloading the Plugin pre-built
You can find released JARs (that can be added to your plugins folder) on the [Releases tab](https://github.com/crashdemons/MiningTrophies/releases) of this project.

If you want something a bit more shiny, you can check out our [development builds](https://ci.meme.tips/job/MiningTrophies/) available through Jenkins CI.

# Building the Project yourself
We've recently moved to using Maven! If you used build.xml or a Netbeans Project before, you may need to import the project again as a maven project / from existing POM.

[This document](https://github.com/crashdemons/Notes/blob/master/Importing_Maven_Projects.md) may help you import the project in your IDE.

# API

You can build your own plugins against MiningTrophies by including the following in your configuration.

Class/API documention is available [here](https://crashdemons.github.io/MiningTrophies/).

Respositories:

        <repository>
            <id>crashdemons-repo</id>
            <url>https://meme.tips/java-repos/</url>
        </repository>
        
Depend on MiningTrophies:

        <dependency>
            <groupId>com.github.crashdemons</groupId>
            <artifactId>MiningTrophies</artifactId>
            <version>0.6.1-SNAPSHOT</version>
        </dependency>
