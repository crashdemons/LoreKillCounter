/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main plugin class for MiningTrophies
 * @author crash
 */
public class LoreKillCounter extends JavaPlugin implements Listener{

    //private final Random rand = new Random();
    
    
    @Override
    public void onEnable(){
        getLogger().info("Enabling...");
        saveDefaultConfig();
        reloadConfig();
        
        if(PlayerHeadsSupport.isPresent()){
            getLogger().info("PlayerHeads support detected");
            getServer().getPluginManager().registerEvents(new PHListener(this), this);
        }
        
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
            boolean result = CounterManager.applyCounterOperation(target,(counter)->null);
            if(result)
                sender.sendMessage(ChatColor.GREEN+"Counters cleared for "+target.getName());
            else
                sender.sendMessage(ChatColor.RED+"Could not clear counters on that (for "+target.getName()+") - is anything being held?");
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
            CounterType type = CounterType.fromShortName(str_type);
            if(type==null || type==CounterType.INVALID){
                sender.sendMessage(ChatColor.RED + "Invalid counter type - must be `mobs` or `players`.");
                return true;
            }
            boolean result = CounterManager.addCounter(target,new Counter(type));
            if(result)
                sender.sendMessage(ChatColor.GREEN+"Added "+type.getDisplayName()+" counter for "+target.getName());
            else
                sender.sendMessage(ChatColor.RED+"Could not add "+type.getDisplayName()+" counter to that (for "+target.getName()+") - is anything being held?");
                
            return true;
        }
        return false;
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onBlockBreakEvent(BlockBreakEvent event){
        if(MiningTrophiesSupport.isSimulatedBlockBreak(event)) return;
        
        Block block = event.getBlock();
        if(block==null) return;
        List<CounterType> breakTypes = CounterType.fromBlockBreak( block );
        if(breakTypes.isEmpty()) return;
        Player player = event.getPlayer();
        if(player==null) return;
        
        
        CounterManager.incrementMatchingCounters(player, breakTypes);
        
    }
    @EventHandler(ignoreCancelled=true)
    public void onEntityDeathEvent(EntityDeathEvent event){
        
        //getLogger().info("LKC death event ");
        LivingEntity killed = event.getEntity();
        Player killer = event.getEntity().getKiller();
        if(killer==null) return;
        //getLogger().info(" killer exists");
        if(!(killer instanceof Player)) return;
        if(!killer.hasPermission("lorekillcounter.counted")) return;
        //getLogger().info(" killer player");
        
        List<CounterType> deathTypes = CounterType.fromEntityDeath(killed);
        
        
        //getLogger().info("deathType = "+deathType);
        
        if(deathTypes.isEmpty()) return;
        
        if((killer instanceof Player) && (killed instanceof Player)){
            if(killer.getUniqueId().equals(killed.getUniqueId())) return;//don't allow suicides to increase the PK counter
        }
        
        
       // getLogger().info(" counter valid");
        
        CounterManager.incrementMatchingCounters(killer, deathTypes);
        
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
