package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Modify.ModifyInfoUser;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserGender;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import static com.bardiademon.CyrusMessenger.bardiademon.Str.IsEmpty;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.Modify.RN_MODIFY_INFO_USER, method = RequestMethod.POST)
public final class ModifyInfoUser
{

    private final UserLoginService userLoginService;
    private final MainAccountService mainAccountService;

    private final String router;
    private final SubmitRequestType type;

    @Autowired
    public ModifyInfoUser (UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;

        this.router = Domain.RNChat.RNNewInfoUser.RN_GENERAL;
        this.type = SubmitRequestType.modify_info_user;
    }

    @RequestMapping (value = { "" , "/" })
    private AnswerToClient newInfoUser
            (HttpServletRequest req , HttpServletResponse res , @RequestBody RequestMIU requestMIU ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        CBSIL both = CBSIL.Both (requestMIU , req , res , codeLogin , userLoginService , router , type);

        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (requestMIU != null)
            {
                String request = ToJson.To (requestMIU);

                if (IsEmpty (requestMIU.getGender ())
                        && IsEmpty (requestMIU.getBio ()) && IsEmpty (requestMIU.getFamily ())
                        && IsEmpty (requestMIU.getMylink ()) && IsEmpty (requestMIU.getName ()))
                {
                    answerToClient = AnswerToClient.RequestIsNull ();
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null , type , true);
                }
                else
                {
                    if (!IsEmpty (requestMIU.getGender ()) && (UserGender.to (requestMIU.getGender ())) == null)
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.gender_invalid.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.gender_invalid.name ()) , null , type , true);
                    }
                    else
                    {
                        requestMIU = mainAccountService.updateInfoUser (mainAccount , requestMIU);
                        answerToClient = AnswerToClient.OK ();

                        if (requestMIU.isUpdatedBio () || !IsEmpty (requestMIU.getBio ()))
                            answerToClient.put (KeyAnswer.bio.name () , true);

                        if (requestMIU.isUpdatedName () || !IsEmpty (requestMIU.getName ()))
                            answerToClient.put (KeyAnswer.name.name () , true);

                        if (requestMIU.isUpdatedFamily () || !IsEmpty (requestMIU.getFamily ()))
                            answerToClient.put (KeyAnswer.family.name () , true);

                        if (requestMIU.isUpdatedMylink () || !IsEmpty (requestMIU.getMylink ()))
                            answerToClient.put (KeyAnswer.mylink.name () , true);

                        if (requestMIU.isUpdatedGender () || !IsEmpty (requestMIU.getGender ()))
                            answerToClient.put (KeyAnswer.gender.name () , true);

                        answerToClient.setReqRes (req , res);
                        l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.check_the_request.name ()) , null , type , false);
                    }
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null , type , true);
            }

        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        check_the_request, gender_invalid
    }

    private enum KeyAnswer
    {
        bio, name, family, mylink, gender
    }


}
