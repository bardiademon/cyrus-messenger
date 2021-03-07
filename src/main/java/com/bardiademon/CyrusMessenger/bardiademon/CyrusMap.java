package com.bardiademon.CyrusMessenger.bardiademon;

import java.util.LinkedHashMap;

public final class CyrusMap<K, V> extends LinkedHashMap <K, V>
{
    public V get (Enum <?> key)
    {
        return super.get (key.name ());
    }

    public V getOrDefault (Enum <?> key , V defaultValue)
    {
        return super.getOrDefault (key.name () , defaultValue);
    }

    /*
     * just string
     */
    public V put (Enum <?> key , V value)
    {
        return super.put ((K) key.name () , value);
    }
}
