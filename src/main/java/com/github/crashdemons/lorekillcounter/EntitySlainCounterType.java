/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

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
    
    private static String getFriendlyEntityName(EntityType type){
        String name = type.name().replace('_', ' ');
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
    
    @Nullable
    public static EntitySlainCounterType fromDisplayName(String displayName){
        EntityType type = displayNameToEntity.get(displayName.toUpperCase());
        if(type==null) return null;
        return new EntitySlainCounterType(type);
    }
}
