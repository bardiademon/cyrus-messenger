package com.bardiademon.CyrusMessenger.ServerSocket;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.OnlineService;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.OnlineStatus.OnlineStatus;
import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.OnlineStatus.RequestOnlineStatus;
import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;
import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.SetOffline;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public final class SIServer
{
    private static final Object Wait = new Object ();

    public final SocketIOServer Server;

    public static final Map <String, Online> Onlines = new LinkedHashMap <> ();

    private static OnlineService onlineService;

    public SIServer (final int Port , Client _Client)
    {
        Server = (create (Port));
        Server.addConnectListener (_Client::Connect);
        System.out.println ("Server Start Port " + Port);
    }

    public void startServer ()
    {
        Server.start ();
    }

    private SocketIOServer create (int port)
    {
        Configuration configuration = new Configuration ();
        configuration.setHostname (HostPort.HOST);
        configuration.setPort (port);
        return new SocketIOServer (configuration);
    }

    public static void CreateFirstConnection (SocketIOServer Server)
    {
        Server.addEventListener (EventName.firstr.name () , String.class , (client , data , ackSender) ->
        {
            FirstRequest firstRequest = new FirstRequest (data , client);

            AnswerToClient answer = firstRequest.getAnswer ();

            if (firstRequest.isOnline ())
            {
                String codeLogin;
                if ((codeLogin = SetOnline (firstRequest.getOnline ())) != null)
                    answer.put (KeyAnswer.code_online.name () , codeLogin);
                else
                {
                    answer = AnswerToClient.ServerError ();
                    l.n (data , EventName.firstr.name () , firstRequest.getOnline ().getMainAccount () , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.error.name ()) , null);
                }
            }

            client.sendEvent (EventName.firstr_answer.name () , ToJson.To (firstRequest.getAnswer ()));
        });

        new Timer ().schedule (new TimerTask ()
        {
            @Override
            public void run ()
            {
                LoopOnline ((codeOnline , online) ->
                {
                    if (Time.Bigger (online.getAnnouncementOfPresence () , 15))
                    {
                        if (onlineService == null)
                            onlineService = (OnlineService) ThisApp.S ().getService (OnlineService.class);

                        onlineService.setOffline (online);
                        Onlines.remove (codeOnline);
                    }
                    return true;
                });
            }
        } , 1000 , 1000);

        OnlineStatus (Server);
        SetOffline (Server);
    }

    private static String SetOnline (Online online)
    {
        final AtomicReference <String> codeOnline = new AtomicReference <> (null);
        new Thread (() -> Code.CreateCodeIsNotExists (Code.CreateCodeLong () , 10 , (code , last) ->
        {
            if (Onlines.containsKey (code))
            {
                if (last) synchronized (Wait)
                {
                    Wait.notifyAll ();
                }

                return false;
            }
            else
            {
                codeOnline.set (code);
                synchronized (Wait)
                {
                    Wait.notifyAll ();
                }
                return true;
            }
        })).start ();
        synchronized (Wait)
        {
            try
            {
                Wait.wait ();
            }
            catch (InterruptedException e)
            {
                l.n (Thread.currentThread ().getStackTrace () , e);
                return null;
            }
        }

        if (codeOnline.get () != null)
        {
            Onlines.put (codeOnline.get () , online);
            l.n (Thread.currentThread ().getStackTrace () , ToJson.CreateClass.nj ("set_online" , online.getMainAccount ().getId ()));
            return codeOnline.get ();
        }
        else return null;
    }

    public static void LoopOnline (LoopOnline loopOnline)
    {
        for (Map.Entry <String, Online> entry : Onlines.entrySet ())
            if (!loopOnline.doing (entry.getKey () , entry.getValue ())) break;
    }

    private static void OnlineStatus (SocketIOServer Server)
    {
        Server.addEventListener (EventName.firstr_status_online.name () , RequestOnlineStatus.class , (client , data , ackSender) ->
                new OnlineStatus (client , data));
    }

    public static void SetOffline (SocketIOServer Server)
    {
        Server.addEventListener (EventName.set_offline.name () , PublicRequest.class , (client , data , ackSender) ->
                new SetOffline (client , data));
    }

    public interface LoopOnline
    {
        // bool => continue ? yes : now;
        boolean doing (String CodeOnline , Online _Online);
    }

    public interface Client
    {
        void Connect (SocketIOClient Client);
    }

    private enum KeyAnswer
    {
        code_online
    }
}
