/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.counters;

import java.util.HashMap;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class EntitySlainCounterType extends CounterType {
    
    private final static HashMap<String,EntityType> displayNameToEntity = new HashMap<String,EntityType>();
    static{
        for(EntityType type : EntityType.values()){
            String dispName = formatDisplayName(CounterBaseType.ENTITIES_SLAIN,getFriendlyEntityName(type));
            displayNameToEntity.put(dispName.toUpperCase(), type);
        }
    }
    
    public final EntityType entityType;
    
    public EntitySlainCounterType(EntityType type){
        super(CounterBaseType.ENTITIES_SLAIN, getFriendlyEntityName(type));
        entityType = type;
    }
    
    
    @Nullable
    public static EntitySlainCounterType fromDisplayName(String displayName){
        EntityType type = displayNameToEntity.get(displayName.toUpperCase());
        if(type==null) return null;
        return new EntitySlainCounterType(type);
    }
    
    @Nullable
    public static EntitySlainCounterType fromEntityName(String entityName){
        EntityType type = null;
        try{
            type = EntityType.valueOf(entityName.toUpperCase());
        }catch(Exception ex){
            //
        }
        if(type==null) return null;
        if(!type.isAlive()) return null;
        return new EntitySlainCounterType(type);
    }
}
