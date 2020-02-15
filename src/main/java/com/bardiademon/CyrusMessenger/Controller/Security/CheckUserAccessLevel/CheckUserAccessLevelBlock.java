package com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.bardiademon.Time;

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
        UserBlocked.Type type = toType ();
        if (type == null) hasAccess = true;
        else
        {
            UserBlocked blocked = userBlockedService.isBlocked (mainAccountToCheck.getId () , mainAccountRequest.getId () , type);

            if (blocked == null)
                hasAccess = true;
            else
            {
                if (Time.BiggerNow (blocked.getValidityTime ()))
                {
                    blocked.setUnblocked (true);
                    blocked.setUnblocked (true);
                    userBlockedService.Repository.save (blocked);
                    hasAccess = true;
                }
                else hasAccess = false;
            }
        }
    }

    public UserBlocked.Type toType ()
    {
        if (checkChat != null && checkChat.equals (CheckUserAccessLevel.CheckChat.send_message))
            return UserBlocked.Type.cns_send_message;
        else
        {
            switch (checkProfile)
            {
                case bio:
                    return UserBlocked.Type.cns_bio;
                case show_phone:
                    return UserBlocked.Type.cns_phone;
                case show_name:
                    return UserBlocked.Type.cns_name;
                case show_username:
                    return UserBlocked.Type.cns_username;
                case show_profile:
                    return UserBlocked.Type.cns_profile;
                case show_email:
                    return UserBlocked.Type.cns_email;
                case show_mylink:
                    return UserBlocked.Type.cns_mylink;
                case show_family:
                    return UserBlocked.Type.cns_family;
                case cover:
                    return UserBlocked.Type.cns_cover;
                default:
                    return UserBlocked.Type.all;
            }
        }
    }

    public boolean hasAccess ()
    {
        return hasAccess;
    }
}
