/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import com.github.crashdemons.lorekillcounter.counters.EntitySlainCounterType;
import com.github.crashdemons.lorekillcounter.counters.CounterBaseType;
import com.github.crashdemons.lorekillcounter.counters.Counter;
import com.github.crashdemons.lorekillcounter.counters.CounterType;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CounterTest {
    
    public CounterTest() {
    }

    @Test
    public void testToStringFormatted() {
        System.out.println("toStringFormatted");
        Counter instance = new Counter(CounterBaseType.PLAYER_KILLS);
        String expResult = "Player Kills: 0";
        String result = ChatColor.stripColor(instance.toStringFormatted());
        assertEquals(expResult, result);
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        Counter instance = new Counter(CounterBaseType.PLAYER_KILLS);
        String expResult = "Player Kills: 0";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetName() {
        System.out.println("getName");
        Counter instance = new Counter(CounterBaseType.PLAYER_KILLS);
        String expResult = "Player Kills";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCount() {
        System.out.println("getCount");
        Counter instance = new Counter(CounterBaseType.PLAYER_KILLS);
        int expResult = 0;
        int result = instance.getCount();
        assertEquals(expResult, result);
    }

    @Test
    public void testSetCount() {
        System.out.println("setCount");
        int count = 0;
        Counter instance = new Counter(CounterBaseType.PLAYER_KILLS);
        instance.setCount(count);
    }

    @Test
    public void testGetType() {
        System.out.println("getType");
        Counter instance = new Counter(CounterBaseType.PLAYER_KILLS);
        CounterBaseType expResult = CounterBaseType.PLAYER_KILLS;
        CounterBaseType result = instance.getType().baseType;
        assertEquals(expResult, result);
    }


    @Test
    public void testIsValid() {
        System.out.println("isValid");
        Counter instance = new Counter(CounterBaseType.PLAYER_KILLS);
        boolean expResult = true;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
    }

    @Test
    public void testIncrement() {
        System.out.println("increment");
        Counter instance = new Counter(CounterBaseType.PLAYER_KILLS);
        instance.increment();
        
        assertEquals(1, instance.getCount());
    }

    @Test
    public void testFromLoreLine() {
        System.out.println("fromLoreLine");
        String line = ChatColor.GREEN+"Player Kills: "+ChatColor.RED+"7";
        Counter result = Counter.fromLoreLine(line);
        assertNotNull(result);
        assertEquals(CounterBaseType.PLAYER_KILLS, result.getType().baseType);
        assertEquals(7, result.getCount());
    }
    
    
    @Test
    public void testFromLoreLineExtended() {
        System.out.println("fromLoreLine extended");
        String line = ChatColor.GREEN+"Withers Slain: "+ChatColor.RED+"7";
        Counter result = Counter.fromLoreLine(line);
        assertNotNull(result);
        assertEquals(CounterBaseType.ENTITIES_SLAIN, result.getType().baseType);
        assertEquals(7, result.getCount());
        
        CounterType type = result.getType();
        if(!(type instanceof EntitySlainCounterType)) fail("countertype is not EntitySlainCounterType");
        EntitySlainCounterType slainType = (EntitySlainCounterType) type;
        
        assertEquals(slainType.entityType,EntityType.WITHER);
    }
    
    @Test
    public void testFromLoreLineExtended2() {
        System.out.println("fromLoreLine extended2");
        String line = ChatColor.GREEN+"Trader Llamas Slain: "+ChatColor.RED+"7";
        Counter result = Counter.fromLoreLine(line);
        assertNotNull(result);
        assertEquals(CounterBaseType.ENTITIES_SLAIN, result.getType().baseType);
        assertEquals(7, result.getCount());
        
        CounterType type = result.getType();
        if(!(type instanceof EntitySlainCounterType)) fail("countertype is not EntitySlainCounterType");
        EntitySlainCounterType slainType = (EntitySlainCounterType) type;
        
        assertEquals(slainType.entityType,EntityType.TRADER_LLAMA);
    }
    
    
    @Test
    public void testCounterTypeEquality() {
        System.out.println("counterTypeEquality");
        
        assertEquals(CounterBaseType.PLAYER_KILLS.createType(),new CounterType(CounterBaseType.PLAYER_KILLS));
        assertEquals(new CounterType(CounterBaseType.PLAYER_KILLS),CounterBaseType.PLAYER_KILLS.createType());
        
        assertEquals(CounterBaseType.ENTITIES_SLAIN.createType(),new CounterType(CounterBaseType.ENTITIES_SLAIN));
        assertFalse(CounterBaseType.ENTITIES_SLAIN.createType().equals( new CounterType(CounterBaseType.ENTITIES_SLAIN,"X")));
        assertFalse((new CounterType(CounterBaseType.ENTITIES_SLAIN,"X")).equals(CounterBaseType.ENTITIES_SLAIN.createType()));
        
        CounterType a = new CounterType(CounterBaseType.ENTITIES_SLAIN,"Trader Llama");
        CounterType b = new EntitySlainCounterType(EntityType.TRADER_LLAMA);
        
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        
        
        assertEquals(a,b);
    }
    
    
    @Test
    public void testDisplayName() {
        CounterType type = new EntitySlainCounterType(EntityType.TRADER_LLAMA);
        assertEquals("Trader Llamas Slain",type.getDisplayName());
        
        type = new CounterType(CounterBaseType.ENTITIES_SLAIN,"xYz AbC");
        assertEquals("xYz AbCs Slain",type.getDisplayName());
        
        type = CounterBaseType.ORES_MINED.createType();
        assertEquals("Ores Mined",type.getDisplayName());
    }
}
