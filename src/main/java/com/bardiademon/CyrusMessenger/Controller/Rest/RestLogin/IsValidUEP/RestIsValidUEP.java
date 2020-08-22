package com.bardiademon.CyrusMessenger.Controller.Rest.RestLogin.IsValidUEP;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VEmail;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VPhone;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.EmailFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.UserEmails;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping (value = Domain.RNLogin.RN_IS_VALID_UEP, method = RequestMethod.POST)
public class RestIsValidUEP
{

    private final MainAccountService mainAccountService;
    private final SubmitRequestService submitRequestService;
    private final UsernamesService usernamesService;

    @Autowired
    public RestIsValidUEP
            (MainAccountService _MainAccountService ,
             SubmitRequestService _SubmitRequestService ,
             UsernamesService _UsernamesService)
    {
        this.mainAccountService = _MainAccountService;
        this.submitRequestService = _SubmitRequestService;
        this.usernamesService = _UsernamesService;
    }

    @RequestMapping ({ "/" , "" })
    public AnswerToClient isValid (@RequestBody IsValidUEPRequest request , HttpServletResponse res , HttpServletRequest servletRequest , boolean loginReq)
    {
        AnswerToClient answerToClient;

        CheckBlockSystem checkBlockSystem;
        if (!loginReq && (checkBlockSystem = new CheckBlockSystem (servletRequest , submitRequestService.blockedByTheSystemService , BlockedFor.submit_request , SubmitRequestType.login.name ())).isBlocked ())
            answerToClient = checkBlockSystem.getAnswerToClient ();
        else
        {
            if (!validation (request)) answerToClient = valid (false , servletRequest , loginReq , request);
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
                            if ((answerToClient = isActive (mainAccount , servletRequest)) == null)
                                answerToClient = valid (true , servletRequest , loginReq , request);
                        }
                        else answerToClient = valid (false , servletRequest , loginReq , request);
                    }
                    break;
                    case IsValidUEPRequest.EMAIL:
                    {
                        UserEmails userEmails = mainAccountService.userEmailsService.find (request.getValueUEP () , EmailFor.ma);
                        if (userEmails != null)
                        {
                            mainAccount = userEmails.getMainAccount ();
                            if ((answerToClient = isActive (mainAccount , servletRequest)) == null)
                            {
                                if (!userEmails.isConfirmed ())
                                    answerToClient = error401 (servletRequest , request);
                                else answerToClient = valid (true , servletRequest , loginReq , request);
                            }
                        }
                        else answerToClient = valid (false , servletRequest , loginReq , request);
                    }
                    break;
                    case IsValidUEPRequest.USERNAME:
                        mainAccount = new FITD_Username (request.getUep () , usernamesService).getMainAccount ();
                        if (mainAccount != null)
                        {
                            if ((answerToClient = isActive (mainAccount , servletRequest)) == null)
                                answerToClient = valid (true , servletRequest , loginReq , request);
                        }
                        else answerToClient = valid (false , servletRequest , loginReq , request);
                        break;
                    default:
                        request.setValueUEP (String.format ("%s,%s,%s" , IsValidUEPRequest.USERNAME , IsValidUEPRequest.EMAIL , IsValidUEPRequest.PHONE));
                        answerToClient = valid (false , servletRequest , loginReq , request);
                }

            }
        }
        answerToClient.setResponse (res);
        return answerToClient;
    }

    private AnswerToClient isActive (MainAccount mainAccount , HttpServletRequest servletRequest)
    {
        if (mainAccount.isActive ()) return null;
        else
        {
            AnswerToClient answerToClient = AnswerToClient.AccountDeactive ();
            answerToClient.put (KeyAnswer.is_valid.name () , true);
            submitRequestService.newRequest (servletRequest.getRemoteAddr () , SubmitRequestType.login , true);
            return answerToClient;
        }
    }

    private AnswerToClient error401 (HttpServletRequest servletRequest , IsValidUEPRequest request)
    {
        AnswerToClient answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED);
        answerToClient.put (KeyAnswer.uep.name () , request.getValueUEP ());
        answerToClient.put (KeyAnswer.is_valid.name () , true);
        answerToClient.put (KeyAnswer.email_is_confirmed.name () , false);

        submitRequestService.newRequest (servletRequest.getRemoteAddr () , SubmitRequestType.login , true);

        return answerToClient;
    }

    private AnswerToClient valid (boolean valid , HttpServletRequest servletRequest , boolean loginReq , IsValidUEPRequest request)
    {
        AnswerToClient answerToClient;
        answerToClient = (valid ? AnswerToClient.OK () : AnswerToClient.error400 ());

        answerToClient.put (KeyAnswer.uep.name () , request.getValueUEP ());
        answerToClient.put (KeyAnswer.is_valid.name () , valid);

        if (!loginReq)
            submitRequestService.newRequest (servletRequest.getRemoteAddr () , SubmitRequestType.login , !valid);

        return answerToClient;
    }

    private boolean validation (IsValidUEPRequest request)
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
