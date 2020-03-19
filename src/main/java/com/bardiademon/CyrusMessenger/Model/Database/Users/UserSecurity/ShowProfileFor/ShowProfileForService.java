package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor;

import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Update.Security.Profile.ListShowFor.Add.RequestAddShowProfileFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileRepository;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ShowProfileForService
{
    public final ShowProfileForRepository Repository;

    @Autowired
    public ShowProfileForService (ShowProfileForRepository Repository)
    {
        this.Repository = Repository;
    }


    public void add (List<Long> ids , SecurityUserProfileRepository securityUserProfileRepository , MainAccount mainAccount , RequestAddShowProfileFor.Add add)
    {
        ShowProfileFor bySecurityUserProfile = Repository.findBySecurityUserProfile (securityUserProfileRepository.findByMainAccount (mainAccount));
        if (add.equals (RequestAddShowProfileFor.Add.list_all_except))
        {
            String showJust = bySecurityUserProfile.getShowAllExcept ();
            if (showJust != null)
            {
                String[] idsStr = showJust.split (ShowProfileFor.IsolationWith);
                for (String str : idsStr) ids.remove (Long.parseLong (str));
                bySecurityUserProfile.setShowAllExcept (toString (ids , new ArrayList<> (Arrays.asList (idsStr))));
            }
            else bySecurityUserProfile.setShowAllExcept (toString (ids , new ArrayList<> ()));
        }
        else if (add.equals (RequestAddShowProfileFor.Add.show_just))
        {
            String showJust = bySecurityUserProfile.getShowJust ();
            if (showJust != null)
            {
                String[] idsStr = showJust.split (ShowProfileFor.IsolationWith);
                for (String str : idsStr) ids.remove (Long.parseLong (str));
                bySecurityUserProfile.setShowJust (toString (ids , new ArrayList<> (Arrays.asList (idsStr))));
            }
            else
                bySecurityUserProfile.setShowJust (toString (ids , new ArrayList<> ()));
        }
        ShowProfileFor save = Repository.save (bySecurityUserProfile);

        if (add.equals (RequestAddShowProfileFor.Add.list_all_except))
            delete (ids , save , RequestAddShowProfileFor.Add.show_just);
        else if (add.equals (RequestAddShowProfileFor.Add.show_just))
            delete (ids , save , RequestAddShowProfileFor.Add.list_all_except);
    }

    private String toString (List<Long> ids , List<String> idsStr)
    {
        if (ids != null && ids.size () > 0) for (Long id : ids) idsStr.add (String.valueOf (id));

        StringBuilder lstStr;
        lstStr = new StringBuilder ();
        for (int i = 0, len = idsStr.size (); i < len; i++)
        {
            String id = idsStr.get (i);
            lstStr.append (id);
            if (i + 1 < len) lstStr.append (",");
        }

        return lstStr.toString ();
    }

    private void delete (List<Long> ids , ShowProfileFor showProfileFor , RequestAddShowProfileFor.Add add)
    {
        ArrayList<String> list;
        if (add.equals (RequestAddShowProfileFor.Add.list_all_except))
        {
            list = new ArrayList<> (Arrays.asList (showProfileFor.getShowAllExcept ().split (ShowProfileFor.IsolationWith)));

            for (Long id : ids) list.remove (String.valueOf (id));

            showProfileFor.setShowAllExcept (toString (null , list));
        }
        else if (add.equals (RequestAddShowProfileFor.Add.show_just))
        {
            list = new ArrayList<> (Arrays.asList (showProfileFor.getShowJust ().split (ShowProfileFor.IsolationWith)));
            for (Long id : ids) list.remove (String.valueOf (id));
            showProfileFor.setShowJust (toString (null , list));
        }
        Repository.save (showProfileFor);
    }
}
