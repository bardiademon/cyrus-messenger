package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP.IsValidUEPRequest;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP.RestIsValidUEP;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.UsersStatusService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping (value = "/api/login", method = RequestMethod.POST)
public class RestLogin
{

    public static final String KEY_CODE_LOGIN_COOKIE = "code_login";

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;
    private UsersStatusService usersStatusService;

    @Autowired
    public RestLogin (UserLoginService _UserLoginService , MainAccountService _MainAccountService , UsersStatusService _UsersStatusService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
        this.usersStatusService = _UsersStatusService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient login (@RequestBody LoginRequest request , HttpServletRequest req , HttpServletResponse res)
    {
        AnswerToClient answerToClient;

        RestIsValidUEP restIsValidUEP = new RestIsValidUEP (mainAccountService , usersStatusService);
        answerToClient = restIsValidUEP.isValid (request.getIsValidUEPRequest () , res);

        Map<String, Object> message = answerToClient.getMessage ();

        Object objValid;
        boolean valid = (boolean) ((objValid = message.get (RestIsValidUEP.KeyAnswer.is_valid.name ())) == null ? false : objValid);


        boolean isOk = answerToClient.isOk ();
        boolean is200 = answerToClient.getStatusCode () == 200;
        if (isOk && is200 && valid)
        {
            MainAccount mainAccount;
            if ((request.getPassword () != null && !request.getPassword ().equals ("")) && (mainAccount = checkPassword (request.getValueUEP () , request.getUep () , request.getPassword ())) != null)
            {
                int counterCreateCode = 0;

                String code = null;
                boolean createCode = false;
                int MAX_CREATE_CODE = 5;
                while ((counterCreateCode++) <= MAX_CREATE_CODE)
                {
                    code = Code.CreateCodeLogin ();
                    if (userLoginService.Repository.findByCodeLogin (code) == null)
                    {
                        createCode = true;
                        break;
                    }
                }
                if (createCode)
                {
                    assert code != null;
                    if (userLoginService.newLogin (code , mainAccount , req.getRemoteAddr ()))
                    {
                        answerToClient = new AnswerToClient (200 , true);
                        answerToClient.put (KeyAnswer.is_login.name () , true);
                        answerToClient.put (KeyAnswer.code_login.name () , code);
                        answerToClient.put (KeyAnswer.credit_up.name () , userLoginService.getCreditUp ());
                        res.addCookie (new Cookie (KEY_CODE_LOGIN_COOKIE , code));
                    }
                    else answerToClient = AnswerToClient.ServerError ();
                }
                else answerToClient = AnswerToClient.ServerError ();
            }
            else
            {
                answerToClient = new AnswerToClient (400 , false);
                answerToClient.put (KeyAnswer.password_is_valid.name () , false);
            }
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private MainAccount checkPassword (String uep , String valueEup , String password)
    {
        if (password == null || password.equals ("")) return null;

        password = (new Hash256 ()).hash (password);

        MainAccount mainAccount = null;
        switch (uep)
        {
            case IsValidUEPRequest.PHONE:
                mainAccount = mainAccountService.findPhone (valueEup , password);
                break;
            case IsValidUEPRequest.EMAIL:
                mainAccount = mainAccountService.findEmail (valueEup , password);
                break;
            case IsValidUEPRequest.USERNAME:
                mainAccount = mainAccountService.findUsername (valueEup , password);
                break;
        }
        return mainAccount;
    }

    public enum KeyAnswer
    {
        password_is_valid, is_login, code_login, credit_up
    }
}
