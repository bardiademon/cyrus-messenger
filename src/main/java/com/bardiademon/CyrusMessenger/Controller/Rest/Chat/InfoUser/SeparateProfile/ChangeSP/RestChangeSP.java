package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.SeparateProfile.ChangeSP;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.ETIdName;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.TAOTL;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// SP => Separate Profile

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_CHANGE, method = RequestMethod.POST)
public final class RestChangeSP
{
    private final String router;
    private final SubmitRequestType type;
    private final UserLoginService userLoginService;
    private final UserSeparateProfilesService userSeparateProfilesService;
    private final EnumTypesService enumTypesService;
    private final TAOTL taotl;

    @Autowired
    public RestChangeSP
            (UserLoginService _UserLoginService ,
             UserSeparateProfilesService _UserSeparateProfilesService ,
             EnumTypesService _EnumTypesService)
    {
        this.userLoginService = _UserLoginService;
        this.userSeparateProfilesService = _UserSeparateProfilesService;
        this.enumTypesService = _EnumTypesService;
        this.router = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_CHANGE;
        this.type = SubmitRequestType.change_user_separate_profile;
        this.taotl = new TAOTL ();
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient change
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestChangeSP request)
    {
        AnswerToClient answerToClient;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                if ((answerToClient = checkRequest (request , mainAccount , req , res)) == null)
                {
                    UserSeparateProfiles sep = userSeparateProfilesService.forUser (request.getId ().getId () , mainAccount.getId ());

                    if (!Str.IsEmpty (request.getName ()))
                        sep.setName (request.getName ());

                    if (!Str.IsEmpty (request.getMylink ()) || request.isMylinkNull ())
                    {
                        if (request.isMylinkNull ()) sep.setMylink ("");
                        else sep.setMylink (request.getMylink ());
                    }
                    if (!Str.IsEmpty (request.getBio ()) || request.isBioNull ())
                    {
                        if (request.isBioNull ()) sep.setBio ("");
                        else sep.setBio (request.getBio ());
                    }
                    if (!Str.IsEmpty (request.getFamily ()) || request.isFamilyNull ())
                    {
                        if (request.isFamilyNull ()) sep.setFamily ("");
                        else sep.setFamily (request.getFamily ());
                    }

                    userSeparateProfilesService.Repository.save (sep);

                    List <ETIdName> profileFor = request.getProfileFor ();
                    for (ETIdName etIdName : profileFor)
                        enumTypesService.updateEnumType (etIdName.getName () , etIdName.getId () , request.getId ().getId ());

                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.changed.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.changed.name ());
                    r.n (mainAccount , type , false);

                    System.gc ();
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerToClient checkRequest (RequestChangeSP request , MainAccount mainAccount , HttpServletRequest req , HttpServletResponse res)
    {
        AnswerToClient answerToClient = null;
        if (Str.IsEmpty (request.getBio ()) && Str.IsEmpty (request.getFamily ()) && Str.IsEmpty (request.getMylink ()) && Str.IsEmpty (request.getName ()))
        {
            answerToClient = AnswerToClient.RequestIsNull ();
            answerToClient.setReqRes (req , res);
            l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
            r.n (mainAccount , type , true);
        }
        else
        {
            if (Str.IsEmpty (request.getName ()))
            {

                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.name_is_empty.name ());
                answerToClient.setReqRes (req , res);
                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.name_is_empty.name ()) , null);
                r.n (mainAccount , type , true);
            }
            else
            {
                if (!request.getId ().isValid () || !userSeparateProfilesService.idIsExists (request.getId ().getId () , mainAccount.getId ()))
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.put (ValAnswer.which.name () , ValAnswer.id_sep.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , null);
                    r.n (mainAccount , type , true);
                }
                else
                {
                    if (request.getProfileFor () == null || request.getProfileFor ().size () == 0)
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.profile_for_is_empty.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.profile_for_is_empty.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                    else
                    {
                        List <ETIdName> profileFor = request.getProfileFor ();
                        for (ETIdName etIdName : profileFor)
                        {
                            if (!etIdName.getIdClass ().isValid ()
                                    || AccessLevel.Who.to (etIdName.getName ()) == null
                                    || !enumTypesService.idIsExists (etIdName.getId () , request.getId ().getId ()))
                            {
                                answerToClient = AnswerToClient.IdInvalid ();
                                answerToClient.put (ValAnswer.which.name () , etIdName);
                                answerToClient.setReqRes (req , res);
                                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , null);
                                r.n (mainAccount , type , true);
                                break;
                            }
                        }
                        if (answerToClient == null)
                        {
                            if (!Str.IsEmpty (request.getMylink ()) && !taotl.isLink (request.getMylink ()))
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.link_invalid.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.link_invalid.name ()) , null);
                                r.n (mainAccount , type , true);
                            }
                        }
                    }
                }
            }

        }
        return answerToClient;
    }

    private enum ValAnswer
    {
        profile_for_is_empty, which, id_sep, link_invalid, name_is_empty
    }
}
