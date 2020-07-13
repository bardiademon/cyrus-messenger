package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount;

public enum UserGender
{
    lady, gentleman, bisexual, do_not_want, not_specified;

    public static UserGender to (String name)
    {
        try
        {
            return valueOf (name);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
