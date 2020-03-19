package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Update.Security.Profile.ListShowFor.Add;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.UsedRequests.R_IDUsername;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNSecurity.RN_SHOW_PROFILE_FOR_ADD, method = RequestMethod.POST)
public final class RestAddShowProfileFor
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;

    @Autowired
    public RestAddShowProfileFor (UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient add
            (HttpServletResponse res , HttpServletRequest req ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestAddShowProfileFor request)
    {
        AnswerToClient answerToClient;

        String router = Domain.RNChat.RNInfoUser.RNSecurity.RN_SHOW_PROFILE_FOR_ADD;
        SubmitRequestType type = SubmitRequestType.add_show_for_profile;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null && request.getList () != null && request.getList ().size () > 0)
            {
                RequestAddShowProfileFor.Add add = RequestAddShowProfileFor.Add.to (request.getAdd ());
                if (add != null)
                {
                    AnswerCheck answerCheck = checkRequest (request.getList () , mainAccount.getId ());
                    if (answerCheck.errorChecks.size () > 0)
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.error.name ());
                        answerToClient.put (AnswerToClient.CUK.result.name () , answerCheck.errorChecks);
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.error.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                    else
                    {
                        mainAccountService.showProfileForService.add (answerCheck.ids , mainAccountService.repositorySecurityProfile , mainAccount , add);
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.added.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.added.name ());
                        r.n (mainAccount , type , false);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.add_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.add_invalid.name ()) , null);
                    r.n (mainAccount , type , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerCheck checkRequest (List<R_IDUsername> request , long idUserRequested)
    {
        List<ErrorCheck> errorChecks = new ArrayList<> ();
        IdUsernameMainAccount idUsernameMainAccount;
        List<Long> ids = new ArrayList<> ();
        for (R_IDUsername r_idUsername : request)
        {
            idUsernameMainAccount = new IdUsernameMainAccount (mainAccountService , r_idUsername.getId () , r_idUsername.getUsername ());
            if (!idUsernameMainAccount.isValid ())
                errorChecks.add (new ErrorCheck (r_idUsername , idUsernameMainAccount.getAnswerToClient ().getMessage ().toString ()));
            else
            {
                if (!ids.contains (idUsernameMainAccount.getIdUser ()))
                {
                    if (idUserRequested == idUsernameMainAccount.getIdUser ())
                        errorChecks.add (new ErrorCheck (r_idUsername , ValAnswer.it_is_yours.name ()));
                    else ids.add (idUsernameMainAccount.getIdUser ());
                }
                else errorChecks.add (new ErrorCheck (r_idUsername , ValAnswer.repetitious.name ()));
            }
        }

        return new AnswerCheck (errorChecks , ids);
    }

    private static class ErrorCheck
    {
        @JsonProperty ("request")
        private final R_IDUsername r_idUsername;
        private final String answer;

        public ErrorCheck (R_IDUsername r_idUsername , String answer)
        {
            this.r_idUsername = r_idUsername;
            this.answer = answer;
        }

        public R_IDUsername getR_idUsername ()
        {
            return r_idUsername;
        }

        public String getAnswer ()
        {
            return answer;
        }
    }

    private static class AnswerCheck
    {
        private final List<ErrorCheck> errorChecks;
        private final List<Long> ids;

        public AnswerCheck (List<ErrorCheck> errorChecks , List<Long> ids)
        {
            this.errorChecks = errorChecks;
            this.ids = ids;
        }
    }

    private enum ValAnswer
    {
        repetitious, add_invalid, added, it_is_yours
    }

}
