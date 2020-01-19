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
        if (!validation ()) answerToClient = valid (false , 0);
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
                        if (!phoneIsActive (mainAccount.getId ()))
                            answerToClient = error401 (KeyAnswer.phone_is_confirmed , mainAccount.getId ());
                        else answerToClient = valid (mainAccount.getId ());
                    }
                    else answerToClient = valid (false , 0);
                }
                break;
                case IsValidUEPRequest.EMAIL:
                {
                    mainAccount = mainAccountService.findEmail (request.getUep ());
                    if (mainAccount != null)
                    {
                        if (!emailIsActive (mainAccount.getId ()))
                            answerToClient = error401 (KeyAnswer.email_is_confirmed , mainAccount.getId ());
                        else answerToClient = valid (mainAccount.getId ());
                    }
                    else answerToClient = valid (false , 0);
                }
                break;
                case IsValidUEPRequest.USERNAME:
                    mainAccount = mainAccountService.findUsername (request.getUep ());
                    if (mainAccount != null) answerToClient = valid (mainAccount.getId ());
                    else answerToClient = valid (false , 0);
                    break;
                default:
                    request.setValueUEP (String.format ("%s,%s,%s" , IsValidUEPRequest.USERNAME , IsValidUEPRequest.EMAIL , IsValidUEPRequest.PHONE));
                    answerToClient = valid (false , 0);
            }

        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private AnswerToClient error401 (KeyAnswer keyAnswer , long idUser)
    {
        AnswerToClient answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED);
        answerToClient.put (KeyAnswer.uep.name () , this.request.getValueUEP ());
        answerToClient.put (KeyAnswer.is_valid.name () , true);
        answerToClient.put (keyAnswer.name () , false);
        if (!keyAnswer.equals (KeyAnswer.phone_is_confirmed) && idUser > 0)
            answerToClient.put (KeyAnswer.phone_is_confirmed.name () , phoneIsActive (idUser));
        return answerToClient;
    }

    private AnswerToClient valid (long idUser)
    {
        return valid (true , idUser);
    }

    private AnswerToClient valid (boolean valid , long idUser)
    {
        AnswerToClient answerToClient;
        boolean phoneActive = false;
        if (idUser > 0)
            answerToClient = ((phoneActive = phoneIsActive (idUser)) ? AnswerToClient.OK () : AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED));
        else answerToClient = (valid ? AnswerToClient.OK () : AnswerToClient.error400 ());

        answerToClient.put (KeyAnswer.uep.name () , this.request.getValueUEP ());
        answerToClient.put (KeyAnswer.is_valid.name () , valid);
        if (idUser > 0) answerToClient.put (KeyAnswer.phone_is_confirmed.name () , phoneActive);
        return answerToClient;
    }

    private boolean phoneIsActive (long id)
    {
        return isConfirmed (id , Status.conformed_phone);
    }

    private boolean emailIsActive (long id)
    {
        return isConfirmed (id , Status.confirmed_email);
    }

    private boolean isConfirmed (long idUser , Status status)
    {
        return usersStatusService.Repository.findByMainAccountIdAndStatusAndActiveRowTrue (idUser , status) != null;
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
        uep, is_valid, phone_is_confirmed, email_is_confirmed
    }
}
