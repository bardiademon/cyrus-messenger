package com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses;

import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.Str;

// r => Request , in baraye sabt request jadid , bazi request ha dar baze zamani kotah nabayad ziyad darkhast esral beshe
public final class r extends Thread implements Runnable
{

    private static SubmitRequestService Service;

    // ns => new service
    public static void ns ()
    {
        Service = This.Context ().getBean (SubmitRequestService.class);
    }

    private String ip;
    private final SubmitRequestType type;
    private final boolean active;
    private MainAccount mainAccount;

    private r (String ip , SubmitRequestType type , boolean active)
    {
        this.ip = ip;
        this.type = type;
        this.active = active;
        start ();
    }

    private r (MainAccount mainAccount , SubmitRequestType type , boolean active)
    {
        this.mainAccount = mainAccount;
        this.type = type;
        this.active = active;
        start ();
    }

    /**
     * nim => New , IP OR MainAccount , if (mainAccount == null) using ip else using mainAccount ,
     * nim > i=>ip , m=>mainAccount
     */
    public static void nim (String ip , MainAccount mainAccount , SubmitRequestType type , boolean active)
    {
        if (mainAccount == null) n (ip , type , active);
        else n (mainAccount , type , active);
    }

    public static void n (String ip , SubmitRequestType type , boolean active)
    {
        if (!Str.IsEmpty (ip) && type != null) new r (ip , type , active);
    }

    public static void n (MainAccount mainAccount , SubmitRequestType type , boolean active)
    {
        if (mainAccount != null && type != null) new r (mainAccount , type , active);
    }

    @Override
    public void run ()
    {
        if (Service == null) ns ();

        if (ip == null || ip.isEmpty ()) Service.newRequest (ip , type , active);
        else Service.newRequest (mainAccount , type , active);
    }
}
