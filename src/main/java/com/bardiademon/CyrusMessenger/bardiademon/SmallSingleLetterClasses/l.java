package com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.Model.Database.Log.Log;
import com.bardiademon.CyrusMessenger.Model.Database.Log.LogService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.InfoLine;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;

// l =>  Log
// esmesho injori neveshtam chon ke hameja estefade mishe rahat bashe va motefavet
public final class l extends Thread implements Runnable
{

    public static final String SEND_TO_CLIENT = "sent_to_client";

    private final String Request;
    private final String Route;
    private final MainAccount _MainAccount;
    private final AnswerToClient _AnswerToClient;
    private final InfoLine _InfoLine;
    private final Exception E;
    private final String Description;

    private static LogService Service;

    // ns => new Service
    public static void ns ()
    {
        Service = This.GetService (LogService.class);
    }

    private l (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , String Description)
    {
        this.Request = Request;
        this.Route = Route;
        this._MainAccount = _MainAccount;
        this._AnswerToClient = _AnswerToClient;
        this._InfoLine = new InfoLine (E , StackTrace);
        this.E = E;
        this.Description = Description;
        start ();
    }

    // n => new
    public static void n (StackTraceElement[] StackTrace , Exception E)
    {
        n (null , null , null , null , StackTrace , E , null);
    }

    // n => new
    public static void n (StackTraceElement[] StackTrace , Exception E , String Description)
    {
        n (null , null , null , null , StackTrace , E , Description);
    }

    // n => new
    public static void n (StackTraceElement[] StackTrace , String Description)
    {
        n (null , null , null , null , StackTrace , null , Description);
    }

    // n => new
    public static void n (StackTraceElement[] StackTrace)
    {
        n (null , null , null , null , StackTrace , null , null);
    }

    // n => new
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , String Description)
    {
        new l (Request , Route , _MainAccount , _AnswerToClient , StackTrace , E , Description);
    }

    // n => new
    public static void n (String Request , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , String Description)
    {
        new l (Request , null , _MainAccount , _AnswerToClient , StackTrace , E , Description);
    }

    // n => new , r r => request
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , String Description , SubmitRequestType _TypeRequest , boolean active)
    {
        new l (Request , Route , _MainAccount , _AnswerToClient , StackTrace , E , Description);
        r.n (_MainAccount , _TypeRequest , active);
    }

    @Override
    public void run ()
    {
        if (Service == null) ns ();

        Log log = new Log ();
        if (_AnswerToClient != null)
        {
            log.setAnswerToClient (_AnswerToClient.toString ());

            String json;
            if (_AnswerToClient.getRequest () != null)
            {
                if (_AnswerToClient.getRequest () != null)
                    log.setIp (_AnswerToClient.getRequest ().getRemoteAddr ());
                try
                {
                    json = ToJson.RequestToJson (_AnswerToClient.getRequest ());
                    if (json != null) log.setHttpServletRequest (json);
                }
                catch (Exception ignored)
                {
                }
            }
            if (_AnswerToClient.getResponse () != null)
            {
                try
                {
                    json = ToJson.ResponseToJson (_AnswerToClient.getResponse ());
                    if (json != null) log.setHttpServletResponse (json);
                }
                catch (Exception ignored)
                {
                }
            }
        }
        log.setError ((E != null));

        if (_MainAccount != null) log.setMainAccount (_MainAccount);

        if (Route != null) log.setRoute (Route);

        log.setInfoLine (_InfoLine.toString ());

        if (Request != null) log.setRequest (Request);
        if (Description != null) log.setDescription (Description);
        Service.Repository.save (log);

    }
}
