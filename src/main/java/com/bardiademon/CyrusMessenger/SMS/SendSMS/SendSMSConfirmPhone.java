package com.bardiademon.CyrusMessenger.SMS.SendSMS;

import com.bardiademon.CyrusMessenger.bardiademon.Default.Default;
import com.bardiademon.CyrusMessenger.Code;

public class SendSMSConfirmPhone extends Send
{

    private static long code;

    public SendSMSConfirmPhone (String Name , String Phone)
    {
        super (createText (Name) , Phone);
    }

    private static String createText (String name)
    {
        StringBuilder text = new StringBuilder ();

        text.append ("Hi, ").append (name);
        text.append ("\n\n");
        text.append ("Your code: ");
        text.append ((code = Code.CodeNumber ()));
        text.append ("\n\n");
        text.append (Default.NameEnglish);

        return text.toString ();
    }

    public static long getCode ()
    {
        return code;
    }
}
