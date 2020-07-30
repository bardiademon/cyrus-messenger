package com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.ProfilePictures;

import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;

import java.util.ArrayList;
import java.util.List;

public final class SortProfilePictures
{
    private final ProfilePicturesService service;
    private final List<ProfilePictures> profilePictures;
    private List<ProfilePictures> newProfilePictures;
    private ProfilePictures mainProfilePicture;

    public SortProfilePictures (List<ProfilePictures> _ProfilePictures)
    {
        this (null , _ProfilePictures);
    }

    public SortProfilePictures (ProfilePicturesService Service , List<ProfilePictures> _ProfilePictures)
    {
        this.service = Service;
        this.profilePictures = _ProfilePictures;
        if ((profilePictures != null && profilePictures.size () > 0)) sort ();
    }

    private void sort ()
    {
        List<ProfilePictures> zeroProfilePictures = new ArrayList<> ();
        List<ProfilePictures> notZeroProfilePictures = new ArrayList<> ();
        for (ProfilePictures profilePicture : profilePictures)
        {
            if (profilePicture.getId () <= 0) continue;
            if (profilePicture.isMainPic ()) mainProfilePicture = profilePicture;
            else if (profilePicture.getPlacementNumber () == 0) zeroProfilePictures.add (profilePicture);
            else notZeroProfilePictures.add (profilePicture);
        }

        ProfilePictures iProfilePicture, jProfilePicture;
        for (int i = 0, len = notZeroProfilePictures.size (); i < len; i++)
        {
            iProfilePicture = notZeroProfilePictures.get (i);
            for (int j = i; j < len; j++)
            {
                jProfilePicture = notZeroProfilePictures.get (j);
                if (iProfilePicture.getPlacementNumber () > jProfilePicture.getPlacementNumber ())
                {
                    notZeroProfilePictures.set (i , jProfilePicture);
                    notZeroProfilePictures.set (j , iProfilePicture);
                    break;
                }
            }
        }

        ProfilePictures profilePictures;
        for (int i = 0; i < notZeroProfilePictures.size () - 1; i++)
        {
            profilePictures = notZeroProfilePictures.get (i);
            profilePictures.setPlacementNumber ((i + 1));
            notZeroProfilePictures.set (i , profilePictures);
        }

        newProfilePictures = new ArrayList<> ();

        if (mainProfilePicture != null) newProfilePictures.add (mainProfilePicture);
        if (notZeroProfilePictures.size () > 0) newProfilePictures.addAll (notZeroProfilePictures);
        if (zeroProfilePictures.size () > 0) newProfilePictures.addAll (zeroProfilePictures);

        if (service != null) service.Repository.saveAll (this.newProfilePictures);
    }

    public List<ProfilePictures> getNewProfilePictures ()
    {
        return newProfilePictures;
    }

    public ProfilePictures getMainProfilePicture ()
    {
        return mainProfilePicture;
    }

    public List<Long> getIds ()
    {
        if (newProfilePictures != null && newProfilePictures.size () > 0)
        {
            List<Long> profilePicturesIds = new ArrayList<> ();
            for (ProfilePictures profilePicture : newProfilePictures) profilePicturesIds.add (profilePicture.getId ());
            return profilePicturesIds;
        }
        else return null;
    }
}
