package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.SeparateProfile;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.ETIdName;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypes;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed.Confirmed;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed.ConfirmedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.UserEmails;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails.UserEmailsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserGender;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.IdEnTy;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfiles;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.TAOTL;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNGap.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE, method = RequestMethod.POST)
public final class RestSeparateProfile
{
    private final String rRemove, rGet, rGetOne, rChange, rAdd;

    private final SubmitRequestType tRemove, tGet, tGetOne, tChange, tAdd;

    private final UserLoginService userLoginService;
    private final UserSeparateProfilesService userSeparateProfilesService;
    private final UserEmailsService userEmailsService;
    private final ConfirmedService confirmedService;
    private final ConfirmCodeService confirmedCodeService;
    private final EnumTypesService enumTypesService;
    private final TAOTL taotl;

    @Autowired
    public RestSeparateProfile
            (UserLoginService _UserLoginService ,
             UserSeparateProfilesService _UserSeparateProfilesService ,
             UserEmailsService _UserEmailsService ,
             ConfirmedService _ConfirmedService ,
             ConfirmCodeService _ConfirmedCodeService ,
             EnumTypesService _EnumTypesService)
    {
        this.userLoginService = _UserLoginService;
        this.userSeparateProfilesService = _UserSeparateProfilesService;
        this.userEmailsService = _UserEmailsService;
        this.confirmedService = _ConfirmedService;
        this.confirmedCodeService = _ConfirmedCodeService;
        this.enumTypesService = _EnumTypesService;

        this.rRemove = Domain.RNGap.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_REMOVE;
        this.tRemove = SubmitRequestType.remove_user_separate_profile;

        this.rGet = Domain.RNGap.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_GET;
        this.tGet = SubmitRequestType.get_user_separate_profile;

        this.rGetOne = Domain.RNGap.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_GET_ONE;
        this.tGetOne = SubmitRequestType.get_one_user_separate_profile;

        this.rChange = Domain.RNGap.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_CHANGE;
        this.tChange = SubmitRequestType.change_user_separate_profile;

        this.rAdd = Domain.RNGap.RNInfoUser.RNSeparateProfile.RN_SEPARATE_PROFILE_ADD;
        this.tAdd = SubmitRequestType.add_user_separate_profile;

        this.taotl = new TAOTL ();
    }

