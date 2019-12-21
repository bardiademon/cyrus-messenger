package com.bardiademon.CyrusMessenger.bardiademon.Default;

import java.io.File;
import java.util.stream.IntStream;

public abstract class Path
{

    public static final String PROJECT = System.getProperty ("user.dir");

    public static final String COVER_USER = StickTogether (PROJECT , "files" , "Users" , "Cover");

    public static String StickTogether (String... Path)
    {
        StringBuilder NewPath;
        NewPath = new StringBuilder ();
        IntStream.range (0 , Path.length).forEachOrdered (i ->
        {
            String p = Path[i];
            if (((i + 1) < (Path.length - 1)) && Path[i + 1].equals (File.separator)) return;
            NewPath.append (p).append (File.separator);
        });
        return NewPath.toString ();
    }
}
