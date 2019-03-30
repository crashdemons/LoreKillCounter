# LoreKillCounter
Spigot 1.13 plugin that counts kills on enabled items

This plugin is not yet finished!


# Downloading the Plugin pre-built
You can find released JARs (that can be added to your plugins folder) on the [Releases tab](https://github.com/crashdemons/LoreKillCounter/releases) of this project.

If you want something a bit more shiny, you can check out our [development builds](https://ci.meme.tips/job/LoreKillCounter/) available through Jenkins CI.

# Building the Project yourself
We've recently moved to using Maven! If you used build.xml or a Netbeans Project before, you may need to import the project again as a maven project / from existing POM.

[This document](https://github.com/crashdemons/Notes/blob/master/Importing_Maven_Projects.md) may help you import the project in your IDE.

# API

You can build your own plugins against MiningTrophies by including the following in your configuration.

Class/API documention is available [here](https://crashdemons.github.io/LoreKillCounter/).

Respositories:

        <repository>
            <id>crashdemons-repo</id>
            <url>https://meme.tips/java-repos/</url>
        </repository>
        
Depend on LoreKillCounter:

        <dependency>
            <groupId>com.github.crashdemons</groupId>
            <artifactId>LoreKillCounter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
