/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.lorekillcounter;

import com.sun.istack.internal.Nullable;
import java.text.MessageFormat;
import java.util.Objects;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class CounterType {
    public final CounterBaseType baseType;
    public final String extendedData;
    
    public CounterType(CounterBaseType type){
        this.baseType=type;
        this.extendedData="";
    }
    
    public CounterType(CounterBaseType type, String extendedData){
        this.baseType=type;
        this.extendedData=extendedData;
    }
    
    protected static String formatDisplayName(CounterBaseType baseType, String extendedData){
        if(!baseType.isExtended()) return baseType.getDisplayName();
        return MessageFormat.format(baseType.getDisplayName(), extendedData);
    }
    
    public String getDisplayName(){
        return formatDisplayName(baseType, extendedData);
    }
    
    @Nullable
    public static CounterType fromDisplayName(String displayName){
        return new CounterType(CounterBaseType.fromDisplayName(displayName));
    }
    
    
    @Override
    public boolean equals(Object other){
        if(!(other instanceof CounterType)) return false;
        if(other==this) return true;
        if(other==null) return false;
        CounterType otherType = (CounterType) other;
        return (this.baseType==otherType.baseType && this.extendedData.equals(otherType.extendedData));
    }

    @Override
    public int hashCode() {//generated by netbeans
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.baseType);
        hash = 79 * hash + Objects.hashCode(this.extendedData);
        return hash;
    }

    
}
