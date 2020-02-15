package com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses;

import com.bardiademon.CyrusMessenger.CyrusMessengerApplication;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.Str;

// r => Request , in baraye sabt request jadid , bazi request ha dar baze zamani kotah nabayad ziyad darkhast esral beshe
public final class r extends Thread implements Runnable
{

    private String ip;
    private SubmitRequestType type;
    private boolean active;
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
        SubmitRequestService service = CyrusMessengerApplication.Context ().getBean (SubmitRequestService.class);

        if (ip == null || ip.isEmpty ()) service.newRequest (ip , type , active);
        else service.newRequest (mainAccount , type , active);
    }
}
