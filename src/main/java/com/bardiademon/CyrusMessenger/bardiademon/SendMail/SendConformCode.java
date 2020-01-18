package com.bardiademon.CyrusMessenger.bardiademon.SendMail;

import com.bardiademon.CyrusMessenger.bardiademon.Default.Default;

public class SendConformCode extends SendMail
{
    private String code;

    private static final String MESSAGE = "کد شما برای تایید ایمیل";

    public SendConformCode (String To , String Code)
    {
        super (To);
        this.code = Code;
        sendMail ();
    }

    private void sendMail ()
    {
        new Thread (() ->
        {
            String message = MESSAGE + "\n" + "Your code: " + code;
            String subject = Default.NameEnglish;

            setContentType (SendMail.CONTENT_TEXT_PLAIN);
            setMsg (message);
            setSubject (subject);

            SendConformCode.super.send ();
        }).start ();
    }

}
