package com.bardiademon.CyrusMessenger.bardiademon;

public abstract class GetSize
{
    public static String Get (long Byte)
    {
        float kb = (float) Byte / 1024;
        System.out.println (kb);
        if (kb >= 1024)
        {
            float mb = (kb / 1024);
            if (mb >= 1024)
            {
                float gb = mb / 1024;
                return String.format ("%s GB" , toString (gb));
            }
            else
            {
                return String.format ("%s MB" , toString (mb));
            }
        }
        else return String.format ("%s KB" , toString (kb));
    }

    private static String toString (double size)
    {
        return String.format ("%.2f" , Math.abs (size));
    }
}
