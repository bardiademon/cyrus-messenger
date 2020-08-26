package com.bardiademon.CyrusMessenger.ServerSocket.RestSocket;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Chat.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Chat.Online.OnlineService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;

public class SetOffline
{
    private final SocketIOClient client;
    private final PublicRequest request;

    private AnswerToClient answer;

    private Online online;

    private boolean disconnect = false;

    public SetOffline (final SocketIOClient Client , final PublicRequest Request)
    {
        this.client = Client;
        this.request = Request;
        setOff ();
        sendToClient ();
    }

    private void setOff ()
    {
        if (request != null && !Str.IsEmpty (request.getCodeLogin ()))
        {
            CBSIL both = CBSIL.Both (request , request.getCodeLogin () , EventName.firstr_set_offline.name ());
            if (both.isOk ())
            {
                assert both.getIsLogin () != null;
                MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

                if (SIServer.Onlines.containsKey (request.getCodeOnline ()))
                {
                    online = SIServer.Onlines.get (request.getCodeOnline ());
                    SIServer.Onlines.remove (request.getCodeOnline ());

                    final OnlineService service = (OnlineService) ThisApp.S ().getService (OnlineService.class);

                    service.setOffline (online);

                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.ok.name ());
                    l.n (ToJson.To (request) , EventName.firstr_set_offline.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.ok.name ());

                    disconnect = true;
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.code_online_invalid.name ());
                    l.n (ToJson.To (request) , EventName.firstr_set_offline.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.code_online_invalid.name ()) , null);
                }
            }
            else answer = both.getAnswerToClient ();
        }
        else
        {
            answer = AnswerToClient.RequestIsNull ();
            l.n (ToJson.To (request) , EventName.firstr_set_offline.name () , null , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
        }
    }

    private void sendToClient ()
    {
        System.out.println (answer.toString ());
        client.sendEvent (EventName.e_firstr_set_offline.name () , answer.toString ());
        try
        {
            Thread.sleep (1000);
        }
        catch (InterruptedException ignored)
        {
        }
        if (!disconnect)
        {
            client.disconnect ();
            online.getClient ().disconnect ();
        }
    }

    private enum ValAnswer
    {
        code_online_invalid
    }
}
