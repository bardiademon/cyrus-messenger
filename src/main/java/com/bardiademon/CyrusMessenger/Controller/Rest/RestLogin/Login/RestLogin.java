package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.Login;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP.IsValidUEPRequest;
import com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP.RestIsValidUEP;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
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

    @Autowired
    public RestLogin (UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient login (@RequestBody LoginRequest request , HttpServletRequest httpServletRequest , HttpServletResponse response)
    {
        RestIsValidUEP restIsValidUEP = new RestIsValidUEP (userLoginService , mainAccountService);
        AnswerToClient isValid = restIsValidUEP.isValid (request.getIsValidUEPRequest () , httpServletRequest);

        Map<String, Object> message = isValid.getMessage ();

        boolean valid = (boolean) message.get (RestIsValidUEP.KeyAnswer.is_valid.name ());

        Object objPhoneIsConfirmed = message.get (RestIsValidUEP.KeyAnswer.phone_is_confirmed.name ());

        boolean phoneIsConfirmed = false;
        if (objPhoneIsConfirmed != null) phoneIsConfirmed = (boolean) objPhoneIsConfirmed;

        boolean isOk = isValid.isOk ();
        boolean is200 = isValid.getStatusCode () == 200;
        if (isOk && is200 && valid && phoneIsConfirmed)
        {
            AnswerToClient answerToClient;
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
                    if (userLoginService.newLogin (code , mainAccount , httpServletRequest.getRemoteAddr ()))
                    {
                        answerToClient = new AnswerToClient (200 , true);
                        answerToClient.put (KeyAnswer.is_login.name () , true);
                        answerToClient.put (KeyAnswer.code_login.name () , code);
                        answerToClient.put (KeyAnswer.credit_up.name () , userLoginService.getCreditUp ());
                        response.addCookie (new Cookie (KEY_CODE_LOGIN_COOKIE , code));
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
            return answerToClient;
        }
        else return isValid;
    }

    private MainAccount checkPassword (String uep , String valueEup , String password)
    {
        if (password == null || password.equals ("")) return null;

        password = (new Hash256 ()).hash (password);

        MainAccount mainAccount = null;
        switch (uep)
        {
            case IsValidUEPRequest.PHONE:
                mainAccount = mainAccountService.Repository.findByPhoneAndPassword (valueEup , password);
                break;
            case IsValidUEPRequest.EMAIL:
                mainAccount = mainAccountService.Repository.findByEmailAndPassword (valueEup , password);
                break;
            case IsValidUEPRequest.USERNAME:
                mainAccount = mainAccountService.Repository.findByUsernameAndPassword (valueEup , password);
                break;
        }
        return mainAccount;
    }


    public enum KeyAnswer
    {
        password_is_valid, is_login, code_login, credit_up
    }
}
