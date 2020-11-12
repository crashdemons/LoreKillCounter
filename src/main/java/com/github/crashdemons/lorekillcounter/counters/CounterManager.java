/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.counters;

import com.github.crashdemons.lorekillcounter.LoreKillCounter;
import static com.github.crashdemons.lorekillcounter.counters.CounterBaseType.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CounterManager {
    private CounterManager(){}
    
    
    
    public static boolean applyCounterOperation(List<String> lore, CounterOperation operation){
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
        return true;
    }
    
    public static boolean applyCounterOperation(ItemStack stack, CounterOperation operation){
        if(stack==null) return false;
        ItemMeta meta = stack.getItemMeta();//spigot already checks if null and makes a new version - otherwise it returns a clone - should be safe to use directly!
        if(meta==null) meta = Bukkit.getItemFactory().getItemMeta(stack.getType());//unnecessary unless implementation differs from spigot-api
        if(meta==null) return false;//Item does not support any meta (eg: AIR)
        List<String> lore = (meta.hasLore()? meta.getLore() : new ArrayList<>());
        boolean result = applyCounterOperation(lore,operation);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return result;
    }
    public static boolean applyCounterOperation(Player player, CounterOperation operation){
        ItemStack stack = player.getInventory().getItemInMainHand();
        if(stack==null) return false;
        if(stack.getType()==Material.AIR) return false;
        return applyCounterOperation(stack,operation);
    }

    
    public static CounterAddResult addCounter(List<String> lore, Counter counter){
        if(lore.size()>=LoreKillCounter.instance.getLoreCap()) return CounterAddResult.ERROR_LORE_CAP;
        
        lore.add(counter.toStringFormatted());
        return CounterAddResult.SUCCESS;
    }
    public static CounterAddResult addCounter(ItemStack stack, Counter counter){
        if(stack==null) return CounterAddResult.ERROR_NO_ITEM;
        ItemMeta meta = stack.getItemMeta();
        if(meta==null) meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
        if(meta==null) return CounterAddResult.ERROR_NO_ITEM;//item does not support meta (eg: AIR)
        List<String> lore = (meta.hasLore()? meta.getLore() : new ArrayList<>());
        CounterAddResult result = addCounter(lore,counter);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return result;
    }
    public static CounterAddResult addCounter(Player player, Counter counter){
        ItemStack stack = player.getInventory().getItemInMainHand();
        if(stack==null) return CounterAddResult.ERROR_NO_ITEM;
        if(stack.getType()==Material.AIR) return CounterAddResult.ERROR_NO_ITEM;
        return addCounter(stack,counter);
    }
    

    
    private static CounterOperation createIncrementMatchOperation(List<CounterType> types){
        return (counter)->{
            //getLogger().info(" lore counter type = "+counter.getType() + " vs "+deathType);
            if(types.contains(counter.getType())){//the lore line is the same type of counter as this kill
                counter.increment();
            }
            return counter;
        };
    }
    
    
    public static boolean incrementMatchingCounter(Player player, CounterType type){
        return incrementMatchingCounters(player, Arrays.asList(type));
    }
    public static boolean incrementMatchingCounters(Player player, List<CounterType> types){
        if(types.isEmpty()) return true;
        ItemStack stack = player.getInventory().getItemInMainHand();
        if(stack==null) return false;
        if(stack.getType()==Material.AIR) return false;
        
        return CounterManager.applyCounterOperation(player,createIncrementMatchOperation(types));
    }
    
    
    
    
    //------------------------------------------------------------
    
    public static CounterType typeFromDisplayName(String name){
        //determine the type of counter on the item
        CounterType type = EntitySlainCounterType.fromDisplayName(name);
        if(type==null) type = CounterType.fromDisplayName(name);
        return type;
    }
    
    public static CounterType typeFromShortName(String name){
        return new CounterType(CounterBaseType.fromShortName(name));
    }
    
    private static boolean isOre(@NotNull Material m){
        String typeName = m.toString().toUpperCase();
        if(typeName.endsWith("_ORE")) return true;
        if(typeName.equals("ANCIENT_DEBRIS")) return true;
        return false;
    }
    
    public static List<CounterType> typeFromBlockBreak(Block b){
        ArrayList<CounterType> types = new ArrayList<>();
        Material mat = b.getType();
        if(mat==null || mat==Material.AIR) return types;//no mining of nothing please!
        if(isOre(mat)) types.add(ORES_MINED.createType());
        types.add(BLOCKS_MINED.createType());
        
        return types;
    }
    
    private static final double MARKSMAN_DISTANCE_SQUARED = 40*40;//50*50 is sniper duel;
    
    public static List<CounterType> typeFromEntityDeath(LivingEntity e){
        ArrayList<CounterType> types = new ArrayList<>();
        if(e instanceof ArmorStand) return types;
        
        EntityType type = e.getType();//notnull
        types.add(new EntitySlainCounterType(type));
        if(e instanceof Player) types.add(PLAYER_KILLS.createType());
        else types.add(MOB_KILLS.createType());
        types.add(ALL_KILLS.createType());
        
        Player killer = e.getKiller();
        if(killer!=null){
            if(killer.isGliding()){
                types.add(ELYTRA_KILLS.createType());
            }
            
            Location locA = e.getLocation();
            Location locB = killer.getLocation();
            try{
                if(locA.distanceSquared(locB)>=MARKSMAN_DISTANCE_SQUARED){
                    types.add(MARKSMAN_KILLS.createType());
                }
            }catch(Exception ex){ }//different dimension
        }else{ }
        
        return types;
    }
    
    public static List<CounterType> typeFromEntityHeadDrop(Entity e){
        ArrayList<CounterType> types = new ArrayList<>();
        if(!(e instanceof LivingEntity)) return types;
        if(e instanceof ArmorStand) return types;
        if(e instanceof Player) types.add(PLAYER_HEADS.createType());
        else types.add(MOB_HEADS.createType());
        types.add(ALL_HEADS.createType());
        return types;
    }
}
