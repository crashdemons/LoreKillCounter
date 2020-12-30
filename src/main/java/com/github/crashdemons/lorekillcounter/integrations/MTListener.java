/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.integrations;

import com.github.crashdemons.lorekillcounter.LoreKillCounter;
import com.github.crashdemons.lorekillcounter.counters.CounterManager;
import com.github.crashdemons.lorekillcounter.counters.CounterType;
import com.github.crashdemons.miningtrophies.events.BlockDropTrophyEvent;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class MTListener implements Listener{
    private final LoreKillCounter plugin;
    public MTListener(LoreKillCounter plugin){
        this.plugin=plugin;
    }
    
    @EventHandler
    public void onMiningTrophyDrop(BlockDropTrophyEvent evt){
        if(!MiningTrophiesSupport.isPresent()) return;
        if(!PlayerHeadsSupport.isPresent()) return;
        if(!plugin.isEnabled()) return;
        
        if(evt.getPlayer()==null) return;
        
        List<CounterType> counterTypes = CounterManager.typeFromBlockTrophyDrop(evt.getBlock());
        if(counterTypes.isEmpty()) return;
        //if(counterTypes==null || counterTypes.baseType==CounterBaseType.INVALID) return;
        
        
        
        CounterManager.incrementMatchingCounters(evt.getPlayer(), counterTypes);
    }
}
