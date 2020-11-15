/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.counters;

import java.util.HashMap;
import org.bukkit.entity.EntityType;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public enum CounterBaseType {
    INVALID("Invalid Counter"),
    
    ALL_KILLS("Kills","kills","k"),
    PLAYER_KILLS("Player Kills",  "pk","players","pvp"),
    MOB_KILLS("Mob Kills",  "mk","mobs","pve"),
    
    MARKSMAN_KILLS("Marksman Kills", "marksman","rk"),
    ELYTRA_KILLS("Elytra Kills",  "elytra","ek"),
    
    ALL_HEADS("Beheadings","heads","h"),
    PLAYER_HEADS("Players Beheaded","pheads","ph"),
    MOB_HEADS("Mobs Beheaded","mheads","mh"),
    
    BLOCKS_MINED("Blocks Mined","blocks","bm"),
    ORES_MINED("Ores Mined","ores","om"),
    
    ENTITIES_SLAIN(true, "{0}s Slain", "entities","es"),
    ENTITIES_BEHEADED(true, "{0}s Beheaded", "eh","ex"),
    
    BOUNTY_POINTS("Bounty Points","bs","bk","bp","bounties");
    
    ;
    
    
    private final String displayName;
    private final boolean extended;//whether the counter type requires extended information (like entity type) to be functional
    
    private static class Lookups {
        static final HashMap<String, CounterBaseType> shortNames = new HashMap<>();
    }
    
    CounterBaseType(boolean isExtendedType, String displayName, String... shortNames){
        this.displayName=displayName;
        this.extended = isExtendedType;
        for(String shortName : shortNames){
            Lookups.shortNames.put(shortName.toUpperCase(), this);
        }
    }
    
    CounterBaseType(String displayName, String... shortNames){
        this.displayName=displayName;
        this.extended = false;
        for(String shortName : shortNames){
            Lookups.shortNames.put(shortName.toUpperCase(), this);
        }
    }
    
    public boolean isValid(){
        return this!=CounterBaseType.INVALID;
    }
    
    public boolean isExtended(){
        return extended;
    }
    public boolean isBasic(){
        return !extended;
    }
    
    public String getDisplayName(){
        if(displayName==null) return this.name();
        return displayName;
    }
      
    public static CounterBaseType fromDisplayName(String name){
        //determine the type of counter on the item
        for(CounterBaseType type : CounterBaseType.values())   //check all counters for a matching name
            if(type.getDisplayName().equals(name))     //the name of the counter matches this item
                return type;
        return null;
    }
    
    public static CounterBaseType fromShortName(String name){
        CounterBaseType result = Lookups.shortNames.get(name.toUpperCase());
        if(result!=null) return result;
        try{
            return CounterBaseType.valueOf(name.toUpperCase());
        }catch(Exception e){
            return null;
        }
    }
    
    public CounterType createType(){
        return new CounterType(this);
    }
}
