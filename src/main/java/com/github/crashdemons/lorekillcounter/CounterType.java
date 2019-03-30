/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

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
    PLAYER_KILLS("Player Kills"),
    MOB_KILLS("Mob Kills");
    
    private String displayName=null;
    
    CounterType(String displayName){
        this.displayName=displayName;
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
    
    public static CounterType fromEntityDeath(Entity e){
        if(e instanceof Player) return PLAYER_KILLS;
        if(!(e instanceof LivingEntity)) return null;
        if(e instanceof ArmorStand) return null;
        return MOB_KILLS;
    }
}
