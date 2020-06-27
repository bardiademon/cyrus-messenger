package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.SeparateProfile.ChangeSP;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// SP => Separate Profile

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_CHANGE, method = RequestMethod.POST)
public final class RestChangeSP
{
    private final String router;
    private final SubmitRequestType type;
    private final UserLoginService userLoginService;
    private final UserSeparateProfilesService userSeparateProfilesService;

    @Autowired
    public RestChangeSP
            (UserLoginService _UserLoginService ,
             UserSeparateProfilesService _UserSeparateProfilesService)
    {
        this.userLoginService = _UserLoginService;
        this.userSeparateProfilesService = _UserSeparateProfilesService;
        this.router = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_CHANGE;
        this.type = SubmitRequestType.change_user_separate_profile;
    }

    public AnswerToClient change
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        return null;
    }
}
