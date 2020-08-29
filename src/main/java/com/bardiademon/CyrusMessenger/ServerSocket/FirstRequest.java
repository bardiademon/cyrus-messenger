package com.bardiademon.CyrusMessenger.ServerSocket;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.OnlineService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;
import java.time.LocalDateTime;

public final class FirstRequest
{
    private final String request;

    private final String codeLogin;
    private final SocketIOClient client;
    private Online online;
    private AnswerToClient answer;

    private boolean isOnline;

    public FirstRequest (String CodeLogin , SocketIOClient Client)
    {
        this.codeLogin = CodeLogin;
        this.client = Client;
        request = ToJson.CreateClass.nj ("code_login" , codeLogin);
        online ();
    }

    private void online ()
    {
        UserLoginService userLoginService = (UserLoginService) ThisApp.S ().getService (UserLoginService.class);
        if (userLoginService != null)
        {
            CBSIL cbsil = CBSIL.Both (request , null , null , codeLogin , userLoginService , null , SubmitRequestType.socket);
            if (cbsil.isOk ())
            {
                assert cbsil.getIsLogin () != null;
                MainAccount mainAccount = cbsil.getIsLogin ().getVCodeLogin ().getMainAccount ();

                checkOnline (mainAccount.getUsername ().getUsername ());

                online = new Online ();
                online.setClient (client);
                online.setMainAccount (mainAccount);
                online.setAnnouncementOfPresence (LocalDateTime.now ());
                online.setOnlineAt (LocalDateTime.now ());
                online.setUuid (client.getSessionId ().toString ());

                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.online.name ());
                l.n (request , null , answer , Thread.currentThread ().getStackTrace () , null , ValAnswer.online.name ());

                isOnline = true;
            }
            else answer = cbsil.getAnswerToClient ();
        }
        else
        {
            answer = AnswerToClient.ServerError ();
            l.n (request , null , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null);
        }
    }

    private void checkOnline (String username)
    {
        SIServer.LoopOnline ((codeOnline , online) ->
        {
            if (online.getMainAccount ().getUsername ().getUsername ().equals (username))
            {
                OnlineService onlineService = (OnlineService) ThisApp.S ().getService (OnlineService.class);
                if (onlineService != null)
                {
                    onlineService.setOffline (online);
                    SIServer.Onlines.remove (codeOnline);
                }
                return false;
            }
            else return true;
        });
    }

    public AnswerToClient getAnswer ()
    {
        return answer;
    }

    public Online getOnline ()
    {
        return online;
    }

    public boolean isOnline ()
    {
        return isOnline;
    }

    private enum ValAnswer
    {
        online
    }
}
