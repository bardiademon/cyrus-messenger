package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing.ReqTyping;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing.Typing;
import com.bardiademon.CyrusMessenger.ServerSocket.HostPort;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;

public final class PrivateGap implements SIServer.Client
{
    private final SIServer _SIServer = new SIServer (HostPort.PORT_PRIVATE_CHAT , this);

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

        _SIServer.Server.addEventListener (EventName.pvgp_typing.name () , ReqTyping.class , (client , data , ackSender) ->
                new Typing (client , data));
    }

    public void deletePersonalGap (MainAccount mainAccount)
    {
        // ersale dastore hazf be device karbar digar ke tavasot karbar dighar in gofego hazf shode
    }


    @Override
    public void Connect (SocketIOClient Client)
    {
        l.n (Thread.currentThread ().getStackTrace () , ToJson.CreateClass.nj ("private_chat_connect" , Client.getSessionId ().toString ()));
    }
}
