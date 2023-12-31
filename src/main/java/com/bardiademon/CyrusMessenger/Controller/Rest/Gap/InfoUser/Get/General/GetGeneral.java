package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.Get.General;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping (value = Domain.RNGap.RNInfoUser.RN_GENERAL, method = RequestMethod.POST)
public class GetGeneral
{

    private final UserLoginService userLoginService;
    private final UserFriendsService userFriendsService;

    @Autowired
    public GetGeneral (UserLoginService _UserLoginService , UserFriendsService _UserFriendsService)
    {
        this.userLoginService = _UserLoginService;
        this.userFriendsService = _UserFriendsService;
    }

    @RequestMapping ({ "/" , "" })
    public AnswerToClient getInfoUser
            (HttpServletResponse res , @RequestBody RequestGeneral requestInfoUser ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            if (requestInfoUser.atLeastOne ())
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();

                answerToClient = AnswerToClient.OK ();

                if (requestInfoUser.isGetName ())
                    answerToClient.put (KeyAnswer.name , mainAccount.getName ());

                if (requestInfoUser.isGetFamily ())
                    answerToClient.put (KeyAnswer.family , mainAccount.getFamily ());

                if (requestInfoUser.isGetUsername () && mainAccount.getUsername () != null)
                    answerToClient.put (KeyAnswer.username , mainAccount.getUsername ().getUsername ());

                if (requestInfoUser.isGetEmail ())
                    answerToClient.put (KeyAnswer.email , mainAccount.getEmail ());

                if (requestInfoUser.isGetPhone ())
                    answerToClient.put (KeyAnswer.phone , mainAccount.getPhone ());

                if (requestInfoUser.isGetMyLink ())
                    answerToClient.put (KeyAnswer.mylink , mainAccount.getMyLink ());

                if (requestInfoUser.isGetBio ())
                    answerToClient.put (KeyAnswer.bio , mainAccount.getBio ());

                if (requestInfoUser.isGetListFriends ())
                    answerToClient.put (KeyAnswer.list_friends ,
                            getListFriend (mainAccount.getId () , StatusFriends.friend));

                if (requestInfoUser.isGetListFriendsReject ())
                    answerToClient.put (KeyAnswer.list_friends_reject ,
                            getListFriend (mainAccount.getId () , StatusFriends.rejected));

                if (requestInfoUser.isGetListFriendsReject ())
                    answerToClient.put (KeyAnswer.list_friends_reject ,
                            getListFriend (mainAccount.getId () , StatusFriends.rejected));

                if (requestInfoUser.isGetListFriendsAwaitingApproval ())
                    answerToClient.put (KeyAnswer.list_friends_awaiting_approval ,
                            getListFriend (mainAccount.getId () , StatusFriends.awaiting_approval));

                if (requestInfoUser.isGetListFriendsDeleted ())
                    answerToClient.put (KeyAnswer.list_friends_deleted ,
                            getListFriend (mainAccount.getId () , StatusFriends.deleted));

                if (requestInfoUser.isGetGender ())
                    answerToClient.put (KeyAnswer.gender , mainAccount.getGender ());

            }
            else answerToClient = AnswerToClient.BadRequest ();
        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    private List <String> getListFriend (long id , StatusFriends status)
    {
        return userFriendsService.Repository.findUsernameUser (id , status);
    }

    public enum KeyAnswer
    {
        name, family, username, email, phone, id, gender,
        bio, mylink, list_friends, list_friends_reject, list_friends_awaiting_approval, list_friends_deleted
    }
}
