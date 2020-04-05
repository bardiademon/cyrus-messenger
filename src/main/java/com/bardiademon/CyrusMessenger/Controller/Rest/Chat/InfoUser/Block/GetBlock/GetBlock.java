package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Block.GetBlock;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList.UserListService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNBlock.RN_GET_BLOCK, method = RequestMethod.POST)
public final class GetBlock
{
    private final UserLoginService userLoginService;

    private final UserProfileAccessLevel.Service serviceProfile;

    @Autowired
    public GetBlock
            (MainAccountService _MainAccountService ,
             EnumTypesService _EnumTypesService ,
             UserLoginService _UserLoginService ,
             UserListService _UserListService ,
             UserFriendsService _UserFriendsService ,
             UserContactsService _UserContactsService ,
             UserSeparateProfilesService _UserSeparateProfilesService ,
             UserBlockedService _UserBlockedService ,
             ProfilePicturesService _ProfilePicturesService)
    {
        this.userLoginService = _UserLoginService;
        this.serviceProfile = new UserProfileAccessLevel.Service (_MainAccountService ,
                _EnumTypesService ,
                _UserListService ,
                _UserFriendsService ,
                _UserContactsService ,
                _UserSeparateProfilesService ,
                _UserBlockedService ,
                _ProfilePicturesService);
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient getBlock
            (HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();

            List <UserBlocked> userBlocked = serviceProfile.userBlockedService.listBlocked (mainAccount.getId ());
            if (userBlocked == null || userBlocked.size () == 0)
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
            else
            {
                UserBlocked blocked;
                UserProfileAccessLevel accessLevel;
                for (int i = 0, len = userBlocked.size (); i < len; i++)
                {
                    blocked = userBlocked.get (i);
                    accessLevel = new UserProfileAccessLevel (serviceProfile , mainAccount , blocked.getMainAccountBlocked ());

                    if (accessLevel.hasAccess (Which.id))
                        blocked.setIdBlocked (blocked.getMainAccountBlocked ().getId ());

                    blocked.setValidityTimeToJson (Time.toString (blocked.getValidityTime ()));
                    userBlocked.remove (i);
                    userBlocked.add (i , blocked);
                }
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , userBlocked);
            }
        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }
}
