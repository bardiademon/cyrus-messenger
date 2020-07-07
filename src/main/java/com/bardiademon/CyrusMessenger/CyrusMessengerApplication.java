package com.bardiademon.CyrusMessenger;

import com.bardiademon.CyrusMessenger.ServerSocket.Server;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CyrusMessengerApplication
{

    public static ConfigurableApplicationContext Context;

    public static void main (String[] args)
    {
        Context = SpringApplication.run (CyrusMessengerApplication.class , args);

        l.ns ();
        r.ns ();

        Server.Run (Context);
    }

    public static ConfigurableApplicationContext Context ()
    {
        return Context;
    }
}
