/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter.counters;

import org.bukkit.ChatColor;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class Counter {
    private int count=0;
    //private CounterType type=CounterType.INVALID;
    private final CounterType type;

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

   // public void setType(CounterType type) {
   //     this.type = type;
   // }
    public boolean isValid(){ return type.baseType!=CounterBaseType.INVALID; }
   
    
    public Counter(CounterBaseType type){
        this.type = new CounterType(type);
    }    
    public Counter(CounterBaseType type, int count){
        this.type = new CounterType(type);
        this.count=count;
    }
    
    public Counter(CounterType type){
        this.type = type;
    }
    public Counter(CounterType type, int count){
        this.type = type;
        this.count=count;
    }
    /*public Counter(CounterType type, String extendedData, int count){
        this.type = new ExtendedCounterType(type, extendedData);
        this.count=count;
    }*/
    
    public void increment(){
        this.count++;
    }
    
    public static Counter fromLoreLine(String line){
        line = ChatColor.stripColor(line);
        String[] pieces = line.split(": ");
        //System.out.println(pieces.length);
        //System.out.println(pieces[0]);
        if(pieces.length!=2) return null;//not the right number of colons
        
        //parse the string to get the name/count
        String name = pieces[0];
        String str_count = pieces[1];
        int count;
        try{
            count = Integer.parseInt(str_count);
        }catch(NumberFormatException e){
            //System.out.println("Invalid integer value "+str_count);
            return null;//invalid count string - can't be a counter
        }
        
        EntityBeheadCounterType beheadCounter = EntityBeheadCounterType.fromDisplayName(name);
        if(beheadCounter!=null) return new Counter(beheadCounter, count);
        EntitySlainCounterType slainCounter = EntitySlainCounterType.fromDisplayName(name);
        if(slainCounter!=null) return new Counter(slainCounter, count);
        
        BlockBrokenCounterType blockCounter = BlockBrokenCounterType.fromDisplayName(name);
        if(blockCounter!=null) return new Counter(blockCounter, count);
        
        
        CounterType counterType = CounterType.fromDisplayName(name);
        if(counterType!=null) return new Counter(counterType, count);
        return null;
    }
}
