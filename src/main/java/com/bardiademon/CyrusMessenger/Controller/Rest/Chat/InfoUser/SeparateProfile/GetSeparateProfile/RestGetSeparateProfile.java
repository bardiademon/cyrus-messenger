package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.SeparateProfile.GetSeparateProfile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.IdEnTy;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_GET, method = RequestMethod.POST)
public final class RestGetSeparateProfile
{

    // router1 => baraye method ke etelaat yek SeparateProfile ro migire
    private final String router, router1;

    // type1 => baraye method ke etelaat yek SeparateProfile ro migire
    private final SubmitRequestType type, type1;

    private final UserLoginService userLoginService;
    private final UserSeparateProfilesService userSeparateProfilesService;
    private final EnumTypesService enumTypesService;

    @Autowired
    public RestGetSeparateProfile
            (UserLoginService _UserLoginService ,
             UserSeparateProfilesService _UserSeparateProfilesService , EnumTypesService _EnumTypesService)
    {
        this.userLoginService = _UserLoginService;
        this.userSeparateProfilesService = _UserSeparateProfilesService;
        this.enumTypesService = _EnumTypesService;
        this.router = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_GET;
        this.type = SubmitRequestType.get_user_separate_profile;

        this.router1 = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_GET_ONE;
        this.type1 = SubmitRequestType.get_one_user_separate_profile;
    }

    @RequestMapping (value = { "/" , "" })
    public AnswerToClient get
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        CBSIL both = CBSIL.Both (null , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            List <IdEnTy> idType = userSeparateProfilesService.findIdType (mainAccount.getId ());
            if (idType != null)
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                answerToClient.put (KeyAnswer.sep.name () , idType);
                answerToClient.setReqRes (req , res);
                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ToJson.To (idType));
                r.n (mainAccount , type , false);
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
                answerToClient.setReqRes (req , res);
                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.not_found.name ());
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    @RequestMapping (value = { "/one" , "/one/{id_sep}" })
    public AnswerToClient getOne
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "id_sep") String strIdSep)
    {
        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.n ("id_sep" , strIdSep).toJson ();
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router1 , type1);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            ID idSep = new ID (strIdSep);
            if (idSep.isValid ())
            {
                UserSeparateProfiles sep = userSeparateProfilesService.forUser (idSep.getId () , mainAccount.getId ());

                List <String> sepFor;
                if (sep != null && (sepFor = enumTypesService.Repository.getEnumType (sep.getId ())) != null)
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());

                    sep.setCreatedAtForShowClient (Time.toString (sep.getCreatedAt ()));

                    answerToClient.put (KeyAnswer.sep.name () , sep);
                    answerToClient.put (KeyAnswer.sep_for.name () , sepFor);
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.not_found.name ());
                    r.n (request , type , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_BAD_REQUEST) , AnswerToClient.CUV.not_found.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.not_found.name ());
                    r.n (mainAccount , type , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.IdInvalid ();
                answerToClient.setReqRes (req , res);
                l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.id_invalid.name ());
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum KeyAnswer
    {
        sep, sep_for
    }
}
