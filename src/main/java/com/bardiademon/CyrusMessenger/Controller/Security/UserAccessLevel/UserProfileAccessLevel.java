package com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel;

import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypes;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.DesEnumTypes;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserList;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.ThisApp;
import java.util.List;
import java.util.Objects;

public class UserProfileAccessLevel
{
    public final static Service _Service = new Service ();
    protected MainAccount applicant;
    protected MainAccount user;
    protected Which which;

    protected String desEnumTypes;
    protected String userBlockedType;

    private UserSeparateProfiles separateProfile;
    private List <ProfilePictures> profilePictures;

    private boolean isAnonymous;

    public UserProfileAccessLevel (MainAccount Applicant)
    {
        this (Applicant , null);
    }

    public UserProfileAccessLevel (MainAccount Applicant , MainAccount User)
    {
        this.applicant = Applicant;
        setUser (User);
    }

    public void setUser (MainAccount user)
    {
        this.user = user;
    }

    public MainAccount getUser ()
    {
        return user;
    }

    public void setApplicant (MainAccount applicant)
    {
        this.applicant = applicant;
    }

    public boolean hasAccess (Which... which)
    {
        for (Which wh : which) if (!hasAccess (wh)) return false;
        return true;
    }

    public boolean hasAccess (Which which)
    {
        this.which = which;

        if (this.applicant == null || this.user == null) return false;

        if (this.applicant.getId () == this.user.getId ())
            return true;

        final SecurityUserProfile securityUser = _Service.mainAccountService.repositorySecurityProfile.findByMainAccount (user);
        final AccessLevel accessLevel = getAccessLevel (securityUser);

        return (securityUser != null && accessLevel != null) && (!isBlock () && hasAccess (accessLevel , securityUser.isShowProfileForAnonymous ()));
    }

    protected boolean isBlock ()
    {
        if (_Service.userBlockedService.isBlocked (user.getId () , applicant.getId () , UserBlocked.Type.valueOf (userBlockedType)) == null)
        {
            if (!userBlockedType.equals (UserBlocked.Type.all.name ()))
                return (_Service.userBlockedService.isBlocked (user.getId () , applicant.getId () , UserBlocked.Type.all) != null);
            else return false;
        }
        else return true;
    }

