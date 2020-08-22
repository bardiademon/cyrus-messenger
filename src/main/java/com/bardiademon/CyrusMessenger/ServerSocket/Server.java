package com.bardiademon.CyrusMessenger.ServerSocket;

public class Server
{
    private Server ()
    {
        runTestConnection ();
    }

    private void runTestConnection ()
    {
        SIServer.CreateTestConnection ();
    }

    public static void Run ()
    {
        new Server ();
    }
}
