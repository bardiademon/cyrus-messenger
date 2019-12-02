package com.bardiademon.CyrusMessenger.Controller.Vaidation;

import java.util.regex.Pattern;

public class VUsername implements Validation
{
    private String username;


    public VUsername (String Username)
    {
        this.username = Username;
    }

    @Override
    public boolean check ()
    {
        if (username == null || username.equals ("")) return false;
        else
        {
            Pattern pattern = Pattern.compile ("[A-Za-z0-9_]+");
            return (username != null) && pattern.matcher (username).matches ();
        }
    }
}
