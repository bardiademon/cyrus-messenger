package com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation;

import java.util.regex.Pattern;

public final class VUsername implements Validation
{
    private final String username;

    private static final int MIN_LEN_USERNAME = 5;

    public VUsername (final String Username)
    {
        this.username = Username;
    }

    @Override
    public boolean check ()
    {
        if (username == null || username.equals ("") || username.length () < MIN_LEN_USERNAME) return false;
        else
        {
            final String substring = username.substring (0 , 1);
            if ((!substring.matches ("[A-Z]")) && !substring.matches ("[a-z]") && !substring.equals ("_")) return false;

            return (Pattern.compile ("[A-Za-z0-9_]+")).matcher (username).matches ();
        }
    }
}
