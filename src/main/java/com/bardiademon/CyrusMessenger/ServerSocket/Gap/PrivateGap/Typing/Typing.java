package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.corundumstudio.socketio.SocketIOClient;
import java.time.LocalDateTime;

public final class Typing
{
    private final SocketIOClient client;
    private final ReqTyping req;

    private AnswerToClient answer;

    private Online onlineTo;

    public Typing (final SocketIOClient Client , final ReqTyping Data)
    {
        this.client = Client;
        this.req = Data;
        if (checkRequest () && foundTo ()) sendToTo ();

        sendToClient ();
    }

    private boolean checkRequest ()
    {
        CBSIL both = CBSIL.Both (req , req.getCodeLogin () , EventName.pvgp_typing.name ());

        if (both.isOk ())
        {
            if (req.checkOnlineCode ())
            {
                UsernamesService usernamesService = (UsernamesService) ThisApp.S ().getService (UsernamesService.class);

                FITD_Username fitd_username = new FITD_Username (req.getTo () , usernamesService);
                if (fitd_username.isValid ()) return true;
                else answer = fitd_username.getAnswer ();
            }
            else answer = req.getAnswer ();
        }
        else answer = both.getAnswerToClient ();

        return false;
    }

    private boolean foundTo ()
    {
        SIServer.LoopOnline ((CodeOnline , _Online) ->
        {
            if (_Online.getMainAccount ().getUsername ().getUsername ().equals (req.getTo ()))
            {
                onlineTo = _Online;
                onlineTo.setAnnouncementOfPresence (LocalDateTime.now ());
                SIServer.Onlines.replace (CodeOnline , onlineTo);

                return false;
            }
            return true;
        });

        if (onlineTo != null)
            return true;
        else
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.to_is_offline.name ());
            return false;
        }
    }

    private void sendToClient ()
    {
        Online online = SIServer.Onlines.get (req.getCodeOnline ());

        online.setClient (client);
        online.setAnnouncementOfPresence (LocalDateTime.now ());
        SIServer.Onlines.replace (req.getCodeOnline () , online);

        client.sendEvent (EventName.e_pvgp_typing.name () , answer);
    }

    private void sendToTo ()
    {
        onlineTo.getClient ().sendEvent (EventName.pvgp_is_typing.name () , req.getFrom ());
    }

    private enum ValAnswer
    {
        to_is_offline
    }
}
