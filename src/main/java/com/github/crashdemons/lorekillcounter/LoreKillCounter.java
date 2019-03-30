/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import java.util.Random;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main plugin class for MiningTrophies
 * @author crash
 */
public class LoreKillCounter extends JavaPlugin implements Listener{

    private final Random rand = new Random();
    

    
    @Override
    public void onEnable(){
        getLogger().info("Enabling...");
        saveDefaultConfig();
        reloadConfig();
        
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Enabled.");
        
    }
    @Override
    public void onDisable(){
        getLogger().info("Disabling...");
        saveConfig();
        getLogger().info("Disabled.");
    }
    
        @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("LoreKillCounter")) {
            return false;
        }
        return false;
    }
    
    public void onEntityDeathEvent(EntityDeathEvent event){
        
    }
    
    public void onBlockBreakEvent(BlockBreakEvent event){
        //if(event instanceof SimulatedBlockBreakEvent) return;
        
    }
    
}
