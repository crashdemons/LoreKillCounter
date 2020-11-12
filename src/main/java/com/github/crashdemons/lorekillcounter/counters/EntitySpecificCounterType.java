/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.counters;

import org.bukkit.entity.EntityType;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
abstract public class EntitySpecificCounterType extends CounterType {
    
    
    public final EntityType entityType;
    
    public EntitySpecificCounterType(CounterBaseType baseType, EntityType entityType){
        super(baseType, getFriendlyEntityName(entityType));
        this.entityType = entityType;
    }
    
    
}
