package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.GetOne;

import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE, method = RequestMethod.POST)
public final class RestGetOneProfilePicture
{

    private UserLoginService userLoginService;
    private MainAccountService mainAccountService;
    private ProfilePicturesService profilePicturesService;

    private IOException exceptionToByte;

    @Autowired
    public RestGetOneProfilePicture (UserLoginService _UserLoginService , MainAccountService _MainAccountService , ProfilePicturesService _ProfilePicturesService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.profilePicturesService = _ProfilePicturesService;
    }

    @RequestMapping (value = {"/{ID_PROFILE_PICTURE}" , "/" , ""}, produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.POST)
    public @ResponseBody
    byte[] getOne
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse response ,
             @PathVariable (value = "ID_PROFILE_PICTURE", required = false) long idProfilePicture)
    {
        response.setContentType ("image/jpeg");

        ToJson.CreateClass createClass = new ToJson.CreateClass ();
        createClass.put ("ID_PROFILE_PICTURE" , idProfilePicture);

        byte[] answer = toByte (Path.IC_NO_COVER);

        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            MainAccount mainAccountRequest = checkLogin.getVCodeLogin ().getMainAccount ();

            if (idProfilePicture > 0)
            {
                ProfilePictures profilePictures = profilePicturesService.Repository.findByIdAndDeletedFalse (idProfilePicture);
                if (profilePictures != null)
                {
                    MainAccount mainAccount = profilePictures.getMainAccount ();

                    createClass.put ("id_user_get_cover" , mainAccount.getId ());

                    CheckUserAccessLevel checkUserAccessLevel = new CheckUserAccessLevel (checkLogin.getVCodeLogin ().getMainAccount () , mainAccount , mainAccountService);
                    if (checkUserAccessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.cover))
                    {
                        String pathFile = Path.StickTogether (Path.PROFILE_PICTURES_USERS , mainAccount.getUsername () , String.format ("%s.%s" , profilePictures.getName () , profilePictures.getType ()));

                        answer = toByte (pathFile);

                        if (answer == null)
                        {
                            answer = toByte (Path.IMAGE_ERROR_500);
                            l.n (createClass.toJson () , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE , mainAccountRequest , null , Thread.currentThread ().getStackTrace () , exceptionToByte , createClass.toJson ());
                        }
                        else
                            l.n (createClass.toJson () , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE , mainAccountRequest , null , Thread.currentThread ().getStackTrace () , null , createClass.toJson ());
                    }
                    else
                        l.n (createClass.toJson () , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE , mainAccountRequest , null , Thread.currentThread ().getStackTrace () , new Exception ("Error from CheckUserAccessLevel") , createClass.toJson ());
                }
                else
                {
                    l.n (createClass.toJson () , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE , mainAccountRequest , null , Thread.currentThread ().getStackTrace () , new Exception ("image_not_found") , createClass.toJson ());
                    answer = toByte (Path.IMAGE_NOT_FOUND);
                }
            }
            else
            {
                l.n (createClass.toJson () , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE , mainAccountRequest , null , Thread.currentThread ().getStackTrace () , new Exception ("id_invalid") , createClass.toJson ());
                answer = toByte (Path.IMAGE_NOT_FOUND);
            }
        }
        else
        {
            l.n (createClass.toJson () , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE , null , null , Thread.currentThread ().getStackTrace () , new Exception ("not login") , Path.IC_NOT_LOGGED);
            answer = toByte (Path.IC_NOT_LOGGED);
        }
        return answer;
    }

    private byte[] toByte (String path)
    {
        try
        {
            return IOUtils.toByteArray (new FileInputStream (new File (path)));
        }
        catch (IOException e)
        {
            exceptionToByte = e;
        }
        return null;
    }

}
