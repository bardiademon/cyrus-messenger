package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VEmail;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VPhone;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.Status;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus.UsersStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping (value = "/api/login/is_valid_uep", method = RequestMethod.POST)
public class RestIsValidUEP
{

    private IsValidUEPRequest request;

    private final MainAccountService mainAccountService;
    private UsersStatusService usersStatusService;

    @Autowired
    public RestIsValidUEP (MainAccountService _MainAccountService , UsersStatusService _UsersStatusService)
    {
        this.mainAccountService = _MainAccountService;
        this.usersStatusService = _UsersStatusService;
    }

    @RequestMapping ({"/" , ""})
    public AnswerToClient isValid (@RequestBody IsValidUEPRequest request , HttpServletResponse res)
    {
        AnswerToClient answerToClient;
        this.request = request;
        if (!validation ()) answerToClient = valid (false);
        else
        {
            MainAccount mainAccount;
            switch (request.getValueUEP ())
            {
                case IsValidUEPRequest.PHONE:
                {
                    mainAccount = mainAccountService.findPhone (request.getUep ());
                    if (mainAccount != null)
                    {
                        if ((answerToClient = isActive (mainAccount)) == null)
                            answerToClient = valid (true);
                    }
                    else answerToClient = valid (false);
                }
                break;
                case IsValidUEPRequest.EMAIL:
                {
                    mainAccount = mainAccountService.findEmail (request.getUep ());
                    if (mainAccount != null)
                    {
                        if ((answerToClient = isActive (mainAccount)) == null)
                        {
                            if (!emailIsActive (mainAccount.getId ()))
                                answerToClient = error401 ();
                            else answerToClient = valid (true);
                        }
                    }
                    else answerToClient = valid (false);
                }
                break;
                case IsValidUEPRequest.USERNAME:
                    mainAccount = mainAccountService.findUsername (request.getUep ());
                    if (mainAccount != null)
                    {
                        if ((answerToClient = isActive (mainAccount)) == null)
                            answerToClient = valid (true);
                    }
                    else answerToClient = valid (false);
                    break;
                default:
                    request.setValueUEP (String.format ("%s,%s,%s" , IsValidUEPRequest.USERNAME , IsValidUEPRequest.EMAIL , IsValidUEPRequest.PHONE));
                    answerToClient = valid (false);
            }

        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private AnswerToClient isActive (MainAccount mainAccount)
    {
        if (mainAccount.isActive ()) return null;
        else
        {
            AnswerToClient answerToClient = AnswerToClient.AccountDeactive ();
            answerToClient.put (KeyAnswer.is_valid.name () , true);
            return answerToClient;
        }
    }

    private AnswerToClient error401 ()
    {
        AnswerToClient answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED);
        answerToClient.put (KeyAnswer.uep.name () , this.request.getValueUEP ());
        answerToClient.put (KeyAnswer.is_valid.name () , true);
        answerToClient.put (KeyAnswer.email_is_confirmed.name () , false);
        return answerToClient;
    }

    private AnswerToClient valid (boolean valid)
    {
        AnswerToClient answerToClient;
        answerToClient = (valid ? AnswerToClient.OK () : AnswerToClient.error400 ());

        answerToClient.put (KeyAnswer.uep.name () , this.request.getValueUEP ());
        answerToClient.put (KeyAnswer.is_valid.name () , valid);
        return answerToClient;
    }

    private boolean emailIsActive (long id)
    {
        return isConfirmed (id);
    }

    private boolean isConfirmed (long idUser)
    {
        return usersStatusService.Repository.findByMainAccountIdAndStatusAndActiveRowTrue (idUser , Status.confirmed_email) != null;
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
        uep, is_valid, email_is_confirmed
    }
}
