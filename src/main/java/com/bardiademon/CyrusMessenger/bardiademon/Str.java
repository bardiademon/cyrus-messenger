package com.bardiademon.CyrusMessenger.bardiademon;

import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;

public final class Str
{
    public static boolean IsEmpty (Object str)
    {
        return (str == null || (String.valueOf (str)).isEmpty ());
    }

    public static boolean HasBool (String str)
    {
        try
        {
            if (IsEmpty (str)) throw new Exception ("Is empty");
            else return (str.equals ("true") || str.equals ("false"));
        }
        catch (Exception e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e);
            return false;
        }
    }

    public static Boolean ToBool (String str)
    {
        try
        {
            if (HasBool (str)) return Boolean.parseBoolean (str);
            else throw new Exception (str + " > not bool");
        }
        catch (Exception e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e);
            return null;
        }
    }

    public static boolean isNumber (Object obj)
    {
        if (obj instanceof Integer || obj instanceof Long || obj instanceof Short || obj instanceof Double || obj instanceof Float)
            return true;
        else return (String.valueOf (obj)).matches ("[0-9]*");
    }

    public static boolean RealBool (String str)
    {
        Boolean bool = ToBool (str);
        if (bool != null) return bool;
        else return false;
    }

    public static String[] toArray (String... value)
    {
        return value;
    }


}
