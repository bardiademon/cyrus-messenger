package com.bardiademon.CyrusMessenger;

import com.bardiademon.CyrusMessenger.ServerSocket.Server;
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
        Server.Run (Context);
    }

    public static ConfigurableApplicationContext Context ()
    {
        return Context;
    }
}
