package com.bardiademon.CyrusMessenger.ServerSocket;

import org.springframework.context.ConfigurableApplicationContext;

public class Server
{
    private final ConfigurableApplicationContext Context;

    private Server (ConfigurableApplicationContext Context)
    {
        this.Context = Context;
        runTestConnection ();
    }

    private void runTestConnection ()
    {
        SIServer.CreateTestConnection ();
    }


    public static void Run (ConfigurableApplicationContext Context)
    {
        new Server (Context);
    }
}
