package com.bardiademon.CyrusMessenger.Model.Database.Default;

public enum DefaultType
{
    // lng => Long , dbl => double , flt => float
    integer, lng, string, dbl, flt;

    public static DefaultType to (String val)
    {
        try
        {
            return valueOf (val);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
