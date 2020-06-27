package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.SeparateProfile.RemoveSP;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypes;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// SP => Separate Profile

@RestController
@RequestMapping (value = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_REMOVE, method = RequestMethod.POST)
public final class RestRemoveSP
{

    private final String router;
    private final SubmitRequestType type;
    private final UserLoginService userLoginService;
    private final UserSeparateProfilesService userSeparateProfilesService;
    private final EnumTypesService enumTypesService;

    @Autowired
    public RestRemoveSP
            (UserLoginService _UserLoginService , UserSeparateProfilesService _UserSeparateProfilesService , EnumTypesService _EnumTypesService)
    {
        this.userLoginService = _UserLoginService;
        this.userSeparateProfilesService = _UserSeparateProfilesService;
        this.enumTypesService = _EnumTypesService;
        this.router = Domain.RNChat.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_REMOVE;
        this.type = SubmitRequestType.remove_user_separate_profile;
    }

    @RequestMapping (value = { "" , "/" , "/{ID_SEPARATE_PROFILE}" })
    public AnswerToClient remove
            (@PathVariable (value = "ID_SEPARATE_PROFILE", required = false) String strIdSeparateProfile ,
             HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        CBSIL both = CBSIL.Both (strIdSeparateProfile , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (!Str.IsEmpty (strIdSeparateProfile))
            {
                ID idSeparateProfile = new ID (strIdSeparateProfile);
                if (idSeparateProfile.isValid ())
                {
                    UserSeparateProfiles userSeparateProfiles = userSeparateProfilesService.forUser (idSeparateProfile.getId () , mainAccount.getId ());
                    if (userSeparateProfiles != null)
                    {
                        userSeparateProfiles.setDeleted (true);
                        userSeparateProfiles.setDeletedAt (LocalDateTime.now ());
                        userSeparateProfilesService.Repository.save (userSeparateProfiles);
                        List <EnumTypes> enumTypes = enumTypesService.Repository.findById2AndDeletedFalse (idSeparateProfile.getId ());
                        if (enumTypes != null && enumTypes.size () > 0)
                        {
                            for (int i = 0, len = enumTypes.size (); i < len; i++)
                            {
                                EnumTypes enumType = enumTypes.get (i);
                                enumType.setDeleted (true);
                                enumType.setDeletedAt (LocalDateTime.now ());
                                enumTypes.set (i , enumType);
                            }
                            enumTypesService.Repository.saveAll (enumTypes);
                        }
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed.name ());
                        r.n (mainAccount , type , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.IdInvalid (AnswerToClient.CUV.not_found.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (null , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , null);
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

}
