/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import com.github.crashdemons.lorekillcounter.integrations.PHListener;
import com.github.crashdemons.lorekillcounter.integrations.PlayerHeadsSupport;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main plugin class for MiningTrophies
 * @author crash
 */
public class LoreKillCounter extends JavaPlugin implements Listener{
    @Override
    public void onEnable(){
        getLogger().info("Enabling...");
        saveDefaultConfig();
        reloadConfig();
        
        if(PlayerHeadsSupport.isPresent()){
            getLogger().info("PlayerHeads support detected");
            getServer().getPluginManager().registerEvents(new PHListener(this), this);
        }
        
        getCommand("LoreKillCounter").setExecutor(new CounterCommandExecutor());
        getCommand("LoreKillCounter").setTabCompleter(new CounterTabCompleter());
        
        getServer().getPluginManager().registerEvents(new CounterListener(), this);
        getLogger().info("Enabled.");
        
    }
    @Override
    public void onDisable(){
        getLogger().info("Disabling...");
        saveConfig();
        getLogger().info("Disabled.");
    }

}