    // Start Remove
    @RequestMapping (value = { "/remove" , "/remove/{ID_SEPARATE_PROFILE}" })
    public AnswerToClient remove
    (@PathVariable (value = "ID_SEPARATE_PROFILE", required = false) String strIdSeparateProfile ,
     HttpServletRequest req , HttpServletResponse res ,
     @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;
        CBSIL both = CBSIL.Both (strIdSeparateProfile , req , res , codeLogin , userLoginService , rRemove , tRemove);
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
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.removed);
                        answerToClient.setReqRes (req , res);
                        l.n (null , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.removed);
                        r.n (mainAccount , tRemove , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.IdInvalid (AnswerToClient.CUV.not_found);
                        answerToClient.setReqRes (req , res);
                        l.n (null , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found));
                        r.n (mainAccount , tRemove , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (null , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.id_invalid));
                    r.n (mainAccount , tRemove , true);
                }

            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , rRemove , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null));
                r.n (mainAccount , tRemove , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }
    // End Remove

    // Start Get
    @RequestMapping (value = "/get")
    public AnswerToClient get
    (HttpServletRequest req , HttpServletResponse res ,
     @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin)
    {
        AnswerToClient answerToClient;

        CBSIL both = CBSIL.Both (null , req , res , codeLogin , userLoginService , rGet , tGet);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            List <IdEnTy> idType = userSeparateProfilesService.findIdType (mainAccount.getId ());
            if (idType != null)
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found);
                answerToClient.put (KeyAnswer.sep , idType);
                answerToClient.setReqRes (req , res);
                l.n (null , rGet , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ToJson.To (idType));
                r.n (mainAccount , tGet , false);
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found);
                answerToClient.setReqRes (req , res);
                l.n (null , rGet , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.not_found);
                r.n (mainAccount , tGet , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        System.gc ();
        return answerToClient;
    }
    // End Get

    // Start GetOne
    @RequestMapping (value = { "/get/one" , "/get/one/{id_sep}" })
    public AnswerToClient getOne
    (HttpServletRequest req , HttpServletResponse res ,
     @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
     @PathVariable (value = "id_sep") String strIdSep)
    {
        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.n ("id_sep" , strIdSep).toJson ();
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , rGetOne , tGetOne);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            ID idSep = new ID (strIdSep);
            if (idSep.isValid ())
            {
                UserSeparateProfiles sep = userSeparateProfilesService.forUser (idSep.getId () , mainAccount.getId ());

                List <ETIdName> sepFor;
                if (sep != null && (sepFor = enumTypesService.getEnumType (sep.getId ())) != null)
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found);

                    sep.setCreatedAtForShowClient (Time.toString (sep.getCreatedAt ()));

                    answerToClient.put (KeyAnswer.sep , sep);
                    answerToClient.put (KeyAnswer.sep_for , sepFor);
                    answerToClient.setReqRes (req , res);
                    l.n (request , rGetOne , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.not_found);
                    r.n (request , tGetOne , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_BAD_REQUEST) , AnswerToClient.CUV.not_found);
                    answerToClient.setReqRes (req , res);
                    l.n (request , rGetOne , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.not_found);
                    r.n (mainAccount , tGetOne , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.IdInvalid ();
                answerToClient.setReqRes (req , res);
                l.n (request , rGetOne , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.id_invalid);
                r.n (mainAccount , tGetOne , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        System.gc ();
        return answerToClient;
    }
    //End GetOne

    // Rest Change
    @RequestMapping (value = "/change")
    public AnswerToClient change
    (HttpServletRequest req , HttpServletResponse res ,
     @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
     @RequestBody RequestChangeSP request)
    {
        AnswerToClient answerToClient;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , rChange , tChange);
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

                    if (!Str.IsEmpty (request.getGender ()))
                        sep.setGender (UserGender.to (request.getGender ()));

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

                    if (!Str.IsEmpty (request.getEmailConfirmedCode ()) || request.isEmailNull ())
                    {
                        if (request.isEmailNull ())
                        {
                            UserEmails email = sep.getEmail ();
                            email.setDeleted (true);
                            email.setDeletedAt (LocalDateTime.now ());

                            Confirmed confirmed = confirmedService.fromValue (email.getEmail ());
                            if (confirmed != null)
                            {
                                confirmed.setActive (false);

                                ConfirmCode confirmCode = confirmed.getConfirmCode ();
                                confirmCode.setDeleted (true);
                                confirmCode.setDeletedAt (LocalDateTime.now ());

                                if (confirmCode.getMainAccount () != null)
                                {
                                    confirmCode.setId2 (mainAccount.getId ());
                                    confirmCode.setMainAccount (null);
                                }

                                confirmedCodeService.Repository.save (confirmCode);
                                confirmedService.Repository.save (confirmed);
                                userEmailsService.Repository.save (email);

                                sep.setEmail (null);

                            }
                        }
                    }

                    userSeparateProfilesService.Repository.save (sep);

                    List <ETIdName> profileFor = request.getProfileFor ();
                    if (profileFor != null)
                    {
                        for (ETIdName etIdName : profileFor)
                            enumTypesService.updateEnumType (etIdName.getName () , etIdName.getId () , request.getId ().getId ());

                    }

                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.changed);
                    answerToClient.setReqRes (req , res);
                    l.n (null , rChange , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , AnswerToClient.CUV.changed);
                    r.n (mainAccount , tChange , false);

                    System.gc ();
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , rChange , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null));
                r.n (mainAccount , tChange , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerToClient checkRequest (RequestChangeSP request , MainAccount mainAccount , HttpServletRequest req , HttpServletResponse res)
    {
        AnswerToClient answerToClient = null;
        if (Str.IsEmpty (request.getBio ()) && Str.IsEmpty (request.getFamily ()) && Str.IsEmpty (request.getMylink ()) && Str.IsEmpty (request.getName ()) && Str.IsEmpty (request.getGender ()))
        {
            answerToClient = AnswerToClient.RequestIsNull ();
            answerToClient.setReqRes (req , res);
            l.n (null , rChange , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null));
            r.n (mainAccount , tChange , true);
        }
        else
        {
            if (!Str.IsEmpty (request.getGender ()) && UserGender.to (request.getGender ()) == null)
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.gender_invalid);
                answerToClient.setReqRes (req , res);
                l.n (null , rChange , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.gender_invalid) , tChange , true);
            }
            else
            {
//                if (Str.IsEmpty (request.getName ()))
//                {
//                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.name_is_empty);
//                    answerToClient.setReqRes (req , res);
//                    l.n (null , rChange , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.name_is_empty) , null , tChange , true);
//                }
//                else
//                {
                if (!request.getId ().isValid () || !userSeparateProfilesService.idIsExists (request.getId ().getId () , mainAccount.getId ()))
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.put (ValAnswer.which , ValAnswer.id_sep);
                    answerToClient.setReqRes (req , res);
                    l.n (null , rChange , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.id_invalid));
                    r.n (mainAccount , tChange , true);
                }
                else
                {
//                    if (request.getProfileFor () == null || request.getProfileFor ().size () == 0)
//                    {
//                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.profile_for_is_empty);
//                        answerToClient.setReqRes (req , res);
//                        l.n (null , rChange , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.profile_for_is_empty));
//                        r.n (mainAccount , tChange , true);
//                    }
//                    else
//                    {
                    List <ETIdName> profileFor = request.getProfileFor ();
                    if (profileFor != null)
                    {
                        for (ETIdName etIdName : profileFor)
                        {
                            if (!etIdName.getIdClass ().isValid ()
                                    || AccessLevel.Who.to (etIdName.getName ()) == null
                                    || !enumTypesService.idIsExists (etIdName.getId () , request.getId ().getId ()))
                            {
                                answerToClient = AnswerToClient.IdInvalid ();
                                answerToClient.put (ValAnswer.which , etIdName);
                                answerToClient.setReqRes (req , res);
                                l.n (null , rChange , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.id_invalid));
                                r.n (mainAccount , tChange , true);
                                break;
                            }
                        }
                    }

                    if (answerToClient == null)
                    {
                        if (!Str.IsEmpty (request.getMylink ()) && !taotl.isLink (request.getMylink ()))
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.link_invalid);
                            answerToClient.setReqRes (req , res);
                            l.n (null , rChange , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.link_invalid));
                            r.n (mainAccount , tChange , true);
                        }
                    }
                }
