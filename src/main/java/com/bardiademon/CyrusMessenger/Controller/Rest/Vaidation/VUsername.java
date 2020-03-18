package com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation;

import java.util.regex.Pattern;

public class VUsername implements Validation
{
    private String username;

    private static final int MIN_LEN_USERNAME = 5;

    public VUsername (String Username)
    {
        this.username = Username;
    }

    @Override
    public boolean check ()
    {
        if (username == null || username.equals ("") || username.length () < MIN_LEN_USERNAME) return false;
        else
        {
            String substring = username.substring (0 , 1);
            if ((!substring.matches ("[A-Z]")) && !substring.matches ("[a-z]") && !substring.equals ("_")) return false;

            Pattern pattern = Pattern.compile ("[A-Za-z0-9_]+");
            return (username != null) && pattern.matcher (username).matches ();
        }
    }
}
