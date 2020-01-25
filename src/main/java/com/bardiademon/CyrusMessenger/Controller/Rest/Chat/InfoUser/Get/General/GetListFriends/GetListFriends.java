package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.General.GetListFriends;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping (value = RouterName.RNChat.RNInfoUser.RNGetListFriends.RN_GET_LIST_FRIENDS, method = RequestMethod.POST)
public class GetListFriends
{

    private boolean isLogin = false;

    private CheckLogin checkLogin;

    private UserFriendsService userFriendsService;
    private UserLoginService userLoginService;

    @Autowired
    public GetListFriends ( UserFriendsService _UserFriendsService , UserLoginService _UserLoginService)
    {
        this.userFriendsService = _UserFriendsService;
        this.userLoginService = _UserLoginService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient getList (HttpServletResponse res ,
                                   @RequestParam ("status") String status , @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        if (!isLogin)
            checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);

        if (isLogin || checkLogin.isValid ())
        {
            if (status == null || status.isEmpty ()) answerToClient = AnswerToClient.error400 ();
            else
            {
                try
                {
                    answerToClient = AnswerToClient.OK ();
                    List<UserFriends> userFriendsList;
                    if (status.equals (KeyRequest.all.name ()))
                    {
                        isLogin = true;

                        answerToClient = AnswerToClient.OK ();
                        answerToClient.put (StatusFriends.deleted.name () ,
                                (getList (res , StatusFriends.deleted.name () , codeLogin)).getMessage ());

                        answerToClient.put (StatusFriends.rejected.name () ,
                                (getList (res , StatusFriends.rejected.name () , codeLogin)).getMessage ());

                        answerToClient.put (StatusFriends.awaiting_approval.name () ,
                                (getList (res , StatusFriends.awaiting_approval.name () , codeLogin)).getMessage ());

                        answerToClient.put (StatusFriends.friend.name () ,
                                (getList (res , StatusFriends.friend.name () , codeLogin)).getMessage ());
                    }
                    else
                    {
                        StatusFriends statusFriends = StatusFriends.valueOf (status);
                        userFriendsList =
                                userFriendsService.Repository.findAllByMainAccountAndStatus
                                        (checkLogin.getVCodeLogin ().getMainAccount () , statusFriends);


                        if (userFriendsList.size () > 0)
                        {
                            Map<String, String> friend;
                            UserFriends userFriends;
                            for (int i = 0; i < userFriendsList.size (); i++)
                            {
                                userFriends = userFriendsList.get (i);
                                friend = new LinkedHashMap<> ();
                                friend.put (KeyAnswer.name.name () , userFriends.getMainAccountFriend ().getUsername ());
                                friend.put (KeyAnswer.status.name () , userFriends.getStatus ().name ());
                                friend.put (KeyAnswer.created_at.name () , Time.toString (userFriends.getCreatedAt ()));
                                friend.put (KeyAnswer.updated_at.name () , Time.toString (userFriends.getUpdatedAt ()));

                                answerToClient.put (String.valueOf (i) , friend);
                            }
                        }
                    }
                }
                catch (IllegalArgumentException e)
                {
                    answerToClient = AnswerToClient.error400 ();
                }
            }

        }
        else answerToClient = checkLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    private enum KeyRequest
    {
        all
    }

    private enum KeyAnswer
    {
        name, status, created_at, updated_at,
    }

}
