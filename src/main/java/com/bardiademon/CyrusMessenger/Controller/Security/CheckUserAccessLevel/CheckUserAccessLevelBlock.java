package com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;

public final class CheckUserAccessLevelBlock
{
    private MainAccount mainAccountRequest;
    private MainAccount mainAccountToCheck;
    private UserBlockedService userBlockedService;
    private CheckUserAccessLevel.CheckProfile checkProfile;
    private CheckUserAccessLevel.CheckChat checkChat;

    private boolean hasAccess;

    public CheckUserAccessLevelBlock (
            MainAccount MainAccountRequest ,
            MainAccount MainAccountToCheck ,
            UserBlockedService _UserBlockedService ,
            CheckUserAccessLevel.CheckProfile _CheckProfile ,
            CheckUserAccessLevel.CheckChat _CheckChat)
    {
        this.mainAccountRequest = MainAccountRequest;
        this.mainAccountToCheck = MainAccountToCheck;
        this.userBlockedService = _UserBlockedService;
        this.checkProfile = _CheckProfile;
        this.checkChat = _CheckChat;
        check ();
    }

    private void check ()
    {
        UserBlocked blocked = userBlockedService.isBlocked (mainAccountToCheck.getId () , mainAccountRequest.getId ());
        if (blocked == null)
            hasAccess = true;
        else
        {
            if (blocked.getType ().equals (UserBlocked.Type.all)) hasAccess = false;
            else
            {
                UserBlocked.Type type = blocked.getType ();
                switch (type)
                {
                    case cns_bio:
                    {
                        if (checkProfile != null && checkProfile.equals (CheckUserAccessLevel.CheckProfile.bio))
                            hasAccess = false;
                        break;
                    }
                    case cns_cover:
                    {
                        if (checkProfile != null && checkProfile.equals (CheckUserAccessLevel.CheckProfile.cover))
                            hasAccess = false;
                        break;
                    }
                    case cns_phone:
                    {
                        if (checkProfile != null && checkProfile.equals (CheckUserAccessLevel.CheckProfile.show_phone))
                            hasAccess = false;
                        break;
                    }
                    case cns_profile:
                    {
                        if (checkProfile != null && checkProfile.equals (CheckUserAccessLevel.CheckProfile.show_profile))
                            hasAccess = false;
                        break;
                    }
                    case cns_send_message:
                    {
                        if (checkChat == null) hasAccess = true;
                        else if (checkChat.equals (CheckUserAccessLevel.CheckChat.send_message))
                            hasAccess = false;
                        break;
                    }
                    case cns_username:
                    {
                        if (checkProfile != null && checkProfile.equals (CheckUserAccessLevel.CheckProfile.show_username))
                            hasAccess = false;
                        break;
                    }
                    case cns_email:
                    {
                        if (checkProfile != null && checkProfile.equals (CheckUserAccessLevel.CheckProfile.show_email))
                            hasAccess = false;
                        break;
                    }
                    case cns_family:
                    {
                        if (checkProfile != null && checkProfile.equals (CheckUserAccessLevel.CheckProfile.show_family))
                            hasAccess = false;
                        break;
                    }
                    case cns_mylink:
                    {
                        if (checkProfile != null && checkProfile.equals (CheckUserAccessLevel.CheckProfile.show_mylink))
                            hasAccess = false;
                        break;
                    }
                    case cns_name:
                    {
                        if (checkProfile != null && checkProfile.equals (CheckUserAccessLevel.CheckProfile.show_name))
                            hasAccess = false;
                        break;
                    }
                    default:
                    {
                        hasAccess = true;
                        break;
                    }
                }
            }
        }
    }

    public boolean hasAccess ()
    {
        return hasAccess;
    }
}
