package com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures;

import com.bardiademon.CyrusMessenger.bardiademon.Str;

public enum ProfilePicFor
{
    user, group, channel;

    public static ProfilePicFor to (String profilePicFor)
    {
        try
        {
            if (Str.IsEmpty (profilePicFor)) throw new Exception ();

            return valueOf (profilePicFor);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
