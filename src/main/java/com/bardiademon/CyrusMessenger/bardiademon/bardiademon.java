package com.bardiademon.CyrusMessenger.bardiademon;

import static java.lang.System.out;

public final class bardiademon
{
    public bardiademon ()
    {
        print ();
    }

    private void print ()
    {
        out.println ("----------------------------------------------------");
        out.println ("bardiademon");
        out.println ("https://bardiademon.com");
        out.println ("----------------------------------------------------");
    }

    public static void run ()
    {
        new bardiademon ();
    }
}
