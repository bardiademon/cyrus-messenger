package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VEmail;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VPhone;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
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
@RequestMapping (value = "/api/login/is_valid_uep", method = RequestMethod.POST)
public class RestIsValidUEP
{

    private IsValidUEPRequest request;

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;

    @Autowired
    public RestIsValidUEP (UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient isValid (@RequestBody IsValidUEPRequest request , HttpServletRequest httpServletRequest)
    {
        this.request = request;
        if (!validation ()) return error (httpServletRequest , "Phone,Username,Email");
        else
        {
            MainAccount mainAccount;
            switch (request.getValueUEP ())
            {
                case IsValidUEPRequest.PHONE:
                    mainAccount = mainAccountService.Repository.findByPhone (request.getUep ());
                    break;
                case IsValidUEPRequest.EMAIL:
                    mainAccount = mainAccountService.Repository.findByEmail (request.getUep ());
                    break;
                case IsValidUEPRequest.USERNAME:
                    mainAccount = mainAccountService.Repository.findByUsername (request.getUep ());
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
                answerToClient.put (KeyAnswer.uep.name () , this.request.getValueUEP ());
                answerToClient.put (KeyAnswer.is_valid.name () , true);
                answerToClient.put (KeyAnswer.phone_is_confirmed.name () , phoneIsActive (mainAccount.getId ()));
                return answerToClient;
            }
        }
    }

    private boolean phoneIsActive (long id)
    {
        MainAccount byIdAndStatusNot = mainAccountService.Repository.findByIdAndStatusNot (id , MainAccountStatus.phone_not_confirmed);
        return (byIdAndStatusNot != null);
    }

    private AnswerToClient error (HttpServletRequest httpServletRequest , String uep)
    {
        AnswerToClient answerToClient = AnswerToClient.error400 ();
        answerToClient.put (KeyAnswer.uep.name () , uep);
        answerToClient.put (KeyAnswer.is_valid.name () , false);
        userLoginService.loginFailed (httpServletRequest.getRemoteAddr ());
        return answerToClient;
    }

    private boolean validation ()
    {
        try
        {
            boolean isJustNumber = request.getUep ().matches ("[0-9]*");

            VPhone vPhone;
            if ((vPhone = new VPhone (request.getUep () , request.region)).check ())
            {
                request.setUep (vPhone.getPhone ());
                request.setValueUEP (IsValidUEPRequest.PHONE);
            }

            else if (!isJustNumber && new VUsername (request.getUep ()).check ())
                request.setValueUEP (IsValidUEPRequest.USERNAME);
            else if (!isJustNumber && new VEmail (request.getUep ()).check ())
                request.setValueUEP (IsValidUEPRequest.EMAIL);
            else return false;

            return true;
        }
        catch (NullPointerException e)
        {
            return false;
        }
    }

    public enum KeyAnswer
    {
        uep, is_valid, phone_is_confirmed
    }
}
