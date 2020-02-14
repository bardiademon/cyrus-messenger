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
        List<ProfilePictures> zeroPlacementNumber = new ArrayList<> ();
        List<ProfilePictures> newProfilePictures = new ArrayList<> ();
        for (ProfilePictures profilePicture : profilePictures)
        {
            if (profilePicture.getId () <= 0) continue;
            if (profilePicture.isMainPic ()) mainProfilePicture = profilePicture;
            else if (profilePicture.getPlacementNumber () == 0) zeroPlacementNumber.add (profilePicture);
            else newProfilePictures.add (profilePicture);
        }

        ProfilePictures iProfilePicture, jProfilePicture;
        for (int i = 0; i < newProfilePictures.size () - 1; i++)
        {
            iProfilePicture = newProfilePictures.get (i);
            for (int j = (i + 1); j < newProfilePictures.size (); j++)
            {
                jProfilePicture = newProfilePictures.get (j);

                if (iProfilePicture.getPlacementNumber () > jProfilePicture.getPlacementNumber ())
                {
                    newProfilePictures.set (i , jProfilePicture);
                    newProfilePictures.set (j , iProfilePicture);
                }
            }
        }

        ProfilePictures profilePictures;
        for (int i = 0; i < newProfilePictures.size () - 1; i++)
        {
            profilePictures = newProfilePictures.get (i);
            profilePictures.setPlacementNumber ((i + 1));
            newProfilePictures.set (i , profilePictures);
        }

        this.newProfilePictures = new ArrayList<> ();

        if (mainProfilePicture != null) this.newProfilePictures.add (mainProfilePicture);
        this.newProfilePictures.addAll (newProfilePictures);
        if (zeroPlacementNumber.size () > 0) this.newProfilePictures.addAll (zeroPlacementNumber);

        if (service != null) service.Repository.saveAll (this.newProfilePictures);
    }

    public List<ProfilePictures> getNewProfilePictures ()
    {
        return newProfilePictures;
    }
}
