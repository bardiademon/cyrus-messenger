package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Get.General;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.VCodeLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping (value = RouterName.RNChat.RNInfoUser.RN_GENERAL, method = RequestMethod.POST)
public class GetGeneral
{

    private UserLoginService userLoginService;
    private UserFriendsService userFriendsService;

    @Autowired
    public GetGeneral (UserLoginService _UserLoginService , UserFriendsService _UserFriendsService)
    {
        this.userLoginService = _UserLoginService;
        this.userFriendsService = _UserFriendsService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient getInfoUser (HttpServletResponse res , @RequestBody RequestGeneral requestInfoUser ,
                                       @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        if (codeLogin.equals ("")) answerToClient = AnswerToClient.NotLoggedIn ();
        else
        {
            VCodeLogin vCodeLogin = new VCodeLogin ();
            if (!vCodeLogin.IsValid (userLoginService.Repository , codeLogin))
                answerToClient = AnswerToClient.NotLoggedIn ();
            else
            {
                if (requestInfoUser.atLeastOne ())
                {
                    MainAccount mainAccount = vCodeLogin.getMainAccount ();

                    answerToClient = new AnswerToClient (200 , true);

                    if (requestInfoUser.isGetName ())
                        answerToClient.put (KeyAnswer.name.name () , mainAccount.getName ());

                    if (requestInfoUser.isGetFamily ())
                        answerToClient.put (KeyAnswer.family.name () , mainAccount.getFamily ());

                    if (requestInfoUser.isGetUsername ())
                        answerToClient.put (KeyAnswer.username.name () , mainAccount.getUsername ());

                    if (requestInfoUser.isGetEmail ())
                        answerToClient.put (KeyAnswer.email.name () , mainAccount.getEmail ());

                    if (requestInfoUser.isGetPhone ())
                        answerToClient.put (KeyAnswer.phone.name () , mainAccount.getPhone ());

                    if (requestInfoUser.isGetMyLink ())
                        answerToClient.put (KeyAnswer.mylink.name () , mainAccount.getMyLink ());

                    if (requestInfoUser.isGetBio ())
                        answerToClient.put (KeyAnswer.bio.name () , mainAccount.getBio ());

                    if (requestInfoUser.isGetListFriends ())
                        answerToClient.put (KeyAnswer.list_friends.name () ,
                                getListFriend (mainAccount.getId () , StatusFriends.friend));

                    if (requestInfoUser.isGetListFriendsReject ())
                        answerToClient.put (KeyAnswer.list_friends_reject.name () ,
                                getListFriend (mainAccount.getId () , StatusFriends.rejected));

                    if (requestInfoUser.isGetListFriendsReject ())
                        answerToClient.put (KeyAnswer.list_friends_reject.name () ,
                                getListFriend (mainAccount.getId () , StatusFriends.rejected));

                    if (requestInfoUser.isGetListFriendsAwaitingApproval ())
                        answerToClient.put (KeyAnswer.list_friends_awaiting_approval.name () ,
                                getListFriend (mainAccount.getId () , StatusFriends.awaiting_approval));

                    if (requestInfoUser.isGetListFriendsDeleted ())
                        answerToClient.put (KeyAnswer.list_friends_deleted.name () ,
                                getListFriend (mainAccount.getId () , StatusFriends.deleted));

                }
                else answerToClient = AnswerToClient.error400 ();
            }
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private List<String> getListFriend (long id , StatusFriends status)
    {
        return userFriendsService.Repository.findUsernameUser (id , status);
    }

    private enum KeyAnswer
    {
        name, family, username, email, phone,
        bio, mylink, list_friends, list_friends_reject, list_friends_awaiting_approval, list_friends_deleted
    }
}
