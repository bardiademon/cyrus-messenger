package com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.OnlineStatus;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Chat.Online.OnlineService;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;
import java.time.LocalDateTime;

public final class OnlineStatus
{
    private final SocketIOClient client;
    private final RequestOnlineStatus request;

    private AnswerToClient answer;

    private MainAccount mainAccount;

    public OnlineStatus (SocketIOClient Client , RequestOnlineStatus Request)
    {
        this.client = Client;
        this.request = Request;
        getLastSeen ();
        toClient ();
    }

    private void getLastSeen ()
    {
        String strReq = ToJson.To (request);

        if (request == null || Str.IsEmpty (request.getUsername ()) || Str.IsEmpty (request.getCodeLogin ()))
        {
            answer = AnswerToClient.RequestIsNull ();
            l.n (strReq , EventName.firstr_status_online.name () , null , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
        }
        else
        {
            CBSIL both = CBSIL.Both (strReq , request.getCodeLogin () , EventName.firstr_status_online.name ());
            if (both.isOk ())
            {
                assert both.getIsLogin () != null;
                mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

                FITD_Username fitd_username = new FITD_Username (request.getUsername () , (UsernamesService) ThisApp.S ().getService (UsernamesService.class));

                if (fitd_username.isValid ())
                {
                    UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (mainAccount , fitd_username.getMainAccount ());

                    if (accessLevel.hasAccess (Which.profile) && accessLevel.hasAccess (Which.find_me))
                    {
                        if (accessLevel.hasAccess (Which.last_seen))
                        {
                            if (SIServer.Onlines.containsKey (request.getCodeOnline ()) && SIServer.Onlines.get (request.getCodeOnline ()).getMainAccount ().getUsername ().getUsername ().equals (fitd_username.getMainAccount ().getUsername ().getUsername ()))
                            {
                                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.online.name ());
                                l.n (strReq , EventName.firstr_status_online.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , ValAnswer.online.name ());
                            }
                            else
                            {
                                OnlineService onlineService = (OnlineService) ThisApp.S ().getService (OnlineService.class);
                                LocalDateTime lastSeen = onlineService.lastSeen (fitd_username.getMainAccount ().getId ());
                                if (lastSeen != null)
                                {
                                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                                    answer.put (KeyAnswer.last_seen.name () , Time.toString (lastSeen));
                                    answer.put (KeyAnswer.timestamp.name () , Time.timestamp (lastSeen).getTime ());
                                    l.n (strReq , EventName.firstr_status_online.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.found.name ());
                                }
                                else
                                {
                                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.do_not_know.name ());
                                    l.n (strReq , EventName.firstr_status_online.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , ValAnswer.do_not_know.name ());
                                }
                            }
                        }
                        else
                        {
                            answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.do_not_know.name ());
                            l.n (strReq , EventName.firstr_status_online.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , ValAnswer.do_not_know.name ());
                        }
                    }
                    else
                    {
                        // chon age gheire faal bashe namaesh profile va peyda kardan pas nabayad begam hamchin useri vojod dare
                        answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_not_found.name ());
                        l.n (strReq , EventName.firstr_status_online.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_not_found.name ()) , null);
                    }
                }
                else
                {
                    answer = fitd_username.getAnswer ();
                    l.n (strReq , EventName.firstr_status_online.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_invalid.name ()) , null);
                }


            }
            else answer = both.getAnswerToClient ();
        }
    }

    public void toClient ()
    {
        if (client != null)
        {
            client.sendEvent (EventName.firstr_last_seen.name () , ToJson.To (answer));
            l.n (ToJson.To (request) , EventName.firstr_status_online.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , null);
        }
    }

    private enum KeyAnswer
    {
        last_seen, timestamp
    }

    private enum ValAnswer
    {
        username_invalid, username_not_found, do_not_know, online
    }
}
