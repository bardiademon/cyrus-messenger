package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Users.GetOne;

import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.PathUploadProfilePictures;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.IO.ToByte;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE_USER, method = { RequestMethod.POST , RequestMethod.GET })
public final class RestGetOneProfilePictureUser
{

    private final UserLoginService userLoginService;

    @Autowired
    public RestGetOneProfilePictureUser (UserLoginService _UserLoginService)
    {
        this.userLoginService = _UserLoginService;
    }

    @RequestMapping (value = { "/" , "" , "/{ID_PROFILE_PICTURE}" }, produces = MediaType.IMAGE_JPEG_VALUE)
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
                ProfilePictures profilePictures = UserProfileAccessLevel._Service.profilePicturesService.getOneForUser (idProfilePicture.getId ());
                if (profilePictures != null)
                {
                    assert both.getIsLogin () != null;
                    MainAccount mainAccountRequest = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
                    MainAccount mainAccountUser = profilePictures.getMainAccount ();
                    UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (mainAccountRequest , mainAccountUser);
                    if (accessLevel.hasAccess (Which.cover))
                    {
                        if (accessLevel.isSeparateProfilePictures ())
                        {
                            List <ProfilePictures> separateProfilePictures = accessLevel.getProfilePictures ();
                            for (ProfilePictures profilePicture : separateProfilePictures)
                            {
                                if (profilePicture.getId () == profilePictures.getId ())
                                    return toByte (PathUploadProfilePictures.User (mainAccountUser.getId () , profilePictures.getName () , profilePictures.getType ()));
                            }
                        }
                        else
                            return toByte (PathUploadProfilePictures.User (mainAccountUser.getId () , profilePictures.getName () , profilePictures.getType ()));
                    }
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
