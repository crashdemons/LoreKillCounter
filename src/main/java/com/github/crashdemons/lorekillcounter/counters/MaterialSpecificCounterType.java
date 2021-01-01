/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.counters;

import org.bukkit.Material;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class MaterialSpecificCounterType extends CounterType {
    
    
    public final Material matType;
    
    public MaterialSpecificCounterType(CounterBaseType baseType, Material matType){
        super(baseType, getFriendlyMaterialName(matType));
        this.matType = matType;
    }
    
    
}
