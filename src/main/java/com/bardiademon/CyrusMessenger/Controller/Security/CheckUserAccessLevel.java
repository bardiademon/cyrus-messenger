package com.bardiademon.CyrusMessenger.Controller.Security;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;

import java.util.Arrays;

public class CheckUserAccessLevel
{
    private CheckProfile checkProfile;

    private CheckChat checkChat;

    public final int CHK_CHAT = 0, CHK_PROFILE = 1;

    private MainAccount mainAccountWhoRequested, mainAccountToCheck;

    private final String userWhoRequested, userToCheck;
    private MainAccountService mainAccountService;
    private SecurityUserChatService securityUserChatService;
    private SecurityUserProfileService securityUserProfileService;

    private ShowChatForService showChatForService;
    private ShowProfileForService showProfileForService;

    private SecurityUserProfile securityUserProfile;

    private ShowProfileFor showProfileFor;

    private SecurityUserChat securityUserChat;

    public CheckUserAccessLevel (String UserWhoRequested , String UserToCheck , MainAccountService mainAccountService)
    {
        this.userWhoRequested = UserWhoRequested;
        this.userToCheck = UserToCheck;
        setService (mainAccountService);
    }

    private void setService (MainAccountService service)
    {
        this.mainAccountService = service;
    }

    public void setServiceSecurityUserChat (SecurityUserChatService service)
    {
        securityUserChatService = service;
    }

    public void setServiceSecurityUserProfile (SecurityUserProfileService service)
    {
        securityUserProfileService = service;
    }

    public void setServiceShowChatFor (ShowChatForService service)
    {
        showChatForService = service;
    }

    public void setServiceShowProfileFor (ShowProfileForService service)
    {
        showProfileForService = service;
    }

    public void setCheckProfile (CheckProfile checkProfile)
    {
        this.checkProfile = checkProfile;
    }

    public void setCheckChat (CheckChat checkChat)
    {
        this.checkChat = checkChat;
    }

    public boolean check (int checkProfileOrChat)
    {
        mainAccountWhoRequested = mainAccountService.Repository.findByUsername (userWhoRequested);
        mainAccountToCheck = mainAccountService.Repository.findByUsername (userToCheck);
        if (mainAccountWhoRequested == null || mainAccountToCheck == null) return false;
        else
        {
            if (checkProfileOrChat == CHK_PROFILE) return checkProfile ();
            else if (checkProfileOrChat == CHK_CHAT) return checkChat ();
            else return false;
        }
    }

    private boolean checkProfile ()
    {
        if (securityUserProfileService == null) return false;
        else
        {
            securityUserProfile =
                    securityUserProfileService.Repository.findByMainAccount (mainAccountToCheck);

            if (securityUserProfile == null) return false;
            return checkSecurityProfile ();
        }
    }

    private boolean checkSecurityProfile ()
    {
        if (checkProfile == null) return false;
        else
        {
            switch (checkProfile)
            {
                case bio:
                    return checkAccessLevelProfile (securityUserProfile.getShowBio ());
                case cover:
                    return checkAccessLevelProfile (securityUserProfile.getShowCover ());
                case show_in_channel:
                    return checkAccessLevelProfile (securityUserProfile.getShowInChannel ());
                case show_in_group:
                    return checkAccessLevelProfile (securityUserProfile.getShowInGroup ());
                case show_profile:
                    return checkAccessLevelProfile (securityUserProfile.getShowInProfile ());
                case show_in_search:
                    return checkAccessLevelProfile (securityUserProfile.getShowInSearch ());
                case show_last_seen:
                    return checkAccessLevelProfile (securityUserProfile.getShowLastSeen ());
                case show_mylink:
                    return checkAccessLevelProfile (securityUserProfile.getShowMyLink ());
                case show_name:
                    return checkAccessLevelProfile (securityUserProfile.getShowName ());
                case show_personal_information:
                    return checkAccessLevelProfile (securityUserProfile.getShowPersonalInformation ());
                case show_phone:
                    return checkAccessLevelProfile (securityUserProfile.getShowPhone ());
                case show_seen_message:
                    return checkAccessLevelProfile (securityUserProfile.getShowSeenMessage ());
                case show_username:
                    return checkAccessLevelProfile (securityUserProfile.getShowUsername ());
                default:
                    return false;
            }
        }
    }

