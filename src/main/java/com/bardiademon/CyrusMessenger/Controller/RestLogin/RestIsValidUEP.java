package com.bardiademon.CyrusMessenger.Controller.RestLogin;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Vaidation.VEmail;
import com.bardiademon.CyrusMessenger.Controller.Vaidation.VPhone;
import com.bardiademon.CyrusMessenger.Controller.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountStatus;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping (value = "/login/is_valid_uep", method = RequestMethod.POST)
public class RestIsValidUEP
{

    private LoginRequest request;

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;

    @Autowired
    public RestIsValidUEP (UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping ({"/" , ""})
    public Object login (@RequestBody LoginRequest request , HttpServletRequest httpServletRequest)
    {
        this.request = request;
        if (!validation ()) return error (httpServletRequest , "Phone,Username,Email");
        else
        {
            MainAccount mainAccount;
            switch (request.getValueUEP ())
            {
                case LoginRequest.PHONE:
                    mainAccount = mainAccountService.Repository.findByPhone (request.uep);
                    break;
                case LoginRequest.EMAIL:
                    mainAccount = mainAccountService.Repository.findByEmail (request.uep);
                    break;
                case LoginRequest.USERNAME:
                    mainAccount = mainAccountService.Repository.findByUsername (request.uep);
                    break;
                default:
                    return error (httpServletRequest , "Phone,Username,Email");
            }
            if (mainAccount == null)
                return error (httpServletRequest , this.request.getValueUEP ());
            else
            {
                AnswerToClient answerToClient = new AnswerToClient (200 , true);
                userLoginService.validUEP (httpServletRequest.getRemoteAddr ());
                answerToClient.put ("uep" , "valid");
                answerToClient.put ("phone" , phoneIsActive (mainAccount.getId ()));
                return answerToClient;
            }
        }
    }

    private String phoneIsActive (long id)
    {
        MainAccount byIdAndStatusNot = mainAccountService.Repository.findByIdAndStatusNot (id , MainAccountStatus.phone_not_confirmed);
        return (byIdAndStatusNot != null) ? "confirmed" : "not confirmed";
    }

    private AnswerToClient error (HttpServletRequest httpServletRequest , String invalid)
    {
        AnswerToClient answerToClient = AnswerToClient.error400 ();
        answerToClient.put ("uep" , String.format ("%s is invalid" , invalid));
        userLoginService.loginFailed (httpServletRequest.getRemoteAddr ());
        return answerToClient;
    }

    private boolean validation ()
    {
        boolean isJustNumber = request.uep.matches ("[0-9]*");

        if (new VPhone (request.uep , request.region).check ()) request.setValueUEP (LoginRequest.PHONE);
        else if (!isJustNumber && new VUsername (request.uep).check ()) request.setValueUEP (LoginRequest.USERNAME);
        else if (!isJustNumber && new VEmail (request.uep).check ()) request.setValueUEP (LoginRequest.EMAIL);
        else return false;

        return true;
    }
}
