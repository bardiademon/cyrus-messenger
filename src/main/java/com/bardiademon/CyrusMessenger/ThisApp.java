package com.bardiademon.CyrusMessenger;

import com.bardiademon.CyrusMessenger.ServerSocket.Server;
import com.bardiademon.CyrusMessenger.ServerSocket.Services;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ThisApp
{
    private static final Services SERVICES = new Services ();

    public static ConfigurableApplicationContext Context;

    private static Server server;

    public static void main (String[] args)
    {
        Context = SpringApplication.run (ThisApp.class , args);

        l.ns ();
        r.ns ();

        server = Server.Run ();
    }

    public static ConfigurableApplicationContext Context ()
    {
        return Context;
    }


    public static Server getServer ()
    {
        return server;
    }

    // S => Services
    public static Services S ()
    {
        return SERVICES;
    }
}
