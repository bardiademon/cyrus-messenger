package com.bardiademon.CyrusMessenger.ServerSocket;

import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.PrivateGap;

public class Server
{
    private Server ()
    {
        firstConnection ();
        privateGap ();
    }

    private void firstConnection ()
    {
        SIServer siServer = new SIServer (HostPort.PORT_TEST_CONNECTION , Client ->
                System.out.println (FirstRequest.class.getName () + " > " + Client.getSessionId ()));

        siServer.startServer ();

        SIServer.CreateFirstConnection (siServer.Server);
    }

    private void privateGap ()
    {
        new PrivateGap ();
    }

    public static void Run ()
    {
        new Server ();
    }
}
