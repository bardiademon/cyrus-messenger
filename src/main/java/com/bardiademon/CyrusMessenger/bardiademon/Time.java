package com.bardiademon.CyrusMessenger.bardiademon;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Time
{
    private Time ()
    {
    }

    private static final int MIN_TIMESTAMP = 60000;

    public static LocalDateTime now ()
    {
        return LocalDateTime.now ();
    }

    public static String toString (LocalDateTime time)
    {
        return toString (time , "yyyy-MM-dd HH:mm:ss");
    }

    public static String toString (LocalDateTime time , String format)
    {
        if (time == null) return null;
        return time.format (DateTimeFormatter.ofPattern (format));
    }

    public static String getTime (LocalDateTime time)
    {
        return toString (time , "HH:mm:ss");
    }

    public static Timestamp timestamp (LocalDateTime time)
    {
        if (time == null) return null;
        else return Timestamp.valueOf (time);
    }

    public static Timestamp timestamp (long time)
    {
        return new Timestamp (time);
    }

    public static LocalDateTime localDateTime (long time)
    {
        return toLocalDateTime (timestamp (time));
    }

    public static LocalDateTime toLocalDateTime (Timestamp timestamp)
    {
        return timestamp.toLocalDateTime ();
    }

    public static boolean BiggerNow (LocalDateTime dateTime)
    {
        return ((Timestamp.valueOf (LocalDateTime.now ())).getTime () > (Timestamp.valueOf (dateTime)).getTime ());
    }

    public static boolean Bigger (LocalDateTime dateTime , int min)
    {
        if (BiggerNow (dateTime))
        {
            long l = Timestamp.valueOf (LocalDateTime.now ()).getTime () - Timestamp.valueOf (dateTime).getTime ();

            // MIN_TIMESTAMP * min => (Value min) min
            return (l >= (MIN_TIMESTAMP * min));
        }
        else return false;
    }
}
