package com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures;

import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.ProfilePictures.SortProfilePictures;

import java.util.List;

public final class GetOneProfilePicture
{
    private List<ProfilePictures> profilePictures;

    private ProfilePictures profilePicture;

    private boolean wasGet;

    public GetOneProfilePicture (List<ProfilePictures> _ProfilePictures)
    {
        this.profilePictures = _ProfilePictures;

        wasGet = get ();
    }


    private boolean get ()
    {
        if (profilePictures == null || profilePictures.size () == 0)
            return false;
        else
        {
            SortProfilePictures sortProfilePictures = new SortProfilePictures (profilePictures);
            profilePictures = sortProfilePictures.getNewProfilePictures ();
            profilePicture = profilePictures.get (0);
            return true;
        }
    }


    public boolean wasGet ()
    {
        return wasGet;
    }

    public ProfilePictures getProfilePicture ()
    {
        return profilePicture;
    }
}
