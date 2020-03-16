package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Users.GetOne;

import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.PathUploadProfilePictures;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.IO.ToByte;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE_USER, method = RequestMethod.POST)
public final class RestGetOneProfilePictureUser
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;
    private final ProfilePicturesService profilePicturesService;
    private final CheckUserAccessLevel.ServiceProfile serviceProfile;

    @Autowired
    public RestGetOneProfilePictureUser
            (
                    UserLoginService _UserLoginService ,
                    MainAccountService _MainAccountService ,
                    ShowProfileForService _ShowProfileForService ,
                    UserContactsService _UserContactsService ,
                    UserFriendsService _UserFriendsService ,
                    SecurityUserProfileService _SecurityUserProfileService ,
                    UserBlockedService _UserBlockedService ,
                    ProfilePicturesService _ProfilePicturesService
            )
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.profilePicturesService = _ProfilePicturesService;
        this.serviceProfile = new CheckUserAccessLevel.ServiceProfile (_ShowProfileForService , _UserContactsService , _UserFriendsService , _SecurityUserProfileService , _UserBlockedService);
    }


    @RequestMapping (value = {"/" , "" , "/{ID_PROFILE_PICTURE}"}, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getOne
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @PathVariable (value = "ID_PROFILE_PICTURE", required = false) String id)
    {
        String request = ToJson.CreateClass.n ("ID_PROFILE_PICTURE" , id).toJson ();

        String router = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE_USER;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , SubmitRequestType.get_one_profile_picture_user);
        if (both.isOk ())
        {
            ID idProfilePicture = new ID (id);
            if (idProfilePicture.isValid ())
            {
                ProfilePictures profilePictures = profilePicturesService.getOneForUser (idProfilePicture.getId ());
                if (profilePictures != null)
                {
                    assert both.getIsLogin () != null;
                    MainAccount mainAccountRequest = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
                    MainAccount mainAccountUser = profilePictures.getMainAccount ();
                    CheckUserAccessLevel accessLevel = new CheckUserAccessLevel (mainAccountRequest , mainAccountUser , mainAccountService);
                    accessLevel.setServiceProfile (serviceProfile);
                    if (accessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.cover))
                        return toByte (PathUploadProfilePictures.User (mainAccountUser.getId () , profilePictures.getName () , profilePictures.getType ()));
                }

            }
        }
        return toByte (Path.IC_NO_COVER);
    }

    private byte[] toByte (String path)
    {
        return ToByte.to (path);
    }
}
