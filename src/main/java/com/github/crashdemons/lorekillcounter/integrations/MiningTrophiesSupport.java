/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.integrations;

import com.github.crashdemons.miningtrophies.events.SimulatedBlockBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class MiningTrophiesSupport {
    private MiningTrophiesSupport(){}
    public static boolean isPresent(){
        return Bukkit.getPluginManager().getPlugin("MiningTrophies")!=null;
    }
    public static boolean isSimulatedBlockBreak(BlockBreakEvent event){
        if(!isPresent()) return false;
        return (event instanceof SimulatedBlockBreakEvent);
    }
}
