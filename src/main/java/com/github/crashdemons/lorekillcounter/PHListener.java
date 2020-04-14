/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.crashdemons.lorekillcounter;


import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.shininet.bukkit.playerheads.events.LivingEntityDropHeadEvent;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class PHListener implements Listener{
    private final LoreKillCounter plugin;
    PHListener(LoreKillCounter plugin){
        this.plugin=plugin;
    }
    
    @EventHandler
    public void onLivingEntityDropHeadEvent(LivingEntityDropHeadEvent event){
        if(!PlayerHeadsSupport.isPresent()) return;
        if(!plugin.isEnabled()) return;
        if(!(event instanceof LivingEntityDropHeadEvent)) return;
        LivingEntityDropHeadEvent beheading = (LivingEntityDropHeadEvent) event;
        LivingEntity beheadee = beheading.getEntity();
        Location loc = beheadee.getLocation();
        Player beheader = beheadee.getKiller();
        
        if(beheader==null) return;
        CounterType counterType = CounterType.fromEntityHeadDrop(beheadee);
        if(counterType==null || counterType==CounterType.INVALID) return;
        
        
        if((beheader instanceof Player) && (beheadee instanceof Player)){
            if(beheader.getUniqueId().equals(beheadee.getUniqueId())) return;//don't allow suicides to increase the PH counter
        }
        
        CounterManager.incrementMatchingCounter(beheader, counterType);
        
        
        
    }
}
