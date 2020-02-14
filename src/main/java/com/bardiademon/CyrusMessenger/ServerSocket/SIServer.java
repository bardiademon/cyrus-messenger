package com.bardiademon.CyrusMessenger.ServerSocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;


public class SIServer
{
    public final SocketIOServer Server;

    public SIServer (final int Port , Client _Client)
    {
        Server = (create (Port));
        Server.addConnectListener (_Client::Connect);
        Server.start ();
        System.out.println ("Server Start Port " + Port);
    }

    private SocketIOServer create (int port)
    {
        Configuration configuration = new Configuration ();
        configuration.setHostname (HostPort.HOST);
        configuration.setPort (port);
        return new SocketIOServer (configuration);
    }

    public static void CreateTestConnection ()
    {
        new SIServer (HostPort.PORT_TEST_CONNECTION , Client -> Client.sendEvent ("ok" , true));
    }

    public interface Client
    {
        void Connect (SocketIOClient Client);
    }

}
