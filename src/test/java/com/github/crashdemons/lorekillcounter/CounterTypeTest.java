/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import com.github.crashdemons.lorekillcounter.counters.CounterBaseType;
import org.bukkit.entity.Entity;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CounterTypeTest {
    
    public CounterTypeTest() {
    }

    @Test
    public void testGetDisplayName() {
        System.out.println("getDisplayName");
        CounterBaseType instance = CounterBaseType.PLAYER_KILLS;
        String expResult = "Player Kills";
        String result = instance.getDisplayName();
        assertEquals(expResult, result);
    }

    @Test
    public void testFromDisplayName() {
        System.out.println("fromDisplayName");
        String name = "Player Kills";
        CounterBaseType expResult = CounterBaseType.PLAYER_KILLS;
        CounterBaseType result = CounterBaseType.fromDisplayName(name);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testFromDisplayNameInvalid() {
        System.out.println("fromDisplayName Invalid");
        String name = "asdasd";
        CounterBaseType expResult = null;
        CounterBaseType result = CounterBaseType.fromDisplayName(name);
        assertEquals(expResult, result);
    }

    /*
    @Test
    public void testFromEntityDeath() {
        System.out.println("fromEntityDeath");
        Entity e = null;
        CounterType expResult = null;
        CounterType result = CounterType.fromEntityDeath(e);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    
}
