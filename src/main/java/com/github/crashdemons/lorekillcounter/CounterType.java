/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import java.util.HashMap;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public enum CounterType {
    INVALID("Invalid Counter"),
    PLAYER_KILLS("Player Kills",  "pk","players","pvp"),
    MOB_KILLS("Mob Kills",  "mk","mobs","pve"),
    
    PLAYER_HEADS("Players Beheaded","pheads","ph"),
    MOB_HEADS("Mobs Beheaded","mheads","mh")
    
    ;
    
    
    private String displayName=null;
    
    private static class Lookups {
        static final HashMap<String, CounterType> shortNames = new HashMap<>();
    }

    CounterType(String displayName, String... shortNames){
        this.displayName=displayName;
        for(String shortName : shortNames){
            Lookups.shortNames.put(shortName.toUpperCase(), this);
        }
    }
    
    public String getDisplayName(){
        if(displayName==null) return this.name();
        return displayName;
    }
    
    public static CounterType fromDisplayName(String name){
        //determine the type of counter on the item
        for(CounterType type : CounterType.values())   //check all counters for a matching name
            if(type.getDisplayName().equals(name))     //the name of the counter matches this item
                return type;
        return null;
    }
    
    public static CounterType fromShortName(String name){
        CounterType result = Lookups.shortNames.get(name.toUpperCase());
        if(result!=null) return result;
        try{
            return CounterType.valueOf(name.toUpperCase());
        }catch(Exception e){
            return null;
        }
    }
    
    
    public static CounterType fromEntityDeath(Entity e){
        if(e instanceof Player) return PLAYER_KILLS;
        if(!(e instanceof LivingEntity)) return null;
        if(e instanceof ArmorStand) return null;
        return MOB_KILLS;
    }
    
    public static CounterType fromEntityHeadDrop(Entity e){
        if(e instanceof Player) return PLAYER_HEADS;
        if(!(e instanceof LivingEntity)) return null;
        if(e instanceof ArmorStand) return null;
        return MOB_HEADS;
    }
}
