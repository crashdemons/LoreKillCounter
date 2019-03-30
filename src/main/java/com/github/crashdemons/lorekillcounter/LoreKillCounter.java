/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        if(!sender.hasPermission("lorekillcounter.modify")){
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }
        
        return false;
    }
    
    public void onEntityDeathEvent(EntityDeathEvent event){
        LivingEntity killed = event.getEntity();
        Player killer = event.getEntity().getKiller();
        if(killer==null) return;
        if(!(killer instanceof Player)) return;
        CounterType deathType = CounterType.fromEntityDeath(killed);
        
        ItemStack stack = killer.getInventory().getItemInMainHand();
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = (meta.hasLore()? meta.getLore() : new ArrayList<>());
        
        
        //update relevant counters
        for(int i=0;i<lore.size();i++){
            Counter counter = Counter.fromLoreLine(lore.get(i));
            if(counter!=null && counter.isValid()){//lore line is a valid counter
                if(counter.getType()==deathType){//the lore line is the same type of counter as this kill
                    counter.increment();
                    lore.set(i, counter.toStringFormatted());
                }
            }
        }
        
        meta.setLore(lore);
        stack.setItemMeta(meta);
        
        
    }
}
