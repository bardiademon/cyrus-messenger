package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.SeparateProfile.AddSeparateProfile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypes;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.TAOTL;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_ADD, method = RequestMethod.POST)
public final class RestAddSeparateProfile
{
    private final String router;
    private final SubmitRequestType type;
    private final TAOTL taotl;

    private final UserLoginService userLoginService;
    private final EnumTypesService enumTypesService;
    private UserSeparateProfilesService userSeparateProfilesService;

    public RestAddSeparateProfile
            (UserLoginService _UserLoginService , EnumTypesService _EnumTypesService ,
             UserSeparateProfilesService _UserSeparateProfilesService)
    {
        this.taotl = new TAOTL ();
        this.router = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_ADD;
        this.type = SubmitRequestType.add_user_separate_profile;
        this.userLoginService = _UserLoginService;
        this.enumTypesService = _EnumTypesService;
        this.userSeparateProfilesService = _UserSeparateProfilesService;
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient add
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestAddSeparateProfile request)
    {
        AnswerToClient answerToClient;
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                if (!isEmpty (request))
                {
                    if (Str.IsEmpty (request.getMylink ()) || taotl.isLink (request.getMylink ()))
                    {
                        if (getProfileFor (request.getProfileFor ()))
                        {
                            String found;
                            if ((found = foundEnum (request.getProfileFor () , mainAccount)) == null)
                            {
                                UserSeparateProfiles separateProfiles = new UserSeparateProfiles ();
                                separateProfiles.setBio (request.getBio ());
                                separateProfiles.setFamily (request.getFamily ());
                                separateProfiles.setMylink (request.getMylink ());
                                separateProfiles.setName (request.getName ());
                                separateProfiles.setMainAccount (mainAccount);
                                separateProfiles = userSeparateProfilesService.Repository.save (separateProfiles);
                                if (separateProfiles.getId () > 0)
                                {
                                    toEnumTypesList (request.getProfileFor () , separateProfiles.getId ());

                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.added.name ());
                                    answerToClient.put (AnswerToClient.CUV.id.name () , separateProfiles.getId ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.added.name ());
                                    r.n (mainAccount , type , false);
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.ServerError ();
                                    answerToClient.setReqRes (req , res);
                                    l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null);
                                    r.n (mainAccount , type , true);
                                }
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.profile_for_found.name ());
                                answerToClient.put (AnswerToClient.CUV.found.name () , found);
                                answerToClient.setReqRes (req , res);
                                l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.profile_for_found.name ()) , null);
                                r.n (mainAccount , type , true);
                            }

                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.profile_for_invalid.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.profile_for_invalid.name ()) , null);
                            r.n (mainAccount , type , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.link_invalid.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.link_invalid.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.empty.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.empty.name ()) , null);
                    r.n (mainAccount , type , true);
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

    private boolean getProfileFor (List <String> profileFor)
    {
        if (profileFor != null && profileFor.size () > 0)
        {
            for (String type : profileFor)
            {
                if (AccessLevel.Who.to (type) == null) return false;
            }
            return true;
        }
        else return false;
    }

    private String foundEnum (List <String> profileFor , MainAccount mainAccount)
    {
        for (String type : profileFor)
        {
            if (userSeparateProfilesService.getSeparateProfiles (AccessLevel.Who.to (type) , mainAccount) != null)
                return type;
        }
        return null;
    }

    private void toEnumTypesList (List <String> whos , long id)
    {
        EnumTypes enumType;
        List <EnumTypes> enumTypes = new ArrayList <> ();
        for (String who : whos)
        {
            enumType = new EnumTypes ();
            enumType.setEnumType (who);
            enumType.setId2 (id);
            enumTypes.add (enumType);
        }
        enumTypesService.Repository.saveAll (enumTypes);
    }

    private boolean isEmpty (RequestAddSeparateProfile req)
    {
        return (Str.IsEmpty (req.getName ()) && Str.IsEmpty (req.getBio ()) && Str.IsEmpty (req.getFamily ()) && Str.IsEmpty (req.getMylink ()));
    }

    private enum ValAnswer
    {
        empty, link_invalid, profile_for_invalid, added, profile_for_found
    }
}
