/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import com.github.crashdemons.lorekillcounter.bounties.BountyManager;
import com.github.crashdemons.lorekillcounter.bounties.NotEnoughTargetsException;
import com.github.crashdemons.lorekillcounter.counters.Counter;
import com.github.crashdemons.lorekillcounter.counters.CounterBaseType;
import com.github.crashdemons.lorekillcounter.counters.CounterManager;
import com.github.crashdemons.lorekillcounter.counters.CounterType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class BountyCommandExecutor implements CommandExecutor  {
    
    
    
    
    private boolean onBountyInfo(CommandSender sender){
        //show bounty info
        if(!(sender instanceof Player)){
            sender.sendMessage("Error: Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        ItemStack stack = player.getInventory().getItemInMainHand();
        Counter counter = CounterManager.getFirstCounter(stack, new CounterType(CounterBaseType.BOUNTY_POINTS));
        boolean hasCounter = (counter!=null);
        
        //NOTE: TODO - check if item has a bounty counter!!!! return if not
        if(!hasCounter){
            sender.sendMessage(ChatColor.RED+"* This item does not have a "+CounterBaseType.BOUNTY_POINTS.getDisplayName()+" counter.");
            return true;
        }
        
        try{
            String bounty = LoreKillCounter.instance.bountyManager.getItemBounty(stack);
            boolean rerolled = false;
            if(bounty==null){ bounty = LoreKillCounter.instance.bountyManager.rerollBounty(player, stack); rerolled = true; }
            if(bounty==null){
                sender.sendMessage("* Failed to choose a bounty for this item - try /bounty again later.");
            }else{
                sender.sendMessage("* This item"+(rerolled?" now":"")+" has a bounty for: "+bounty+". "+ (
                        sender.hasPermission("lorekillcounter.bounty.reset")?"Reroll cost is "+LoreKillCounter.instance.bountyManager.getBountyResetCost(counter.getCount())+" points.":""
                ));
            }
        }catch(NotEnoughTargetsException e){
            sender.sendMessage("* Not enough players online to choose a bounty for this item - try /bounty again later.");
        }
        
        return true;
    }
    private boolean onBountyReset(CommandSender sender, String firstArg){
        if(!firstArg.equalsIgnoreCase("reroll")) return false;
        if(!sender.hasPermission("lorekillcounter.bounty.reset")){
            sender.sendMessage("You do not have permission reset bounties.");
            return true;
        }
        
        if(!(sender instanceof Player)){
            sender.sendMessage("Error: Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        ItemStack stack = player.getInventory().getItemInMainHand();
        CounterType bountyType = new CounterType(CounterBaseType.BOUNTY_POINTS);
        Counter counter = CounterManager.getFirstCounter(stack, bountyType);
        boolean hasCounter = (counter!=null);
        
        //NOTE: TODO - check if item has a bounty counter!!!! return if not
        if(!hasCounter){
            sender.sendMessage(ChatColor.RED+"* This item does not have a "+CounterBaseType.BOUNTY_POINTS.getDisplayName()+" counter.");
            return true;
        }
        
        int cost = LoreKillCounter.instance.bountyManager.getBountyResetCost(counter.getCount());

        try{
            String bounty;// = LoreKillCounter.instance.bountyManager.getItemBounty(stack);
            bounty = LoreKillCounter.instance.bountyManager.rerollBounty(player, stack);
            if(bounty==null){
                sender.sendMessage("* Cleared bounty target for this item (cost: "+cost+" points) - see /bounty");
            }else{
                sender.sendMessage("* Rerolled bounty target for this item (cost: "+cost+" points) - target is: "+bounty+".");
            }
        }catch(NotEnoughTargetsException e){
            sender.sendMessage("* Cleared bounty target for this item (cost: "+cost+" points) - see /bounty.");
        }
        
        BountyManager.deductBountyCounterCost(stack, cost);
        
        
        
        
        //reset bounty
        return true;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("LoreKillCounterBounty")) {
            return false;
        }
        if(!sender.hasPermission("lorekillcounter.bounty.command")){
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }
        
        if(args.length>1) return false;//only 0 or 1 parameter allowed;
        if(args.length==1) return onBountyReset(sender, args[0]);
        return onBountyInfo(sender);
    }
}
