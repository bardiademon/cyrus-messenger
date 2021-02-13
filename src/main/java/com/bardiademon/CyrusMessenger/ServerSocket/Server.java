package com.bardiademon.CyrusMessenger.ServerSocket;

import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.ServerSocketGap;

public class Server
{
    private ServerSocketGap serverSocketGap;

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
        serverSocketGap = new ServerSocketGap ();
    }

    public ServerSocketGap getServerSocketGap ()
    {
        return serverSocketGap;
    }

    public static Server Run ()
    {
        return new Server ();
    }

}