    protected boolean hasAccess (AccessLevel accessLevel , boolean showForAnonymous)
    {
        assert desEnumTypes != null;

        isAnonymous = isAnonymous ();

        if (isAnonymous && !showForAnonymous) return false;

        if (accessLevel.equals (AccessLevel.all)) return true;
        else
        {
            UserList userList = _Service.userListService.getUserList (user.getId () , applicant.getId ());
            if (accessLevel.equals (AccessLevel.all_except))
            {
                return userList == null;
            }
            else if (accessLevel.equals (AccessLevel.just)) return userList != null;
            else
            {
                List <UserSeparateProfiles> separateProfiles
                        = _Service.userSeparateProfilesService.Repository.findByMainAccountIdAndDeletedFalse (user.getId ());

                Boolean hasSeparately = hasSeparately (separateProfiles);

                if (accessLevel.equals (AccessLevel.separately))
                {
                    if (which.equals (Which.cover))
                    {
                        List <ProfilePictures> separate = _Service.profilePicturesService.getSeparate (user.getId ());
                        List <EnumTypes> byId2AndDeletedFalse;
                        if (separate != null && separate.size () > 0)
                        {
                            for (ProfilePictures pictures : separate)
                            {
                                byId2AndDeletedFalse = _Service.enumTypesService.Repository.findById2AndDeletedFalse (pictures.getId ());
                                if (byId2AndDeletedFalse != null && checkEnumTypesCheck (byId2AndDeletedFalse))
                                {
                                    this.profilePictures = separate;
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                    else return Objects.requireNonNullElse (hasSeparately , false);
                }
                else if (accessLevel.equals (AccessLevel.all_separately))
                {
                    if (which.equals (Which.cover))
                    {
                        List <ProfilePictures> separate = _Service.profilePicturesService.getSeparate (user.getId ());
                        List <EnumTypes> byId2AndDeletedFalse;
                        if (separate != null && separate.size () > 0)
                        {
                            for (ProfilePictures pictures : separate)
                            {
                                byId2AndDeletedFalse = _Service.enumTypesService.Repository.findById2AndDeletedFalse (pictures.getId ());
                                if (byId2AndDeletedFalse != null && checkEnumTypesCheck (byId2AndDeletedFalse))
                                {
                                    this.profilePictures = separate;
                                    return true;
                                }
                            }
                        }
                        return true;
                    }
                    else return Objects.requireNonNullElse (hasSeparately , true);
                }
                else return false;
            }
        }
    }

    protected boolean checkEnumTypesCheck (List <EnumTypes> enumTypes)
    {
        String strEnumType;
        for (EnumTypes enumType : enumTypes)
        {
            strEnumType = enumType.getEnumType ();
            if (!isAnonymous && strEnumType.equals (AccessLevel.Who.family.name ()))
            {
                if (hasInList (UserListType.family)) return true;
            }
            else if (!isAnonymous && strEnumType.equals (AccessLevel.Who.friend.name ()))
            {
                if (_Service.userFriendsService.findFriend (user , applicant , StatusFriends.friend) != null)
                    return true;
            }
            else if (!isAnonymous && strEnumType.equals (AccessLevel.Who.contact.name ()))
            {
                if (_Service.userContactsService.findContact (user.getId () , applicant.getId ()) != null)
                    return true;
            }
            else if (!isAnonymous && strEnumType.equals (AccessLevel.Who.trustworthy.name ()))
            {
                if (hasInList (UserListType.trustworthy)) return true;
            }
            else if (!isAnonymous && strEnumType.equals (AccessLevel.Who.unreliable.name ()))
            {
                if (hasInList (UserListType.unreliable)) return true;
            }
            else if (isAnonymous && strEnumType.equals (AccessLevel.Who.anonymous.name ())) return true;
        }
        return false;
    }

    private Boolean hasSeparately (List <UserSeparateProfiles> separateProfiles)
    {
        if (separateProfiles != null && separateProfiles.size () > 0)
        {
            List <EnumTypes> byId2AndDeletedFalse;
            for (UserSeparateProfiles separateProfile : separateProfiles)
            {
                byId2AndDeletedFalse = _Service.enumTypesService.Repository.findById2AndDeletedFalse (separateProfile.getId ());
                if (byId2AndDeletedFalse != null && checkEnumTypesCheck (byId2AndDeletedFalse))
                {
                    this.separateProfile = separateProfile;
                    return true;
                }
            }
        }
        return null;
    }

    protected boolean hasInList (UserListType type)
    {
        UserList userList = _Service.userListService.Repository
                .findByMainAccountIdAndUserIdAndTypeAndDeletedFalse (user.getId () , applicant.getId () , type);
        return userList != null;
    }

    protected boolean isAnonymous ()
    {
        return (!hasInList (UserListType.family) &&
                !hasInList (UserListType.trustworthy) &&
                !hasInList (UserListType.unreliable) &&
                (_Service.userContactsService.findContact (user.getId () , applicant.getId ()) == null) &&
                ((_Service.userFriendsService.findFriend (user , applicant , StatusFriends.friend)) == null) &&
                ((_Service.userFriendsService.findFriend (user , applicant , StatusFriends.awaiting_approval)) == null) &&
                ((_Service.userFriendsService.findFriend (user , applicant , StatusFriends.deleted)) == null) &&
                ((_Service.userFriendsService.findFriend (user , applicant , StatusFriends.rejected)) == null)
        );
    }

    private AccessLevel getAccessLevel (SecurityUserProfile securityUser)
    {
        switch (which)
        {
            case id:
                desEnumTypes = DesEnumTypes.show_id.name ();
                userBlockedType = UserBlocked.Type.all.name ();
                return securityUser.getShowId ();
            case find_me:
                desEnumTypes = DesEnumTypes.find_me.name ();
                userBlockedType = UserBlocked.Type.cns_find_me.name ();
                return securityUser.getFindMe ();
            case find_me_by_phone:
                desEnumTypes = DesEnumTypes.find_me_by_phone.name ();
                userBlockedType = UserBlocked.Type.cns_find_be_by_phone.name ();
                return securityUser.getFindMeByPhone ();
            case bio:
                desEnumTypes = DesEnumTypes.show_bio.name ();
                userBlockedType = UserBlocked.Type.cns_bio.name ();
                return securityUser.getShowBio ();
            case name:
                desEnumTypes = DesEnumTypes.show_name.name ();
                userBlockedType = UserBlocked.Type.cns_name.name ();
                return securityUser.getShowName ();
            case cover:
                desEnumTypes = DesEnumTypes.show_cover.name ();
                userBlockedType = UserBlocked.Type.cns_cover.name ();
                return securityUser.getShowCover ();
            case email:
                desEnumTypes = DesEnumTypes.show_email.name ();
                userBlockedType = UserBlocked.Type.cns_email.name ();
                return securityUser.getShowEmail ();
            case phone:
                desEnumTypes = DesEnumTypes.show_phone.name ();
                userBlockedType = UserBlocked.Type.cns_phone.name ();
                return securityUser.getShowPhone ();
            case family:
                desEnumTypes = DesEnumTypes.show_family.name ();
                userBlockedType = UserBlocked.Type.cns_family.name ();
                return securityUser.getShowFamily ();
            case mylink:
                desEnumTypes = DesEnumTypes.show_mylink.name ();
                userBlockedType = UserBlocked.Type.cns_mylink.name ();
                return securityUser.getShowMyLink ();
            case profile:
                desEnumTypes = DesEnumTypes.show_profile.name ();
                userBlockedType = UserBlocked.Type.cns_profile.name ();
                return securityUser.getShowInProfile ();
            case in_group:
                desEnumTypes = DesEnumTypes.show_in_group.name ();
                userBlockedType = UserBlocked.Type.cns_profile.name ();
                return securityUser.getShowInGroup ();
            case username:
                desEnumTypes = DesEnumTypes.show_username.name ();
                userBlockedType = UserBlocked.Type.cns_username.name ();
                return securityUser.getShowUsername ();
            case in_search:
                desEnumTypes = DesEnumTypes.show_in_search.name ();
                userBlockedType = UserBlocked.Type.all.name ();
                return securityUser.getShowInSearch ();
            case last_seen:
                desEnumTypes = DesEnumTypes.show_last_seen.name ();
                userBlockedType = UserBlocked.Type.all.name ();
                return securityUser.getShowLastSeen ();
            case in_channel:
                desEnumTypes = DesEnumTypes.show_in_channel.name ();
                userBlockedType = UserBlocked.Type.all.name ();
                return securityUser.getShowInChannel ();
            case list_friends:
                desEnumTypes = DesEnumTypes.list_friends.name ();
                userBlockedType = UserBlocked.Type.all.name ();
                return securityUser.getListFriends ();
            case seen_message:
                desEnumTypes = DesEnumTypes.show_seen_message.name ();
                userBlockedType = UserBlocked.Type.cns_seed_message.name ();
                return securityUser.getShowSeenMessage ();
            case personal_information:
                desEnumTypes = DesEnumTypes.show_personal_information.name ();
                userBlockedType = UserBlocked.Type.all.name ();
                return securityUser.getShowPersonalInformation ();
            case gender:
                desEnumTypes = DesEnumTypes.gender.name ();
                userBlockedType = UserBlocked.Type.cns_gender.name ();
                return securityUser.getShowGender ();
            default:
                desEnumTypes = null;
                userBlockedType = null;
                return null;
        }
    }

    public static class Service
    {
        public final MainAccountService mainAccountService;
        public final EnumTypesService enumTypesService;
        public final UserListService userListService;
        public final UserFriendsService userFriendsService;
        public final UserContactsService userContactsService;
        public final UserSeparateProfilesService userSeparateProfilesService;
        public final UserBlockedService userBlockedService;
        public final ProfilePicturesService profilePicturesService;

        public Service ()
        {
            this.mainAccountService = (MainAccountService) ThisApp.Services ().Get (MainAccountService.class);
            this.enumTypesService = (EnumTypesService) ThisApp.Services ().Get (EnumTypesService.class);
            this.userListService = (UserListService) ThisApp.Services ().Get (UserListService.class);
            this.userFriendsService = (UserFriendsService) ThisApp.Services ().Get (UserFriendsService.class);
            this.userContactsService = (UserContactsService) ThisApp.Services ().Get (UserContactsService.class);
            this.userSeparateProfilesService = (UserSeparateProfilesService) ThisApp.Services ().Get (UserSeparateProfilesService.class);
            this.userBlockedService = (UserBlockedService) ThisApp.Services ().Get (UserBlockedService.class);
            this.profilePicturesService = (ProfilePicturesService) ThisApp.Services ().Get (ProfilePicturesService.class);
        }
    }

    public UserSeparateProfiles getSeparateProfile ()
    {
        return separateProfile;
    }

    public boolean isSeparateProfile ()
    {
        return (separateProfile != null);
    }

    public List <ProfilePictures> getProfilePictures ()
    {
        return profilePictures;
    }

    public boolean isSeparateProfilePictures ()
    {
        return (profilePictures != null);
    }
}
