/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    
    public void applyCounterOperation(List<String> lore, CounterOperation operation){
        for(int i=0;i<lore.size();i++){
            Counter counter = Counter.fromLoreLine(lore.get(i));
            if(counter!=null && counter.isValid()){//lore line is a valid counter
                counter = operation.apply(counter);
                if(counter==null || !counter.isValid()){
                    lore.set(i,"REMOVED_KILL_COUNTER:REMOVED_KILL_COUNTER");
                }else{
                    lore.set(i, counter.toStringFormatted());
                }
            }
        }
        lore.removeIf((line)->line.equals("REMOVED_KILL_COUNTER:REMOVED_KILL_COUNTER"));
    }
    
    public void applyCounterOperation(ItemStack stack, CounterOperation operation){
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = (meta.hasLore()? meta.getLore() : new ArrayList<>());
        applyCounterOperation(lore,operation);
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }
    public void applyCounterOperation(Player player, CounterOperation operation){
       ItemStack stack = player.getInventory().getItemInMainHand();
       applyCounterOperation(stack,operation);
    }
    
    public void addCounter(List<String> lore, Counter counter){
        lore.add(counter.toStringFormatted());
    }
    public void addCounter(ItemStack stack, Counter counter){
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = (meta.hasLore()? meta.getLore() : new ArrayList<>());
        addCounter(lore,counter);
        meta.setLore(lore);
        stack.setItemMeta(meta);
    }
    public void addCounter(Player player, Counter counter){
       ItemStack stack = player.getInventory().getItemInMainHand();
       addCounter(stack,counter);
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
        //boolean senderIsPlayer = (sender instanceof Player);
        
        //   counter add pk/mk [user]
        //   counter remove [user]
        if(args.length<1) return false;
        if(args.length>3) return false;
        
        
        String targetUser = sender.getName();
        if(args[0].equals("remove") || args[0].equals("clear")){
            if(args.length==2) targetUser = args[1];
            Player target = Bukkit.getPlayer(targetUser);
            if(target==null){
                sender.sendMessage(ChatColor.RED + "Target player either not specified or not found");
                return true;
            }
            applyCounterOperation(target,(counter)->null);
            sender.sendMessage(ChatColor.GREEN+"Counters cleared for "+target.getName());
            return true;
        }else if(args[0].equals("add")){
            if(args.length<2) return false;
            if(args.length==3) targetUser = args[2];
            Player target = Bukkit.getPlayer(targetUser);
            if(target==null){
                sender.sendMessage(ChatColor.RED + "Target player either not specified or not found");
                return true;
            }
            String str_type = args[1];
            CounterType type = CounterType.INVALID;
            switch(str_type){
                case "pk":
                case "players":
                case "player_kills":
                    type=CounterType.PLAYER_KILLS;
                    break;
                case "mk":
                case "mobs":
                case "mob_kills":
                    type=CounterType.MOB_KILLS;
                    break;
            }
            if(type==CounterType.INVALID){
                sender.sendMessage(ChatColor.RED + "Invalid counter type.");
                return true;
            }
            addCounter(target,new Counter(type));
            sender.sendMessage(ChatColor.GREEN+"Added "+type.getDisplayName()+" counter for "+target.getName());
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
        
        applyCounterOperation(killer,(counter)->{
            if(counter.getType()==deathType){//the lore line is the same type of counter as this kill
                counter.increment();
            }
            return counter;
        });
        
        /*
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
        */
        
        
        
    }
}
