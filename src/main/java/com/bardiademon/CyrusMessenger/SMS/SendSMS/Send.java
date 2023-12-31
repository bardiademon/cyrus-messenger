package com.bardiademon.CyrusMessenger.SMS.SendSMS;

public class Send
{

    private String text;

    private String[] phone;

    private boolean send;
    private boolean delivered;
    private boolean error;

    public Send (String Text , String... Phone)
    {
        this.text = Text;
        this.phone = Phone;
        send ();
    }


    private void send ()
    {
        new Thread (() ->
        {
            send = true;
            delivered = true;
            error = false;
        }).start ();
    }

    public boolean isSend ()
    {
        return send;
    }

    public boolean isDelivered ()
    {
        return delivered;
    }

    public boolean isError ()
    {
        return error;
    }
}
