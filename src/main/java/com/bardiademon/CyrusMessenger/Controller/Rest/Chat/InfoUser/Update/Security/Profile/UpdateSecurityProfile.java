package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Update.Security.Profile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = RouterName.RNChat.RNInfoUser.RNUpdate.RNUpdateSecurity.RN_UPDATE_SECURITY_PROFILE,
        method = RequestMethod.POST)
public final class UpdateSecurityProfile
{

    private UserLoginService userLoginService;
    private SecurityUserProfileService securityUserProfileService;

    public UpdateSecurityProfile (UserLoginService _UserLoginService , SecurityUserProfileService _SecurityUserProfileService)
    {
        this.userLoginService = _UserLoginService;
        this.securityUserProfileService = _SecurityUserProfileService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient update
            (HttpServletResponse res ,
             @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestUpdateSecurityProfile request)
    {
        AnswerToClient answerToClient;

        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
            answerToClient = afterUpdate (update (checkLogin.getVCodeLogin ().getMainAccount () , request));
        else answerToClient = checkLogin.getAnswerToClient ();

        answerToClient.setResponse (res);

        return answerToClient;
    }

    private SecurityUserProfile update (MainAccount mainAccount , RequestUpdateSecurityProfile request)
    {
        SecurityUserProfile securityUserProfile
                = securityUserProfileService.Repository.findByMainAccount (mainAccount);
        if (securityUserProfile == null) return null;
        else
        {
            AccessLevel accessLevel;
            if ((accessLevel = (checkAccessLevel (request.getSecBio ()))) != null)
                securityUserProfile.setShowBio (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecListFriends ()))) != null)
                securityUserProfile.setListFriends (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecCover ()))) != null)
                securityUserProfile.setShowCover (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowInChannel ()))) != null)
                securityUserProfile.setShowInChannel (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowInGroup ()))) != null)
                securityUserProfile.setShowInGroup (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowProfile ()))) != null)
                securityUserProfile.setShowInProfile (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowInSearch ()))) != null)
                securityUserProfile.setShowInSearch (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowLastSeen ()))) != null)
                securityUserProfile.setShowLastSeen (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowMyLink ()))) != null)
                securityUserProfile.setShowMyLink (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowUsername ()))) != null)
                securityUserProfile.setShowUsername (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowEmail ()))) != null)
                securityUserProfile.setShowEmail (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowFamily ()))) != null)
                securityUserProfile.setShowFamily (accessLevel);

            return securityUserProfile;
        }
    }

    private AnswerToClient afterUpdate (SecurityUserProfile securityUserProfile)
    {
        if (securityUserProfile == null) return AnswerToClient.ServerError ();
        else
        {
            securityUserProfileService.Repository.save (securityUserProfile);
            return AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.updated.name ());
        }
    }

    private AccessLevel checkAccessLevel (String accessLevel)
    {
        try
        {
            return AccessLevel.valueOf (accessLevel);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private enum ValAnswer
    {
        updated
    }
}
