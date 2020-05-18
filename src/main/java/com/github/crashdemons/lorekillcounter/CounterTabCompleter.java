/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import com.github.crashdemons.lorekillcounter.counters.CounterBaseType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CounterTabCompleter implements TabCompleter{
    public List<String> onTabComplete​(CommandSender sender, Command command, String alias, String[] args){
        if(!command.getName().equalsIgnoreCase("LoreKillCounter")) return null;
        
        
        if(args.length == 1) return Arrays.asList("add","clear","addextended","addex");
        
        
        //List<String> argList = Stream.of(args).map(String::toLowerCase).collect(Collectors.toList());
        if(args.length >= 2){//counter add ...
            if(args[0].equalsIgnoreCase("add")){
                if(args.length==2) return Stream.of( CounterBaseType.values() ).filter(CounterBaseType::isValid).filter(CounterBaseType::isBasic).map(CounterBaseType::name).collect(Collectors.toList());
            }else if(args[0].equalsIgnoreCase("addextended") || args[0].equalsIgnoreCase("addex")){
                if(args.length==2) return Stream.of( CounterBaseType.values() ).filter(CounterBaseType::isExtended).map(CounterBaseType::name).collect(Collectors.toList());
                if(args.length==3) return Stream.of( EntityType.values() ).filter(EntityType::isAlive).map(EntityType::name).collect(Collectors.toList());
            }else if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("remove")){
                return new ArrayList<>();
            }
            
        }
        //unsupported argument length
        return new ArrayList<>();
        
    }
}
