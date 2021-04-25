package com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Log.Log;
import com.bardiademon.CyrusMessenger.Model.Database.Log.LogService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.This;
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
        n (null , ((String) null) , null , null , StackTrace , E , "");
    }

    // n => new
    public static void n (StackTraceElement[] StackTrace , Exception E , Enum <?> Description)
    {
        n (StackTrace , E , Description.name ());
    }    // n => new

    public static void n (StackTraceElement[] StackTrace , Exception E , String Description)
    {
        n (null , ((String) null) , null , null , StackTrace , E , Description);
    }

    // n => new
    public static void n (StackTraceElement[] StackTrace , Enum <?> Description)
    {
        n (StackTrace , Description.name ());
    }    // n => new

    public static void n (StackTraceElement[] StackTrace , String Description)
    {
        n (null , ((String) null) , null , null , StackTrace , null , Description);
    }

    // n => new
    public static void n (StackTraceElement[] StackTrace)
    {
        n (null , ((String) null) , null , null , StackTrace , null , "");
    }

    // n => new
    public static void n (String Request , Enum <?> Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E)
    {
        n (Request , Route.name () , _MainAccount , _AnswerToClient , StackTrace , E , ((String) null));
    }

    // n => new
    public static void n (String Request , Enum <?> Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , Enum <?> Description)
    {
        n (Request , Route.name () , _MainAccount , _AnswerToClient , StackTrace , E , Description);
    }

    //  => new
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace)
    {
        n (Request , Route , _MainAccount , _AnswerToClient , StackTrace , null);
    }

    //  => new
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E)
    {
        n (Request , Route , _MainAccount , _AnswerToClient , StackTrace , E , ((String) null));
    }

    //  => new
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , Enum <?> Description)
    {
        n (Request , Route , _MainAccount , _AnswerToClient , StackTrace , E , Description.name ());
    }

    // n => new
    public static void n (String Request , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E)
    {
        n (Request , ((String) null) , _MainAccount , _AnswerToClient , StackTrace , E , "");
    }

    // n => new
    public static void n (String Request , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , Enum <?> Description)
    {
        n (Request , ((String) null) , _MainAccount , _AnswerToClient , StackTrace , E , Description.name ());
    }

    // n => new
    public static void n (String Request , Enum <?> Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , String Description)
    {
        n (Request , Route.name () , _MainAccount , _AnswerToClient , StackTrace , E , Description);
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
    public static void n (String Request , Enum <?> Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , Enum <?> Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Route.name () , _MainAccount , _AnswerToClient , StackTrace , E , Description , _TypeRequest , Active);
    }

    // n => new , r r => request
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , Enum <?> Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Route , _MainAccount , _AnswerToClient , StackTrace , E , Description.name () , _TypeRequest , Active);
    }

    // n => new , r r => request
    public static void n (String Request , Enum <?> Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , String Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Route.name () , _MainAccount , _AnswerToClient , StackTrace , E , Description , _TypeRequest , Active);
    }

    // n => new , r r => request
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Enum <?> Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Route , _MainAccount , _AnswerToClient , StackTrace , Description , _TypeRequest , Active);
    }

    // n => new , r r => request
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , String Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Route , _MainAccount , _AnswerToClient , StackTrace , Description , _TypeRequest , Active);
    }

    // n => new , r r => request
    public static void n (String Request , Enum <?> Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Route.name () , _MainAccount , _AnswerToClient , StackTrace , E , _TypeRequest , Active);
    }

    // n => new , r r => request
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Route , _MainAccount , _AnswerToClient , StackTrace , E , _TypeRequest , Active);
    }

    // n => new , r r => request
    public static void n (String Request , String Route , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , String Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        new l (Request , Route , _MainAccount , _AnswerToClient , StackTrace , E , Description);
        r.n (_MainAccount , _TypeRequest , Active);
    }

    public static Exception e (final Enum <?> Message)
    {
        return e (Message.name ());
    }

    public static Exception e (final String Message)
    {
        return new Exception (Message);
    }

    public static void n (String Request , Enum <?> Router , MainAccount _MainAccount , StackTraceElement[] StackTrace , Enum <?> Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Router.name () , _MainAccount , null , StackTrace , Description , _TypeRequest , Active);
    }

    public static void n (String Request , String Router , MainAccount _MainAccount , StackTraceElement[] StackTrace , Enum <?> Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Router , _MainAccount , null , StackTrace , Description.name () , _TypeRequest , Active);
    }

    public static void n (String Request , String Router , MainAccount _MainAccount , StackTraceElement[] StackTrace , String Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        new l (Request , Router , _MainAccount , null , StackTrace , null , Description);
        r.n (_MainAccount , _TypeRequest , Active);
    }

    public static void n (String Request , Enum <?> Router , MainAccount _MainAccount , StackTraceElement[] StackTrace , Exception E , Enum <?> Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Router.name () , _MainAccount , null , StackTrace , E , Description , _TypeRequest , Active);
    }

    public static void n (String Request , String Router , MainAccount _MainAccount , StackTraceElement[] StackTrace , Exception E , Enum <?> Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Router , _MainAccount , null , StackTrace , E , Description.name () , _TypeRequest , Active);
    }

    public static void n (String Request , String Router , MainAccount _MainAccount , StackTraceElement[] StackTrace , Exception E , String Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        new l (Request , Router , _MainAccount , null , StackTrace , E , Description);
        r.n (_MainAccount , _TypeRequest , Active);
    }

    public static void n (String Request , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , Enum <?> Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , "" , _MainAccount , _AnswerToClient , StackTrace , E , Description.name () , _TypeRequest , Active);
    }

    public static void n (String Request , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , String Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        new l (Request , null , _MainAccount , _AnswerToClient , StackTrace , E , Description);
        r.n (_MainAccount , _TypeRequest , Active);
    }

    public static void n (String Request , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Enum <?> Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , "" , _MainAccount , _AnswerToClient , StackTrace , Description.name () , _TypeRequest , Active);
    }

    public static void n (String Request , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , String Description , SubmitRequestType _TypeRequest , boolean Active)
    {
        new l (Request , null , _MainAccount , _AnswerToClient , StackTrace , null , Description);
        r.n (_MainAccount , _TypeRequest , Active);
    }

    public static void n (String Request , Enum <?> Router , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , SubmitRequestType _TypeRequest , boolean Active)
    {
        n (Request , Router.name () , _MainAccount , _AnswerToClient , StackTrace , null , "" , _TypeRequest , Active);
    }

    public static void n (String Request , String Router , MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , SubmitRequestType _TypeRequest , boolean Active)
    {
        new l (Request , Router , _MainAccount , _AnswerToClient , StackTrace , null , "");
        r.n (_MainAccount , _TypeRequest , Active);
    }

    public static void n (String Request , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E)
    {
        new l (Request , null , null , _AnswerToClient , StackTrace , null , "");
    }

    public static void n (AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , Enum <?> Description)
    {
        n (_AnswerToClient , StackTrace , E , Description.name ());
    }

    public static void n (AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E , String Description)
    {
        new l (null , null , null , _AnswerToClient , StackTrace , E , Description);
    }

    public static void n (AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E)
    {
        new l (null , null , null , _AnswerToClient , StackTrace , E , "");
    }

    public static void n (MainAccount _MainAccount , AnswerToClient _AnswerToClient , StackTraceElement[] StackTrace , Exception E)
    {
        new l (null , null , _MainAccount , _AnswerToClient , StackTrace , null , "");
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
        log = Service.Repository.save (log);

        if (log.isError ()) System.err.println (log.toString ());
        else System.out.println (log.toString ());

    }

}
