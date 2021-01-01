/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.counters;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class BlockBrokenCounterType extends MaterialSpecificCounterType {
    
    private final static HashMap<String,Material> displayNameToBlock = new HashMap<String,Material>();
    static{
        for(Material type : Material.values()){
            if(!type.isBlock()) continue;
            String dispName = formatDisplayName(CounterBaseType.BLOCKS_BROKEN,getFriendlyMaterialName(type));
            displayNameToBlock.put(dispName.toUpperCase(), type);
        }
    }
    public BlockBrokenCounterType(Material blockType){
        super(CounterBaseType.BLOCKS_BROKEN, blockType);
        if(!blockType.isBlock()) throw new IllegalArgumentException("Type is not a block");
    }
    @Nullable
    public static BlockBrokenCounterType fromDisplayName(String displayName){
        Material type = displayNameToBlock.get(displayName.toUpperCase());
        if(type==null) return null;
        return new BlockBrokenCounterType(type);
    }
    
    @Nullable
    public static BlockBrokenCounterType fromBlockName(String matName){
        Material type = null;
        try{
            type = Material.valueOf(matName.toUpperCase());
        }catch(Exception ex){
            //
        }
        if(type==null) return null;
        if(!type.isBlock()) return null;
        return new BlockBrokenCounterType(type);
    }
}
