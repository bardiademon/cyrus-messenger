package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Friends.ListFriends;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import static com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel._Service;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS_LIST, method = RequestMethod.POST)
public final class ListFriends
{

    private final UserLoginService userLoginService;

    @Autowired
    public ListFriends (UserLoginService _UserLoginService)
    {
        this.userLoginService = _UserLoginService;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient getList
            (HttpServletResponse res ,
             @RequestParam ("status") String status ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);

        if (isLogin.isValid ())
        {
            if (status == null || status.isEmpty ()) answerToClient = AnswerToClient.error400 ();
            else
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();

                answerToClient = AnswerToClient.OK ();

                if (status.equals (Status.requests.name ()))
                {
                    List <String> requests = _Service.userFriendsService.requests (mainAccount.getId ());
                    if (requests != null && requests.size () > 0)
                        AnswerToClient.OneAnswer (answerToClient , KeyAnswer.usernames.name () , requests);
                    else
                        AnswerToClient.OneAnswer (answerToClient , AnswerToClient.CUV.not_found.name ());
                }
                else
                {
                    try
                    {
                        List <UserFriends> userFriendsList;
                        StatusFriends statusFriends = StatusFriends.valueOf (status);
                        userFriendsList = _Service.userFriendsService.Repository.findAllByMainAccountAndStatus
                                (mainAccount , statusFriends);

                        if (userFriendsList.size () > 0)
                        {
                            Map <String, String> friend;
                            UserFriends userFriends;

                            UserProfileAccessLevel checkUserAccessLevel;

                            for (int i = 0; i < userFriendsList.size (); i++)
                            {
                                userFriends = userFriendsList.get (i);
                                friend = new LinkedHashMap <> ();

                                checkUserAccessLevel = new UserProfileAccessLevel (mainAccount , userFriends.getMainAccountFriend ());
                                if (checkUserAccessLevel.hasAccess (Which.username))
                                    friend.put (KeyAnswer.name.name () , userFriends.getMainAccountFriend ().getUsername ().getUsername ());

                                friend.put (KeyAnswer.status.name () , userFriends.getStatus ().name ());
                                friend.put (KeyAnswer.created_at.name () , Time.toString (userFriends.getCreatedAt ()));
                                friend.put (KeyAnswer.updated_at.name () , Time.toString (userFriends.getUpdatedAt ()));

                                answerToClient.put (String.valueOf (i) , friend);
                            }
                        }

                    }
                    catch (IllegalArgumentException e)
                    {
                        answerToClient = AnswerToClient.error400 ();
                    }
                }
            }

        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    private enum Status
    {
        requests
    }

    private enum KeyAnswer
    {
        name, status, created_at, updated_at, usernames
    }

}
