/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.integrations;

import org.bukkit.Bukkit;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class PlayerHeadsSupport {
    private PlayerHeadsSupport(){}
    
    private static boolean hasChecked = false;
    private static boolean PHEnabled=false;
    
    private static boolean classExists(String name){
        try {
            Class.forName( name );
            return true;
        } catch( ClassNotFoundException e ) {
            return false;
        }
    }
    public static boolean isPresent(){
        if(hasChecked) return PHEnabled;
        if(Bukkit.getServer().getPluginManager().getPlugin("PlayerHeads") != null){
            PHEnabled = classExists("org.shininet.bukkit.playerheads.events.MobDropHeadEvent");
        }
        hasChecked=true;
        return PHEnabled;
    }
}
