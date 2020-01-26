package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Block.NewBlock;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login.RestLogin;
import com.bardiademon.CyrusMessenger.Controller.Rest.RouterName;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = RouterName.RNChat.RNInfoUser.RNBlock.RN_NEW_BLOCK, method = RequestMethod.POST)
public final class NewBlock
{

    private UserLoginService userLoginService;
    private MainAccountService mainAccountService;
    private UserBlockedService userBlockedService;

    public NewBlock
            (UserLoginService _UserLoginService , MainAccountService _MainAccountService , UserBlockedService UserBlockedService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.userBlockedService = UserBlockedService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient newBlock
            (HttpServletResponse res ,
             @CookieValue (value = RestLogin.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestNewBlock request)
    {
        AnswerToClient answerToClient;

        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        if (checkLogin.isValid ())
        {
            if (request == null || request.getExtent () <= 0 || Str.IsEmpty (request.getUsername ()) || Str.IsEmpty (request.getPlusUpTo ()))
                answerToClient = AnswerToClient.RequestIsNull ();
            else
            {
                VUsername vUsername = new VUsername (request.getUsername ());
                if (vUsername.check ())
                {
                    MainAccount username = mainAccountService.findUsername (request.getUsername ());
                    if (username == null)
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_not_found.name ());
                    else
                    {
                        MainAccount mainAccount = checkLogin.getVCodeLogin ().getMainAccount ();

                        if (mainAccount.getId () == username.getId ())
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.you_cannot_block_yourself.name ());
                        else
                        {
                            RequestNewBlock.PlusUpTo plusUpTo = request.checkPlusToUp (request.getPlusUpTo ());
                            UserBlocked.Type type = checkType (request.getType ());
                            if (plusUpTo == null)
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.plus_up_to_invalid.name ());
                            else if (type == null)
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.type_invalid.name ());
                            else
                            {
                                UserBlocked blocked
                                        = userBlockedService.isBlocked (mainAccount.getId () , username.getId ());

                                if (request.isBlock ())
                                {
                                    if (blocked == null)
                                    {
                                        blocked = new UserBlocked ();
                                        blocked.setMainAccount (mainAccount);
                                        blocked.setMainAccountBlocked (username);
                                        blocked.setValidityTime (getValidityTime (plusUpTo , request.getExtent ()));
                                        blocked.setType (type);
                                        blocked = userBlockedService.Repository.save (blocked);
                                        if (blocked.getId () > 0)
                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.blocked.name ());
                                        else
                                            answerToClient = AnswerToClient.ServerError ();
                                    }
                                    else
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.already_blocked.name ());
                                }
                                else
                                {
                                    if (blocked == null)
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.not_blocked.name ());
                                    else
                                    {
                                        blocked.setUnblocked (true);
                                        blocked.setUnblockedAt (LocalDateTime.now ());
                                        userBlockedService.Repository.save (blocked);
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.unblocked.name ());
                                    }
                                }
                            }
                        }
                    }
                }
                else
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_invalid.name ());
            }
        }
        else answerToClient = checkLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    private LocalDateTime getValidityTime (RequestNewBlock.PlusUpTo plusUpTo , int extent)
    {
        LocalDateTime now = LocalDateTime.now ();
        switch (plusUpTo)
        {
            case minutes:
                return now.plusMinutes (extent);
            case hour:
                return now.plusHours (extent);
            case month:
                return now.plusMonths (extent);
            case year:
                return now.plusYears (extent);
            default:
                return now;
        }
    }

    public UserBlocked.Type checkType (String type)
    {
        try
        {
            return UserBlocked.Type.valueOf (type);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private enum ValAnswer
    {
        username_invalid, username_not_found, plus_up_to_invalid,
        you_cannot_block_yourself, blocked, already_blocked, not_blocked, unblocked, type_invalid
    }


}