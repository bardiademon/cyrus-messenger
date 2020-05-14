package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Friends.ApproveFriend;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.StatusFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriends;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends.UserFriendsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS_APPROVE, method = RequestMethod.POST)
public final class ApproveFriend
{

    private final String router;
    private final SubmitRequestType type;
    private final UserLoginService userLoginService;
    private final UserFriendsService userFriendsService;
    private final UsernamesService usernamesService;

    @Autowired
    public ApproveFriend
            (UserLoginService _UserLoginService ,
             UserFriendsService _UserFriendsService ,
             UsernamesService _UsernamesService)
    {
        this.userLoginService = _UserLoginService;
        this.userFriendsService = _UserFriendsService;
        this.usernamesService = _UsernamesService;
        this.router = Domain.RNChat.RNInfoUser.RNFriends.RN_FRIENDS_APPROVE;
        this.type = SubmitRequestType.approve_friend;
    }

    @RequestMapping (value = { "" , "/" }, method = RequestMethod.POST)
    public AnswerToClient approve
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestParam (value = "username") String username)
    {
        AnswerToClient answerToClient;
        String request = ToJson.CreateClass.n ("username" , username).toJson ();

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            FITD_Username fitd_username = new FITD_Username (username , usernamesService);
            if (fitd_username.isFound ())
            {
                UserFriends friend = userFriendsService.findFriend (fitd_username.getMainAccount () , mainAccount , StatusFriends.awaiting_approval);
                if (friend != null)
                {
                    friend.setStatus (StatusFriends.friend);
                    userFriendsService.Repository.save (friend);

                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.approved.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.approved.name ());
                    r.n (mainAccount , type , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.friend_not_found.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.friend_not_found.name ()) , null);
                    r.n (mainAccount , type , true);
                }
            }
            else
            {
                answerToClient = fitd_username.getAnswer ();
                answerToClient.setReqRes (req , res);
                l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (FITD_Username.class.getName ()) , null);
                r.n (mainAccount , type , true);
            }
        }
        else
            answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        friend_not_found, approved
    }
}
