package com.bardiademon.CyrusMessenger.ServerSocket;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Online.OnlineService;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public final class SIServer
{
    public final SocketIOServer Server;

    public static final List <Online> Onlines = new ArrayList <> ();

    private static OnlineService onlineService;

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
        SIServer onlineServer = new SIServer (HostPort.PORT_TEST_CONNECTION , Client ->
                System.out.println (FirstRequest.class.getName () + " > " + Client.getSessionId ()));

        onlineServer.Server.addEventListener (EventName.firstr.name () , String.class , (client , data , ackSender) ->
        {
            FirstRequest firstRequest = new FirstRequest (data , client);
            if (firstRequest.isOnline ())
                Onlines.add (firstRequest.getOnline ());

            client.sendEvent (EventName.firstr_answer.name () , ToJson.To (firstRequest.getAnswer ()));
        });

        new Timer ().schedule (new TimerTask ()
        {
            @Override
            public void run ()
            {
                Offline ((online , index) ->
                {
                    if (Time.Bigger (online.getAnnouncementOfPresence () , 15))
                    {
                        if (onlineService == null)
                            onlineService = (OnlineService) ThisApp.S ().getService (OnlineService.class);

                        onlineService.setOffline (online);
                        Onlines.remove (index);
                    }
                    return true;
                });
            }
        } , 1000 , 1000);

    }

    public static void Offline (Offline offline)
    {
        for (int i = 0, len = SIServer.Onlines.size (); i < len; i++)
            if (!offline.doing (SIServer.Onlines.get (i) , i)) break;
    }

    public interface Offline
    {
        // bool => continue ? yes : now;
        boolean doing (Online online , int index);
    }

    public interface Client
    {
        void Connect (SocketIOClient Client);
    }
}
