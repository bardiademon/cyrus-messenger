package com.bardiademon.CyrusMessenger.bardiademon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class CyrusJSON extends JSONObject
{
    public JSONObject put (final Enum <?> key , final boolean value) throws JSONException
    {
        return super.put (key.name () , value);
    }

    public JSONObject put (final Enum <?> key , final Collection <?> value) throws JSONException
    {
        return super.put (key.name () , value);
    }

    public JSONObject put (final Enum <?> key , final double value) throws JSONException
    {
        return super.put (key.name () , value);
    }

    public JSONObject put (final Enum <?> key , final float value) throws JSONException
    {
        return super.put (key.name () , value);
    }

    public JSONObject put (final Enum <?> key , final int value) throws JSONException
    {
        return super.put (key.name () , value);
    }

    public JSONObject put (final Enum <?> key , final long value) throws JSONException
    {
        return super.put (key.name () , value);
    }

    public JSONObject put (final Enum <?> key , final Map <?, ?> value) throws JSONException
    {
        return super.put (key.name () , value);
    }

    public JSONObject put (final Enum <?> key , final Object value) throws JSONException
    {
        return super.put (key.name () , value);
    }


    public Object get (final Enum <?> key) throws JSONException
    {
        return super.get (key.name ());
    }

    public <E extends Enum <E>> E getEnum (Class <E> clazz , Enum <?> key) throws JSONException
    {
        return super.getEnum (clazz , key.name ());
    }


    public boolean getBoolean (final Enum <?> key) throws JSONException
    {
        return super.getBoolean (key.name ());
    }


    public BigInteger getBigInteger (final Enum <?> key) throws JSONException
    {
        return super.getBigInteger (key.name ());
    }

    public BigDecimal getBigDecimal (final Enum <?> key) throws JSONException
    {
        return super.getBigDecimal (key.name ());
    }

    public double getDouble (final Enum <?> key) throws JSONException
    {
        return super.getDouble (key.name ());
    }

    public float getFloat (final Enum <?> key) throws JSONException
    {
        return super.getFloat (key.name ());
    }

    public Number getNumber (final Enum <?> key) throws JSONException
    {
        return super.getNumber (key.name ());
    }

    public int getInt (final Enum <?> key) throws JSONException
    {
        return super.getInt (key.name ());
    }

    public JSONArray getJSONArray (final Enum <?> key) throws JSONException
    {
        return super.getJSONArray (key.name ());
    }

    public JSONObject getJSONObject (final Enum <?> key) throws JSONException
    {
        return super.getJSONObject (key.name ());
    }

    public long getLong (final Enum <?> key) throws JSONException
    {
        return super.getLong (key.name ());
    }

    public String getString (final Enum <?> key) throws JSONException
    {
        return super.getString (key.name ());
    }

    public boolean has (final Enum <?> key)
    {
        return super.has (key.name ());
    }

    public JSONObject increment (final Enum <?> key) throws JSONException
    {
        return super.increment (key.name ());
    }

    public boolean isNull (final Enum <?> key)
    {
        return super.isNull (key.name ());
    }
}