    private boolean checkAccessLevelProfile (AccessLevel accessLevel)
    {
        if (accessLevel.equals (AccessLevel.all)) return true;
        else
        {
            SecurityUserProfile byMainAccount
                    = securityUserProfileService.Repository.findByMainAccount (mainAccountWhoRequested);
            if (byMainAccount == null) return false;
            else
            {
                if (showProfileForService == null) return false;

                showProfileFor
                        = showProfileForService.Repository.findBySecurityUserProfile (byMainAccount);
                if (showProfileFor == null) return false;

                return checkFinal (accessLevel);
            }
        }
    }


    private boolean checkFinal (AccessLevel accessLevel)
    {
        String result;
        if (accessLevel.equals (AccessLevel.all_except))
            result = showProfileFor.getShowAllExcept ();
        else if (accessLevel.equals (AccessLevel.just_my_list))
            result = showProfileFor.getShowJust ();
        else if (accessLevel.equals (AccessLevel.just_list_friends))
            result = showProfileFor.getShowJustFriends ();
        else if (accessLevel.equals (AccessLevel.not)) return false;
        else return false;

        return search (result.split (ShowProfileFor.IsolationWith));
    }

    private boolean search (String[] listExcept)
    {
        Arrays.sort (listExcept);
        return Arrays.binarySearch (listExcept , mainAccountWhoRequested.getId ()) < 0;
    }

    private boolean checkChat ()
    {
        if (securityUserChatService == null) return false;
        else
        {
            securityUserChat
                    = securityUserChatService.Repository.findByMainAccount (mainAccountToCheck);

            if (securityUserChat == null) return false;
            else return checkSecurityChat ();
        }
    }

    private boolean checkSecurityChat ()
    {
        if (checkChat == null) return false;
        else
        {
            switch (checkChat)
            {
                case send_message:
                    return checkAccessLevelChat (securityUserChat.getCanSendMessage ());
                case send_emoji:
                    return checkAccessLevelChat (securityUserChat.getCanSendEmoji ());
                case send_file:
                    return checkAccessLevelChat (securityUserChat.getCanSendFile ());
                case send_gif:
                    return checkAccessLevelChat (securityUserChat.getCanSendGif ());
                case send_image:
                    return checkAccessLevelChat (securityUserChat.getCanSendImage ());
                case send_invitation:
                    return checkAccessLevelChat (securityUserChat.getCanSendInvitation ());
                case send_link:
                    return checkAccessLevelChat (securityUserChat.getCanSendLink ());
                case send_sticker:
                    return checkAccessLevelChat (securityUserChat.getCanSendSticker ());
                case send_voice:
                    return checkAccessLevelChat (securityUserChat.getCanSendVoice ());
                default:
                    return false;
            }
        }
    }

    private boolean checkAccessLevelChat (AccessLevel accessLevel)
    {
        if (accessLevel.equals (AccessLevel.all)) return true;
        else
        {
            SecurityUserChat byMainAccount =
                    securityUserChatService.Repository.findByMainAccount (mainAccountWhoRequested);
            if (byMainAccount == null) return false;
            else
            {
                if (showChatForService == null) return false;

                showProfileFor
                        = showChatForService.Repository.findBySecurityUserChat (byMainAccount);
                if (showProfileFor == null) return false;

                return checkFinal (accessLevel);
            }
        }
    }

    public enum CheckProfile
    {
        bio, cover, show_in_channel, show_in_group, show_profile,
        show_in_search, show_last_seen, show_mylink, show_name, show_personal_information,
        show_phone, show_seen_message, show_username
    }

    public enum CheckChat
    {
        send_emoji, send_file, send_gif, send_image, send_invitation,
        send_link, send_message, send_sticker, send_voice
    }

}
