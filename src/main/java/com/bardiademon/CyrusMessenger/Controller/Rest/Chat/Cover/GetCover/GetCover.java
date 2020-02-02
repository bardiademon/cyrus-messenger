package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Cover.GetCover;

import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedByTheSystemService;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.VCodeLogin;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@RestController
@RequestMapping (value = RouterName.RNChat.RNCover.RN_GET_USER_COVER, method = RequestMethod.GET)
public class GetCover
{
    private MainAccountService mainAccountService;
    private UserLoginService userLoginService;
    private SubmitRequestService submitRequestService;
    private BlockedByTheSystemService blockedByTheSystemService;

    private CheckUserAccessLevel.ServiceProfile serviceProfile;

    private HttpServletRequest request;

    @Autowired
    public GetCover
            (MainAccountService _MainAccountService ,
             UserLoginService _UserLoginService ,
             SecurityUserProfileService _SecurityUserProfileService ,
             ShowProfileForService _ShowProfileForService ,
             UserContactsService _UserContactsService ,
             UserFriendsService _UserFriendsService ,
             SubmitRequestService _SubmitRequestService ,
             BlockedByTheSystemService _BlockedByTheSystemService ,
             UserBlockedService _UserBlockedService
            )
    {
        this.mainAccountService = _MainAccountService;
        this.userLoginService = _UserLoginService;
        this.submitRequestService = _SubmitRequestService;
        this.blockedByTheSystemService = _BlockedByTheSystemService;
        serviceProfile = new CheckUserAccessLevel.ServiceProfile (_ShowProfileForService , _UserContactsService , _UserFriendsService , _SecurityUserProfileService , _UserBlockedService);
    }

    @RequestMapping (value = {"/{username}" , "/" , ""}, produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.GET)
    public @ResponseBody
    byte[] get (
            @PathVariable (value = "username",
                    required = false) String username ,
            HttpServletResponse response ,
            HttpServletRequest request ,
            @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin
    )
    {
        response.setContentType ("image/jpeg");

        if ((new CheckBlockSystem (request , blockedByTheSystemService , BlockedFor.submit_request , SubmitRequestType.get_cover.name ())).isBlocked ())
            return error (Path.IMAGE_NOT_FOUND);

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

            accessLevel.setServiceProfile (serviceProfile);
            accessLevel.setCheckProfile (CheckUserAccessLevel.CheckProfile.cover);
            boolean isAccessLevel = accessLevel.check (accessLevel.CHK_PROFILE);

            if (isAccessLevel)
            {
                String cover = mainAccount.getCover ();
                File fileCover = Path.StickTogetherFile (Path.COVER_USER , cover);
                if (fileCover.exists ())
                {
                    submitRequestService.newRequest (request.getRemoteAddr () , SubmitRequestType.get_cover , false);
                    return toByte (fileCover);
                }
                else return error (Path.IC_NO_COVER);
            }
            else return error (Path.IC_NO_COVER);
        }
        else return error (Path.IC_NO_COVER);

    }

    private MainAccount findUsername (String username)
    {
        return mainAccountService.findUsername (username);
    }

    private byte[] error (String image)
    {
        submitRequestService.newRequest (request.getRemoteAddr () , SubmitRequestType.get_cover , true);
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
