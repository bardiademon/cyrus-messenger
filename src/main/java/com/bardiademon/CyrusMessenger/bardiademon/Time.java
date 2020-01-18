package com.bardiademon.CyrusMessenger.bardiademon;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Time
{
    public static String toString (LocalDateTime time)
    {
        if (time == null) return null;
        return time.format (DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm:ss"));
    }
}
