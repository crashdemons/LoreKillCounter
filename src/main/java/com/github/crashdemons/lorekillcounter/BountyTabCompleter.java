/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class BountyTabCompleter implements TabCompleter {
    public List<String> onTabComplete​(CommandSender sender, Command command, String alias, String[] args){
        if(!command.getName().equalsIgnoreCase("LoreKillCounterBounty")) return null;
        
        int numToComplete = args.length;//semantics - number of argument to complete starting at 1.
        
        if(numToComplete == 1) return Arrays.asList("reroll");
        return new ArrayList<>();
    }
    
}
