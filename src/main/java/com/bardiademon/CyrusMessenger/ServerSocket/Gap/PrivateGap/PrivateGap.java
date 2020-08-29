package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;

public final class PrivateGap implements SIServer.Client
{
    // Private Chat => tak tak char ha ba ham jam shodan shod 1147 hamino gozashtam prt :D
    private static final int PORT = 1147;

    private final SIServer _SIServer = new SIServer (PORT , this);

    public PrivateGap ()
    {
        on ();
        _SIServer.startServer ();
    }

    private void on ()
    {
        SIServer.CreateFirstConnection (_SIServer.Server);
        _SIServer.Server.addEventListener (EventName.pvgp_send_message.name () , RequestPrivateGap.class , (client , data , ackSender) ->
                new NewPrivateMessage (client , data));




        SIServer.SetOffline (_SIServer.Server);
    }

    @Override
    public void Connect (SocketIOClient Client)
    {
        l.n (Thread.currentThread ().getStackTrace () , ToJson.CreateClass.nj ("private_chat_connect" , Client.getSessionId ().toString ()));
    }
}
