package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserGapAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;
import java.time.LocalDateTime;

public final class Typing
{
    private final SocketIOClient client;
    private final ReqTyping req;

    private AnswerToClient answer;

    private Online onlineTo;

    private MainAccount from;

    public Typing (final SocketIOClient Client , final ReqTyping Data)
    {
        this.client = Client;
        this.req = Data;
        if (checkRequest () && foundTo ()) sendToTo ();

        sendToClient ();
    }

    private boolean checkRequest ()
    {
        final CBSIL both = CBSIL.Both (req , req.getCodeLogin () , EventName.ssg_typing.name ());

        if (both.isOk ())
        {
            final Online online;
            if ((online = req.getOnline ()) != null)
            {
                from = online.getMainAccount ();
                final UsernamesService usernamesService = This.GetService (UsernamesService.class);
                final FITD_Username usernameTO = new FITD_Username (req.getTo () , usernamesService);
                if (usernameTO.isValid ()) return true;
                else answer = usernameTO.getAnswer ();
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
        final Online online = SIServer.Onlines.get (req.getCodeOnline ());

        online.setClient (client);
        online.setAnnouncementOfPresence (LocalDateTime.now ());
        SIServer.Onlines.replace (req.getCodeOnline () , online);

        client.sendEvent (EventName.e_ssg_typing.name () , answer);
    }

    private void sendToTo ()
    {
        final UserGapAccessLevel gapAccessLevel = new UserGapAccessLevel (onlineTo.getMainAccount () , from);
        if (gapAccessLevel.hasAccess (Which.sh_is_typing))
        {
            final UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (onlineTo.getMainAccount () , from);
            if (accessLevel.hasAccess (Which.id))
            {
                l.n (Thread.currentThread ().getStackTrace () , "Send typing => " + ToJson.To (req));
                onlineTo.getClient ().sendEvent (EventName.ssg_is_typing.name () , from.getUsername ().getId ());
            }
            else
                l.n (Thread.currentThread ().getStackTrace () , new Exception ("access denied (id) Send typing") , String.valueOf (from.getId ()));
        }
        else
            l.n (Thread.currentThread ().getStackTrace () , new Exception ("access denied Send typing => " + ToJson.To (req)));
    }

    private enum ValAnswer
    {
        to_is_offline
    }
}
