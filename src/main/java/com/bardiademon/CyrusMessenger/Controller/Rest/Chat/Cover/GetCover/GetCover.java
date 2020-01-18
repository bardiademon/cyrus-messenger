package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Cover.GetCover;

import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.VCodeLogin;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@RestController
@RequestMapping (value = "/api/cover", method = RequestMethod.GET)
public class GetCover
{
    private MainAccountService mainAccountService;
    private UserLoginService userLoginService;
    private SecurityUserProfileService securityUserProfileService;
    private ShowProfileForService showProfileForService;
    private UserFriendsService userFriendsService;

    @Autowired
    public GetCover
            (MainAccountService _MainAccountService ,
             UserLoginService _UserLoginService ,
             SecurityUserProfileService _SecurityUserProfileService ,
             ShowProfileForService _ShowProfileForService ,
             UserFriendsService _UserFriendsService
            )
    {
        this.mainAccountService = _MainAccountService;
        this.userLoginService = _UserLoginService;
        this.securityUserProfileService = _SecurityUserProfileService;
        this.showProfileForService = _ShowProfileForService;
        this.userFriendsService = _UserFriendsService;
    }

    @RequestMapping (value = {"/{username}" , "/" , ""}, produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.GET)
    public @ResponseBody
    byte[] get (
            @PathVariable (value = "username",
                    required = false) String username ,
            HttpServletResponse response ,
            @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin
    )
    {
        response.setContentType ("image/jpeg");

        if (username == null || username.equals ("")) return error (Path.IMAGE_NOT_FOUND);

        VCodeLogin vCodeLogin;
        if (codeLogin.equals (""))
            return error (Path.IC_NOT_LOGIN);
        else
        {
            vCodeLogin = new VCodeLogin ();
            if (!vCodeLogin.IsValid (userLoginService.Repository , codeLogin))
                return error (Path.IC_NOT_LOGIN);
        }

        MainAccount mainAccount = findUsername (username);
        if (mainAccount != null)
        {

            CheckUserAccessLevel accessLevel =
                    new CheckUserAccessLevel (vCodeLogin.getMainAccount () , mainAccount , mainAccountService);

            accessLevel.setServiceSecurityUserProfile (securityUserProfileService);
            accessLevel.setServiceShowProfileFor (showProfileForService);
            accessLevel.setUserFriendsService (userFriendsService);
            accessLevel.setCheckProfile (CheckUserAccessLevel.CheckProfile.cover);
            boolean isAccessLevel = accessLevel.check (accessLevel.CHK_PROFILE);

            if (isAccessLevel)
            {
                String cover = mainAccount.getCover ();
                File fileCover = Path.StickTogetherFile (Path.COVER_USER , cover);
                if (fileCover.exists ()) return toByte (fileCover);
                else return error (Path.IC_NO_COVER);
            }
            else return error (Path.IC_NO_COVER);
        }
        else return error (Path.IC_NO_COVER);

    }


    private MainAccount findUsername (String username)
    {
        return mainAccountService.Repository.findByUsername (username);
    }

    private byte[] error (String image)
    {
        return toByte (new File (image));
    }

    private byte[] toByte (File file)
    {
        try
        {
            return IOUtils.toByteArray (new FileInputStream (file));
        }
        catch (IOException ignored)
        {
        }
        return null;
    }

}
