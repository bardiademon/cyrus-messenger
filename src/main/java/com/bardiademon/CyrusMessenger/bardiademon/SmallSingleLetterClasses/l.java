package com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.CyrusMessengerApplication;
import com.bardiademon.CyrusMessenger.Model.Database.Log.Log;
import com.bardiademon.CyrusMessenger.Model.Database.Log.LogService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.bardiademon.CyrusMessenger.bardiademon.InfoLine;

// l =>  Log
// esmesho injori neveshtam chon ke hameja estefade mishe rahat bashe va motefavet
public final class l extends Thread implements Runnable
{

    private String Request;
    private String Route;
    private MainAccount _MainAccount;
    private AnswerToClient _AnswerToClient;
    private InfoLine _InfoLine;
    private Exception E;
    private String Description;

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

    @Override
    public void run ()
    {
        LogService _LogService = CyrusMessengerApplication.Context ().getBean (LogService.class);

        Log log = new Log ();
        if (_AnswerToClient != null)
        {
            log.setAnswerToClient (_AnswerToClient.toString ());

            String json;
            if (_AnswerToClient.getRequest () != null)
            {
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
        _LogService.Repository.save (log);

    }
}
