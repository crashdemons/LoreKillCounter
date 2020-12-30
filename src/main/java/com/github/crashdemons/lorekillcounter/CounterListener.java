/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import com.github.crashdemons.lorekillcounter.counters.CounterManager;
import com.github.crashdemons.lorekillcounter.counters.CounterType;
import com.github.crashdemons.lorekillcounter.integrations.MiningTrophiesSupport;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CounterListener implements Listener {

    
    @EventHandler(ignoreCancelled=true)
    public void onBlockBreakEvent(BlockBreakEvent event){
        if(MiningTrophiesSupport.isSimulatedBlockBreak(event)) return;
        
        Block block = event.getBlock();
        if(block==null) return;
        List<CounterType> breakTypes = CounterManager.typeFromBlockBreak(block);
        if(breakTypes.isEmpty()) return;
        Player player = event.getPlayer();
        if(player==null) return;
        
        
        CounterManager.incrementMatchingCounters(player, breakTypes);
        
    }
    @EventHandler(ignoreCancelled=true)
    public void onEntityDeathEvent(EntityDeathEvent event){
        
        //getLogger().info("LKC death event ");
        LivingEntity killed = event.getEntity();
        Player killer = event.getEntity().getKiller();
        if(killer==null) return;
        //getLogger().info(" killer exists");
        if(!(killer instanceof Player)) return;
        if(!killer.hasPermission("lorekillcounter.counted")) return;
        //getLogger().info(" killer player");
        
        List<CounterType> deathTypes = CounterManager.typeFromEntityDeath(killed);
        
        
        /*ItemStack stack = killer.getInventory().getItemInMainHand();
        deathTypes.addAll(CounterManager.typeFromBountyDeath(killer,stack,killed));*/
        
        
        //getLogger().info("deathType = "+deathType);
        
        if(deathTypes.isEmpty()) return;
        
        if((killer instanceof Player) && (killed instanceof Player)){
            if(killer.getUniqueId().equals(killed.getUniqueId())) return;//don't allow suicides to increase the PK counter
        }
        
        
       // getLogger().info(" counter valid");
        
        CounterManager.incrementMatchingCounters(killer, deathTypes);
        
        /*
        //update relevant counters
        for(int i=0;i<lore.size();i++){
            Counter counter = Counter.fromLoreLine(lore.get(i));
            if(counter!=null && counter.isValid()){//lore line is a valid counter
                if(counter.getType()==deathType){//the lore line is the same type of counter as this kill
                    counter.increment();
                    lore.set(i, counter.toStringFormatted());
                }
            }
        }
        */
        
        
        
    }
}
