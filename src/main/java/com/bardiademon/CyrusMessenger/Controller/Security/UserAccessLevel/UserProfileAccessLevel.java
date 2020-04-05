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

import java.util.List;
import java.util.Objects;

public final class UserProfileAccessLevel
{
    private final Service service;
    private final MainAccount applicant;
    private final MainAccount user;
    private Which which;

    private String desEnumTypes;
    private String userBlockedType;

    private UserSeparateProfiles separateProfile;
    private List <ProfilePictures> profilePictures;

    private boolean isAnonymous;

    public UserProfileAccessLevel (Service _Service , MainAccount Applicant , MainAccount User)
    {
        this.service = _Service;
        this.applicant = Applicant;
        this.user = User;
    }

    public boolean hasAccess (Which _Which)
    {
        this.which = _Which;

        if (this.applicant.getId () == this.user.getId ())
            return true;

        final SecurityUserProfile securityUser = service.mainAccountService.repositorySecurityProfile.findByMainAccount (user);
        final AccessLevel accessLevel = getAccessLevel (securityUser);

        return (securityUser != null && accessLevel != null) && (!isBlock () && hasAccess (accessLevel , securityUser));
    }

    private boolean isBlock ()
    {
        return (service.userBlockedService.isBlocked (user.getId () , applicant.getId () , UserBlocked.Type.valueOf (userBlockedType))) != null;
    }

    private boolean hasAccess (AccessLevel accessLevel , SecurityUserProfile securityUser)
    {
        assert desEnumTypes != null;

        if (accessLevel.equals (AccessLevel.all)) return true;
        else
        {
            isAnonymous = isAnonymous ();

            List <EnumTypes> listEnum
                    = service.enumTypesService.Repository.findById2AndDeletedFalseAndDes (securityUser.getId () , desEnumTypes);
            if (accessLevel.equals (AccessLevel.all_except))
            {
                if (listEnum != null && listEnum.size () > 0) return !(checkEnumTypesCheck (listEnum));
                else return true;
            }
            else if (accessLevel.equals (AccessLevel.just))
            {
                if (listEnum != null && listEnum.size () > 0) return checkEnumTypesCheck (listEnum);
                else return false;
            }
            else
            {

                List <UserSeparateProfiles> separateProfiles
                        = service.userSeparateProfilesService.Repository.findByMainAccountIdAndDeletedFalse (user.getId ());

                Boolean hasSeparately = hasSeparately (separateProfiles);

                if (accessLevel.equals (AccessLevel.separately))
                {
                    if (which.equals (Which.cover))
                    {
                        List <ProfilePictures> separate = service.profilePicturesService.getSeparate (user.getId ());
                        if (separate != null && separate.size () > 0)
                        {
                            for (ProfilePictures pictures : separate)
                            {
                                if (checkEnumTypesCheck (pictures.getSeparateFor ()))
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
                        List <ProfilePictures> separate = service.profilePicturesService.getSeparate (user.getId ());
                        if (separate != null && separate.size () > 0)
                        {
                            for (ProfilePictures pictures : separate)
                            {
                                if (checkEnumTypesCheck (pictures.getSeparateFor ()))
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

    private boolean checkEnumTypesCheck (List <EnumTypes> enumTypes)
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
                if (service.userFriendsService.findFriend (user , applicant , StatusFriends.friend) != null)
                    return true;
            }
            else if (!isAnonymous && strEnumType.equals (AccessLevel.Who.contact.name ()))
            {
                if (service.userContactsService.findContact (user.getId () , applicant.getId ()) != null)
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
            for (UserSeparateProfiles separateProfile : separateProfiles)
            {
                if (checkEnumTypesCheck (separateProfile.getProfileFor ()))
                {
                    this.separateProfile = separateProfile;
                    return true;
                }
            }
        }
        return null;
    }

    private boolean hasInList (UserListType type)
    {
        UserList userList = service.userListService.Repository
                .findByMainAccountIdAndUserIdAndTypeAndDeletedFalse (user.getId () , applicant.getId () , type);
        return userList != null;
    }

    private boolean isAnonymous ()
    {
        return (!hasInList (UserListType.family) &&
                !hasInList (UserListType.trustworthy) &&
                !hasInList (UserListType.unreliable) &&
                (service.userContactsService.findContact (user.getId () , applicant.getId ()) == null) &&
                ((service.userFriendsService.findFriend (user , applicant , StatusFriends.friend)) == null) &&
                ((service.userFriendsService.findFriend (user , applicant , StatusFriends.awaiting_approval)) == null) &&
                ((service.userFriendsService.findFriend (user , applicant , StatusFriends.deleted)) == null) &&
                ((service.userFriendsService.findFriend (user , applicant , StatusFriends.rejected)) == null)
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
                userBlockedType = UserBlocked.Type.cns_send_message.name ();
                return securityUser.getShowSeenMessage ();
            case personal_information:
                desEnumTypes = DesEnumTypes.show_personal_information.name ();
                userBlockedType = UserBlocked.Type.all.name ();
                return securityUser.getShowPersonalInformation ();
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

        public Service
                (MainAccountService _MainAccountService ,
                 EnumTypesService _EnumTypesService ,
                 UserListService _UserListService ,
                 UserFriendsService _UserFriendsService ,
                 UserContactsService _UserContactsService ,
                 UserSeparateProfilesService _UserSeparateProfilesService ,
                 UserBlockedService _UserBlockedService ,
                 ProfilePicturesService _ProfilePicturesService)
        {
            this.mainAccountService = _MainAccountService;
            this.enumTypesService = _EnumTypesService;
            this.userListService = _UserListService;
            this.userFriendsService = _UserFriendsService;
            this.userContactsService = _UserContactsService;
            this.userSeparateProfilesService = _UserSeparateProfilesService;
            this.userBlockedService = _UserBlockedService;
            this.profilePicturesService = _ProfilePicturesService;
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
