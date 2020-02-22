package com.bardiademon.CyrusMessenger.bardiademon;

public final class Str
{
    public static boolean IsEmpty (String str)
    {
        return (str == null || str.isEmpty ());
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
            return false;
        }
    }

    public static Boolean ToBool (String str)
    {
        try
        {
            if (HasBool (str)) return Boolean.parseBoolean (str);
            else return null;
        }
        catch (Exception e)
        {
            return null;
        }
    }

}
