package com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel;

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
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContacts;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;

import java.util.Arrays;

public class CheckUserAccessLevel
{
    private CheckProfile checkProfile;

    private CheckChat checkChat;

    public final int CHK_CHAT = 0, CHK_PROFILE = 1;

    private MainAccount mainAccountWhoRequested, mainAccountToCheck;

    private String userWhoRequested, userToCheck;

    private MainAccountService mainAccountService;
    private SecurityUserChatService securityUserChatService;

    private ShowChatForService showChatForService;

    private ShowProfileFor showProfileFor;

    private SecurityUserChat securityUserChat;

    private ServiceProfile serviceProfile;

    private SecurityUserProfile securityUserProfile;

    public CheckUserAccessLevel (String UserWhoRequested , String UserToCheck , MainAccountService _MainAccountService)
    {
        this.userWhoRequested = UserWhoRequested;
        this.userToCheck = UserToCheck;
        setService (_MainAccountService);
    }

    public CheckUserAccessLevel (MainAccount MainAccountWhoRequested , MainAccount MainAccountToCheck , MainAccountService _MainAccountService)
    {
        this.mainAccountWhoRequested = MainAccountWhoRequested;
        this.mainAccountToCheck = MainAccountToCheck;
        setService (_MainAccountService);
    }

    private void setService (MainAccountService service)
    {
        this.mainAccountService = service;
    }

    public void setServiceSecurityUserChat (SecurityUserChatService service)
    {
        securityUserChatService = service;
    }

    public void setServiceShowChatFor (ShowChatForService service)
    {
        showChatForService = service;
    }

    public void setServiceProfile (ServiceProfile serviceProfile)
    {
        this.serviceProfile = serviceProfile;
    }

    public void setCheckProfile (CheckProfile checkProfile)
    {
        this.checkProfile = checkProfile;
    }

    public void setCheckChat (CheckChat checkChat)
    {
        this.checkChat = checkChat;
    }

    public boolean hasAccessProfile (CheckProfile checkProfile)
    {
        setCheckProfile (checkProfile);
        return check (CHK_PROFILE);
    }

    public boolean check (int checkProfileOrChat)
    {
        if (mainAccountWhoRequested == null)
            mainAccountWhoRequested = mainAccountService.findUsername (userWhoRequested);

        if (mainAccountToCheck == null)
            mainAccountToCheck = mainAccountService.findUsername (userToCheck);

        if (mainAccountWhoRequested == null || mainAccountToCheck == null) return false;

        if (mainAccountWhoRequested.getId () == mainAccountToCheck.getId ()) return true;
        else
        {
            CheckUserAccessLevelBlock accessLevelBlock =
                    new CheckUserAccessLevelBlock (mainAccountWhoRequested , mainAccountToCheck , serviceProfile._UserBlockedService , checkProfile , checkChat);

            if (accessLevelBlock.hasAccess ())
            {
                if (checkProfileOrChat == CHK_PROFILE) return checkProfile ();
                else if (checkProfileOrChat == CHK_CHAT) return checkChat ();
                else return false;
            }
            else return false;
        }
    }

    private boolean checkProfile ()
    {
        if (serviceProfile._SecurityUserProfileService == null) return false;
        else
        {
            securityUserProfile =
                    serviceProfile._SecurityUserProfileService.Repository.findByMainAccount (mainAccountToCheck);

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
                case show_family:
                    return checkAccessLevelProfile (securityUserProfile.getShowFamily ());
                case show_email:
                    return checkAccessLevelProfile (securityUserProfile.getShowEmail ());
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
        else if (accessLevel.equals (AccessLevel.not)) return false;
        else
        {
            SecurityUserProfile byMainAccount
                    = serviceProfile._SecurityUserProfileService.Repository.findByMainAccount (mainAccountToCheck);

            if (byMainAccount == null) return false;
            else
            {
                if (serviceProfile._ShowProfileForService == null) return false;
                showProfileFor
                        = serviceProfile._ShowProfileForService.Repository.findBySecurityUserProfile (byMainAccount);
                if (showProfileFor == null) return false;

                return checkFinal (accessLevel);
            }
        }
    }


    private boolean checkFinal (AccessLevel accessLevel)
    {
        String result;
        boolean has = true;
        if (accessLevel.equals (AccessLevel.just_contacts) || accessLevel.equals (AccessLevel.all_except_contacts))
        {
            UserContacts contact = serviceProfile._UserContactsService.findContact (mainAccountToCheck.getId () , mainAccountWhoRequested.getId ());
            if (accessLevel.equals (AccessLevel.just_contacts)) return contact != null;
            else return contact == null;
        }
        else if (accessLevel.equals (AccessLevel.all_except))
        {
            result = showProfileFor.getShowAllExcept ();
            has = false;
        }
        else if (accessLevel.equals (AccessLevel.just_my_list))
            result = showProfileFor.getShowJust ();
        else if (accessLevel.equals (AccessLevel.just_list_friends))
            return ((serviceProfile._UserFriendsService.findFriend (mainAccountWhoRequested , mainAccountToCheck , StatusFriends.friend)) != null);
        else return false;

        boolean resultIsNull = (result == null || result.equals (""));
        if (!has && resultIsNull) return true;
        else if (has && resultIsNull) return false;

        return search (result.split (ShowProfileFor.IsolationWith) , has);
    }

    private boolean search (String[] listExcept , boolean has)
    {
        boolean listIsNull = (listExcept == null || listExcept.length == 0);
        if (!has && listIsNull) return true;
        else if (has && listIsNull) return false;

        boolean resultSearch = Arrays.asList (listExcept).contains (String.valueOf (mainAccountWhoRequested.getId ()));

        System.out.println (resultSearch);

        if (has && resultSearch) return true;
        else return (!has && !resultSearch);
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
        show_in_search, show_last_seen, show_mylink, show_name, show_family, show_email, show_personal_information,
        show_phone, show_seen_message, show_username
    }

    public enum CheckChat
    {
        send_emoji, send_file, send_gif, send_image, send_invitation,
        send_link, send_message, send_sticker, send_voice
    }

    public static class Service
    {
        public final UserContactsService _UserContactsService;
        public final UserFriendsService _UserFriendsService;
        public final UserBlockedService _UserBlockedService;

        public Service (UserContactsService _UserContactsService , UserFriendsService _UserFriendsService , UserBlockedService userBlockedService)
        {
            this._UserContactsService = _UserContactsService;
            this._UserFriendsService = _UserFriendsService;
            _UserBlockedService = userBlockedService;
        }
    }

    public static class ServiceProfile extends Service
    {
        public final ShowProfileForService _ShowProfileForService;
        public final SecurityUserProfileService _SecurityUserProfileService;

        public ServiceProfile
                (ShowProfileForService _ShowProfileForService ,
                 UserContactsService _UserContactsService ,
                 UserFriendsService _UserFriendsService ,
                 SecurityUserProfileService _SecurityUserProfileService , UserBlockedService _UserBlockedService)
        {
            super (_UserContactsService , _UserFriendsService , _UserBlockedService);
            this._ShowProfileForService = _ShowProfileForService;
            this._SecurityUserProfileService = _SecurityUserProfileService;
        }

    }

}
