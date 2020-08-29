package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Update.Security.Profile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
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
@RequestMapping (value = Domain.RNGap.RNInfoUser.RNUpdate.RNUpdateSecurity.RN_UPDATE_SECURITY_PROFILE,
        method = RequestMethod.POST)
public final class UpdateSecurityProfile
{

    private final UserLoginService userLoginService;
    private final SecurityUserProfileService securityUserProfileService;

    public UpdateSecurityProfile (UserLoginService _UserLoginService , SecurityUserProfileService _SecurityUserProfileService)
    {
        this.userLoginService = _UserLoginService;
        this.securityUserProfileService = _SecurityUserProfileService;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient update
            (HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestUpdateSecurityProfile request)
    {
        AnswerToClient answerToClient;

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
            answerToClient = afterUpdate (update (isLogin.getVCodeLogin ().getMainAccount () , request));
        else answerToClient = isLogin.getAnswerToClient ();

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

            if ((accessLevel = (checkAccessLevel (request.getSecFindMe ()))) != null)
                securityUserProfile.setFindMe (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecFindMeByPhone ()))) != null)
                securityUserProfile.setFindMeByPhone (accessLevel);

            if ((accessLevel = (checkAccessLevel (request.getSecShowGender ()))) != null)
                securityUserProfile.setShowGender (accessLevel);

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
