/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import org.bukkit.ChatColor;
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
        assertEquals(CounterBaseType.PLAYER_KILLS, result.getType());
        assertEquals(7, result.getCount());
    }
    
}