//                }
//                }
            }
        }
        return answerToClient;
    }
    // End Rest Change

    // Start Add
    @RequestMapping (value = "/add")
    public AnswerToClient add
    (HttpServletRequest req , HttpServletResponse res ,
     @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
     @RequestBody RequestAddSP request)
    {
        AnswerToClient answerToClient;
        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , rAdd , tAdd);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                if (!isEmpty (request))
                {
                    if (!Str.IsEmpty (request.getName ()))
                    {
                        UserGender gender = null;
                        if (!Str.IsEmpty (request.getGender ()) && (gender = (UserGender.to (request.getGender ()))) == null)
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.gender_invalid);
                            answerToClient.setReqRes (req , res);
                            l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.gender_invalid) , tAdd , false);
                        }
                        else
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

                                        if (gender != null)
                                            separateProfiles.setGender (UserGender.to (request.getGender ()));

                                        separateProfiles.setMainAccount (mainAccount);
                                        separateProfiles = userSeparateProfilesService.Repository.save (separateProfiles);
                                        if (separateProfiles.getId () > 0)
                                        {
                                            toEnumTypesList (request.getProfileFor () , separateProfiles.getId ());

                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.added);
                                            answerToClient.put (AnswerToClient.CUV.id , separateProfiles.getId ());
                                            answerToClient.setReqRes (req , res);
                                            l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.added);
                                            r.n (mainAccount , tAdd , false);
                                        }
                                        else
                                        {
                                            answerToClient = AnswerToClient.ServerError ();
                                            answerToClient.setReqRes (req , res);
                                            l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.please_try_again));
                                            r.n (mainAccount , tAdd , true);
                                        }
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.profile_for_found);
                                        answerToClient.put (AnswerToClient.CUV.found , found);
                                        answerToClient.setReqRes (req , res);
                                        l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.profile_for_found));
                                        r.n (mainAccount , tAdd , true);
                                    }

                                }
                                else
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.profile_for_invalid);
                                    answerToClient.setReqRes (req , res);
                                    l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.profile_for_invalid));
                                    r.n (mainAccount , tAdd , true);
                                }
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.link_invalid);
                                answerToClient.setReqRes (req , res);
                                l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.link_invalid));
                                r.n (mainAccount , tAdd , true);
                            }
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.name_is_empty);
                        answerToClient.setReqRes (req , res);
                        l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.name_is_empty));
                        r.n (mainAccount , tAdd , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.empty);
                    answerToClient.setReqRes (req , res);
                    l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.empty));
                    r.n (mainAccount , tAdd , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , rAdd , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.request_is_null));
                r.n (mainAccount , tAdd , true);
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

    private boolean isEmpty (RequestAddSP req)
    {
        return (Str.IsEmpty (req.getName ()) && Str.IsEmpty (req.getBio ()) && Str.IsEmpty (req.getFamily ()) && Str.IsEmpty (req.getMylink ()));
    }
    // End Add

    private enum KeyAnswer
    {
        sep, sep_for
    }

    private enum ValAnswer
    {
        gender_invalid

        /* profile_for_is_empty */, which, id_sep, link_invalid, name_is_empty,

        // For Rest Add
        empty, profile_for_invalid, added, profile_for_found
    }
}
