/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.bounties;

import com.github.crashdemons.lorekillcounter.LoreKillCounter;
import com.github.crashdemons.lorekillcounter.counters.CounterBaseType;
import com.github.crashdemons.lorekillcounter.counters.CounterManager;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class BountyManager {
    private final Plugin plugin;
    private final Random rand;
    public BountyManager(Plugin pl){ plugin = pl; rand = new Random(); }
    
    
 
    public static void deductBountyCounterCost(ItemStack stack, int cost){
        CounterManager.applyCounterOperation(stack, (currentCounter)->{
            if(currentCounter.getType().equals(CounterBaseType.BOUNTY_POINTS.createType())){
                int newCount = currentCounter.getCount() - cost;
                if(newCount<0) newCount = 0;
                currentCounter.setCount(newCount);
            }
            return currentCounter;
        });
    }
    
    
    public void onBountyComplete(Player holder, ItemStack stack){
        holder.sendMessage("* Bounty Complete!");
        String bounty;
        try{
            bounty = rerollBounty(holder, stack);
            if(bounty==null){
                holder.sendMessage("* Could not find a new bounty target - try /bounty later.");
                return;
            }
        }catch(NotEnoughTargetsException e){
            holder.sendMessage("* Not enough players online to select a bounty - try /bounty later.");
            return;
        }
        holder.sendMessage("* Next bounty for this item: "+bounty);
        
    }
    public String rerollBounty(Player holder, ItemStack stack){
        debug("rerollBounty"); 
        removeItemBounty(stack);
        String newBounty = getNewRandomBounty(holder);
        setItemBounty(stack,newBounty);
        return newBounty;
    }
    
    
    private static final float BOUNTY_RESET_COST_PERCENT = 0.10f;
    public int getBountyResetCost(int bounties){
        float bountyCostF = ((float) bounties) * BOUNTY_RESET_COST_PERCENT;
        return (int)bountyCostF;
    }
    
    private void debug(String s){}
    
    public static String getIP(Player player){
        if(player==null) return null;
        InetSocketAddress addr = player.getAddress();
        if(addr==null) return null;
        InetAddress addr2 = addr.getAddress();
        if(addr2==null) return null;
        return addr2.getHostAddress();
    }
    
    
    private static final float BOUNTY_MINIMUM_CHOICES = 10;
    public @NotNull String getNewRandomBounty(Player holder){
        List<? extends Player> choices = Bukkit.getOnlinePlayers().stream().filter(
                (player)->( 
                            (!holder.getUniqueId().equals(player.getUniqueId())) && 
                            (!getIP(holder).equals(getIP(player))) &&
                            holder.canSee(holder) && 
                            !player.hasPermission("lorekillcounter.bounty.exempt")
                        )
        ).collect(Collectors.toList());
        if(choices.size()<BOUNTY_MINIMUM_CHOICES) throw new NotEnoughTargetsException();
        
        if(choices.size()<1) throw new NotEnoughTargetsException();
        
        
        Player choice = choices.get(rand.nextInt(choices.size()));
        
        return choice.getName();
    }
    
    
    private NamespacedKey getItemBountyKey(){
        return new NamespacedKey(plugin,"bounty");
    }
    
    private String getItemBounty(@NotNull ItemMeta meta){
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String value = pdc.get(getItemBountyKey(), PersistentDataType.STRING);
        debug("getItemBounty: "+getItemBountyKey().toString()+" = "+(value==null?"null":value)); 
        return value;
    }
    
    private void removeItemBounty(@NotNull ItemMeta meta){
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        debug("removeItemBounty"); 
        pdc.remove(getItemBountyKey());
        
    }
    
    private void setItemBounty(@NotNull ItemMeta meta, String playerName){
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        debug("setItemBounty: "+getItemBountyKey().toString()+" = "+playerName); 
        pdc.set(getItemBountyKey(), PersistentDataType.STRING, playerName);
    }
    public @Nullable String getItemBounty(ItemStack stack){
        if(stack==null) {debug("getItemBounty: null stack"); return null;}
        if(!stack.hasItemMeta()){debug("getItemBounty: no meta");  return null; }
        ItemMeta meta = stack.getItemMeta();
        if(meta==null){debug("getItemBounty: null meta");  return null;}
        return getItemBounty(meta);
        
    }
    public void removeItemBounty(ItemStack stack){
        if(stack==null)  {debug("removeItemBounty: null stack"); return ;}
        if(!stack.hasItemMeta())  {debug("removeItemBounty: no meta"); return ;}
        ItemMeta meta = stack.getItemMeta();
        if(meta==null)  {debug("removeItemBounty: null meta"); return ;}
        removeItemBounty(meta);
        stack.setItemMeta(meta);
    }
    public boolean setItemBounty(ItemStack stack, String playerName){
        ItemMeta meta = null;
        if(stack.hasItemMeta()) meta = stack.getItemMeta();
        else meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
        if(meta==null){debug("setItemBounty: null meta"); return false;}//material does not support meta (eg: AIR)
        
        setItemBounty(meta, playerName);
        stack.setItemMeta(meta);
        return true;
    }
    
   
    
    
    

}
