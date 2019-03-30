/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import net.md_5.bungee.api.ChatColor;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class Counter {
    private int count=0;
    private CounterType type=CounterType.INVALID;

    public String toStringFormatted(){
        return ChatColor.GREEN+getName()+": "+ChatColor.RED+getCount();
    }
    
    public String toString(){
        return getName()+": "+getCount();
    }
    
    public String getName(){
        return type.getDisplayName();
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public CounterType getType() {
        return type;
    }

    public void setType(CounterType type) {
        this.type = type;
    }
    public boolean isValid(){ return type!=CounterType.INVALID; }
    
    public Counter(CounterType type){
        this.type=type;
    }    
    public Counter(CounterType type, int count){
        this.type=type;
        this.count=count;
    }
    
    public void increment(){
        this.count++;
    }
    
    public static Counter fromLoreLine(String line){
        line = ChatColor.stripColor(line);
        String[] pieces = line.split(":");
        if(pieces.length!=2) return null;//not the right number of colons
        
        //parse the string to get the name/count
        String name = pieces[0];
        String str_count = pieces[1];
        int count;
        try{
            count = Integer.parseInt(str_count);
        }catch(NumberFormatException e){
            return null;//invalid count string - can't be a counter
        }
        
        CounterType type = CounterType.fromDisplayName(name);//match display name
        if(type==null) return null;
        return new Counter(type,count);
    }
}
