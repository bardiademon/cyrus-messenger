package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Block.GetBlock;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
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
    private UserLoginService userLoginService;
    private UserBlockedService userBlockedService;

    public GetBlock
            (UserLoginService _UserLoginService , UserBlockedService UserBlockedService)
    {
        this.userLoginService = _UserLoginService;
        this.userBlockedService = UserBlockedService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient getBlock
            (HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        IsLogin isLogin = new IsLogin (codeLogin , userLoginService.Repository);
        if (isLogin.isValid ())
        {
            List<UserBlocked> userBlocked = userBlockedService.listBlocked (isLogin.getVCodeLogin ().getMainAccount ().getId ());
            if (userBlocked == null || userBlocked.size () == 0)
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.not_found.name ());
            else
            {
                UserBlocked blocked;
                for (int i = 0, len = userBlocked.size (); i < len; i++)
                {
                    blocked = userBlocked.get (i);
                    blocked.setIdBlocked (blocked.getMainAccountBlocked ().getId ());
                    blocked.setValidityTimeToJson (Time.toString (blocked.getValidityTime ()));
                    userBlocked.remove (i);
                    userBlocked.add (blocked);
                }
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , userBlocked);
            }
        }
        else answerToClient = isLogin.getAnswerToClient ();

        answerToClient.setResponse (res);
        return answerToClient;
    }

    private enum ValAnswer
    {
        not_found
    }
}
