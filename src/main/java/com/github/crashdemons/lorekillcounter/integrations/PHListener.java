/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.lorekillcounter.integrations;


import com.github.crashdemons.lorekillcounter.LoreKillCounter;
import com.github.crashdemons.lorekillcounter.counters.CounterType;
import com.github.crashdemons.lorekillcounter.counters.CounterManager;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.shininet.bukkit.playerheads.events.LivingEntityDropHeadEvent;
import org.shininet.bukkit.playerheads.events.VanillaLivingEntityDropHeadEvent;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class PHListener implements Listener{
    private final LoreKillCounter plugin;
    public PHListener(LoreKillCounter plugin){
        this.plugin=plugin;
    }
    
    /*
    //Unused vanilla head drop detection - BUT we can't tell when it also triggers a PH drop.
    @EventHandler(ignoreCancelled=true)
    public void onEntityDeathEvent(EntityDeathEvent event){
        if(!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity beheadee = (LivingEntity) event.getEntity();
        Player beheader = beheadee.getKiller();
        
        if(beheader==null) return;
        if((beheader instanceof Player) && (beheadee instanceof Player)){
            if(beheader.getUniqueId().equals(beheadee.getUniqueId())) return;//don't allow suicides to increase the PH counter
        }
        List<CounterType> counterTypes = CounterManager.typeFromEntityHeadDrop(beheadee);
        if(counterTypes.isEmpty()) return;
        
        CounterManager.incrementMatchingCounters(beheader, counterTypes);
    }*/
    
    @EventHandler(ignoreCancelled=true)
    public void onVanillaLivingEntityDropHeadEvent(VanillaLivingEntityDropHeadEvent event){
        if(!PlayerHeadsSupport.isPresent()) return;
        if(!plugin.isEnabled()) return;
        //if(!(event instanceof VanillaLivingEntityDropHeadEvent)) return;
        VanillaLivingEntityDropHeadEvent beheading = event;//(LivingEntityDropHeadEvent) event;
        LivingEntity beheadee = beheading.getEntity();
        Location loc = beheadee.getLocation();
        LivingEntity killerEntity = beheading.getKillerEntity();
        Player beheader = killerEntity instanceof Player ? (Player)killerEntity : beheadee.getKiller();
        
        if(beheader==null) return;
        /*if((beheader instanceof Player) && (beheadee instanceof Player)){
            if(beheader.getUniqueId().equals(beheadee.getUniqueId())) return;//don't allow suicides to increase the PH counter
        }*/
        List<CounterType> counterTypes = CounterManager.typeFromEntityHeadDrop(beheadee);
        if(counterTypes.isEmpty()) return;
        //if(counterTypes==null || counterTypes.baseType==CounterBaseType.INVALID) return;
        
        
        
        CounterManager.incrementMatchingCounters(beheader, counterTypes);
        
        
        
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onLivingEntityDropHeadEvent(LivingEntityDropHeadEvent event){
        if(!PlayerHeadsSupport.isPresent()) return;
        if(!plugin.isEnabled()) return;
        //if(!(event instanceof LivingEntityDropHeadEvent)) return;
        LivingEntityDropHeadEvent beheading = event;//(LivingEntityDropHeadEvent) event;
        LivingEntity beheadee = beheading.getEntity();
        Location loc = beheadee.getLocation();
        LivingEntity killerEntity = beheading.getKillerEntity();
        Player beheader = killerEntity instanceof Player ? (Player)killerEntity : beheadee.getKiller();
        
        if(beheader==null) return;
        if((beheader instanceof Player) && (beheadee instanceof Player)){
            if(beheader.getUniqueId().equals(beheadee.getUniqueId())) return;//don't allow suicides to increase the PH counter
        }
        List<CounterType> counterTypes = CounterManager.typeFromEntityHeadDrop(beheadee);
        if(counterTypes.isEmpty()) return;
        //if(counterTypes==null || counterTypes.baseType==CounterBaseType.INVALID) return;
        
        
        
        CounterManager.incrementMatchingCounters(beheader, counterTypes);
        
        
        
    }
}
