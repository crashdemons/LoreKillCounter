/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.counters;

import static com.github.crashdemons.lorekillcounter.counters.CounterBaseType.MOB_HEADS;
import static com.github.crashdemons.lorekillcounter.counters.CounterBaseType.MOB_KILLS;
import static com.github.crashdemons.lorekillcounter.counters.CounterBaseType.ORES_MINED;
import static com.github.crashdemons.lorekillcounter.counters.CounterBaseType.PLAYER_HEADS;
import static com.github.crashdemons.lorekillcounter.counters.CounterBaseType.PLAYER_KILLS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
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

    
    public static boolean addCounter(List<String> lore, Counter counter){
        lore.add(counter.toStringFormatted());
        return true;
    }
    public static boolean addCounter(ItemStack stack, Counter counter){
        if(stack==null) return false;
        ItemMeta meta = stack.getItemMeta();
        if(meta==null) meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
        if(meta==null) return false;//item does not support meta (eg: AIR)
        List<String> lore = (meta.hasLore()? meta.getLore() : new ArrayList<>());
        boolean result = addCounter(lore,counter);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return result;
    }
    public static boolean addCounter(Player player, Counter counter){
        ItemStack stack = player.getInventory().getItemInMainHand();
        if(stack==null) return false;
        if(stack.getType()==Material.AIR) return false;
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
        
        return types;
    }
    
    
    public static List<CounterType> typeFromEntityDeath(Entity e){
        ArrayList<CounterType> types = new ArrayList<>();
        if(!(e instanceof LivingEntity)) return types;
        if(e instanceof ArmorStand) return types;
        
        EntityType type = e.getType();//notnull
        types.add(new EntitySlainCounterType(type));
        if(e instanceof Player) types.add(PLAYER_KILLS.createType());
        else types.add(MOB_KILLS.createType());
        return types;
    }
    
    public static CounterType typeFromEntityHeadDrop(Entity e){
        if(!(e instanceof LivingEntity)) return null;
        if(e instanceof ArmorStand) return null;
        if(e instanceof Player) return PLAYER_HEADS.createType();
        return MOB_HEADS.createType();
    }
}