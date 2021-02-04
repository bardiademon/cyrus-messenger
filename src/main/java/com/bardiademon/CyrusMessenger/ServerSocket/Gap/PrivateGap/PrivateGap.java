package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages.GetMessages;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages.RequestGetMessages;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing.ReqTyping;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing.Typing;
import com.bardiademon.CyrusMessenger.ServerSocket.HostPort;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.bardiademon.Pagination;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;

public final class PrivateGap implements SIServer.Client
{
    private final SIServer _SIServer = new SIServer (HostPort.PORT_PRIVATE_CHAT , this);

    private final Pagination pagination = new Pagination ();

    private final StatusOfSentMessage statusOfSentMessage;

    public PrivateGap ()
    {
        on ();
        _SIServer.startServer ();
        statusOfSentMessage = new StatusOfSentMessage ();
    }

    private void on ()
    {
        SIServer.CreateFirstConnection (_SIServer.Server);
        _SIServer.Server.addEventListener (EventName.pvgp_send_message.name () , RequestPrivateGap.class , (client , data , ackSender) ->
                new NewPrivateMessage (client , data));

        SIServer.SetOffline (_SIServer.Server);

        _SIServer.Server.addEventListener (EventName.pvgp_typing.name () , ReqTyping.class , (client , data , ackSender) ->
                new Typing (client , data));

        _SIServer.Server.addEventListener (EventName.get_messages.name () , RequestGetMessages.class , (client , data , ackSender) ->
                new GetMessages (client , data));

        _SIServer.Server.addEventListener (EventName.status_of_sent_message.name () , StatusOfSentMessage.Request.class , (client , data , ackSender) ->
                statusOfSentMessage.status (data , client));
    }

    public void deletePersonalGap (MainAccount mainAccount , long personalGapId)
    {
        // ersale dastore hazf be device karbar digar ke tavasot karbar dighar in gofego hazf shode

        SIServer.LoopOnline ((CodeOnline , _Online) ->
        {
            // chon momkene chan ta online vojod dashte bashe baraye hamin ta akhar loop ro donbal mikonal ,
            // chan ta online => ba yek account
            if (_Online.getMainAccount ().getId () == mainAccount.getId ())
                sendDeletePersonalGap (CodeOnline , _Online , personalGapId);

            return true;
        });

    }

    private void sendDeletePersonalGap (String codeOnline , Online online , long personalGapId)
    {
        online.getClient ().sendEvent (EventName.delete_personal_gap.name () , personalGapId);
        online.setAnnouncementOfPresence (Time.now ());
        SIServer.Onlines.replace (codeOnline , online);
    }

    @Override
    public void Connect (SocketIOClient Client)
    {
        l.n (Thread.currentThread ().getStackTrace () , ToJson.CreateClass.nj ("private_chat_connect" , Client.getSessionId ().toString ()));
    }

    public Pagination getPagination ()
    {
        return pagination;
    }
}
