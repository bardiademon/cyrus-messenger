package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Groups.PlacementNumber;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.ManageGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicFor;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.ProfilePictures.SortProfilePictures;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PP_PLACEMENT_NUMBER_GROUP, method = RequestMethod.POST)
public final class RestPlacementNumberGroup
{

    private final UserLoginService userLoginService;
    private final ProfilePicturesService profilePicturesService;
    private final ManageGroup.Service service;

    @Autowired
    public RestPlacementNumberGroup
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             GroupsService _GroupsService ,
             GroupManagementService _GroupManagementService ,
             ProfilePicturesService _ProfilePicturesService)
    {
        this.userLoginService = _UserLoginService;
        this.profilePicturesService = _ProfilePicturesService;
        this.service = new ManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient update
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @RequestBody RequestPlacementNumberGroup request)
    {
        AnswerToClient answerToClient;

        String router = Domain.RNChat.RNProfilePicture.RN_PP_PLACEMENT_NUMBER_GROUP;
        SubmitRequestType type = SubmitRequestType.placement_number_group;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null && request.getPlacementNumberGroup () != null && request.getPlacementNumberGroup ().size () > 0)
            {
                ID idGroup = request.getIdGroup ();
                if (idGroup.isValid ())
                {
                    ManageGroup manageGroup = new ManageGroup (service , idGroup , mainAccount , AccessLevel.change_placement_number);
                    if (manageGroup.canManage ())
                    {
                        AnswerCheckIdProfilePicture answer = checkIdProfilePicture (mainAccount , router , type , manageGroup.getManager ().getGroup ().getId () , res , req , request);
                        if ((answerToClient = answer.answerToClient) == null)
                        {
                            List<ProfilePictures> profilePictures = answer.profilePictures;

                            List<PlacementNumberGroup> placementNumberGroup = request.getPlacementNumberGroup ();

                            ProfilePictures profilePicture;
                            PlacementNumberGroup placementNumber;
                            for (int i = 0, len = profilePictures.size (); i < len; i++)
                            {
                                profilePicture = profilePictures.get (i);
                                placementNumber = placementNumberGroup.get (i);
                                if (placementNumber.getPlacementNumber () < 0) placementNumber.setPlacementNumber (0);

                                profilePicture.setPlacementNumber (placementNumber.getPlacementNumber ());
                                if (placementNumber.isUpdateMain ())
                                    profilePicture.setMainPic (placementNumber.isMain ());
                            }

                            new SortProfilePictures (profilePicturesService , profilePictures);

                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.updated.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.updated.name ());
                            r.n (mainAccount , type , false);
                        }
                    }
                    else
                    {
                        answerToClient = manageGroup.getAnswerToClient ();
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_group_invalid.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_group_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_group_invalid.name ()) , null);
                    r.n (mainAccount , type , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("request is empty") , null);
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerCheckIdProfilePicture checkIdProfilePicture
            (MainAccount mainAccount ,
             String router , SubmitRequestType type , long idGroup ,
             HttpServletResponse res , HttpServletRequest req , RequestPlacementNumberGroup request)
    {
        List<PlacementNumberError> errors = new ArrayList<> ();

        List<PlacementNumberGroup> placementNumberGroup = request.getPlacementNumberGroup ();

        int numberMain = 0;

        AnswerToClient answerToClient;

        for (PlacementNumberGroup placementNumber : placementNumberGroup)
        {
            if (placementNumber.isMain ()) numberMain++;

            if (numberMain >= 2)
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.just_one_main.name ());
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.just_one_main.name ()) , null);
                r.n (mainAccount , type , true);

                return new AnswerCheckIdProfilePicture (answerToClient , null);
            }
        }

        if (numberMain > 0)
            profilePicturesService.Repository.disableMainPhotoGroup (idGroup , ProfilePicFor.group);

        AnswerCheckIdProfilePicture answer = new AnswerCheckIdProfilePicture (null , new ArrayList<> ());

        ID id;
        List<Long> ids = new ArrayList<> ();
        for (PlacementNumberGroup placementNumber : placementNumberGroup)
        {
            if ((id = new ID (placementNumber.getId ())).isValid ())
            {
                if (!ids.contains (id.getId ()))
                {
                    ids.add (id.getId ());
                    ProfilePictures profilePicture = profilePicturesService.getOneGroup (id.getId () , idGroup);
                    if (profilePicture != null) answer.profilePictures.add (profilePicture);
                    else errors.add (new PlacementNumberError (placementNumber , ErrAnswer.id_not_found.name ()));
                }
                else errors.add (new PlacementNumberError (placementNumber , ErrAnswer.duplicate_id.name ()));
            }
            else errors.add (new PlacementNumberError (placementNumber , ErrAnswer.id_invalid.name ()));
        }

        if (errors.size () > 0)
        {
            answer.answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.error.name ());
            answer.answerToClient.put (KeyAnswer.result.name () , errors);
            answer.answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , router , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.just_one_main.name ()) , null);
            r.n (mainAccount , type , true);

        }
        return answer;
    }

    private static class AnswerCheckIdProfilePicture
    {
        private AnswerToClient answerToClient;
        private List<ProfilePictures> profilePictures;

        public AnswerCheckIdProfilePicture (AnswerToClient answerToClient , List<ProfilePictures> profilePictures)
        {
            this.answerToClient = answerToClient;
            this.profilePictures = profilePictures;
        }
    }

    private static class PlacementNumberError extends PlacementNumberGroup
    {
        private String error;

        public PlacementNumberError (PlacementNumberGroup _PlacementNumberGroup , String Error)
        {
            super.setId (_PlacementNumberGroup.getId ());
            super.setMain (_PlacementNumberGroup.isMain ());
            super.setPlacementNumber (_PlacementNumberGroup.getPlacementNumber ());
            super.setUpdateMain (_PlacementNumberGroup.isUpdateMain ());
            this.error = Error;
        }

        public String getError ()
        {
            return error;
        }
    }

    private enum ValAnswer
    {
        id_group_invalid, just_one_main, updated
    }

    private enum ErrAnswer
    {
        id_invalid, id_not_found, duplicate_id
    }

    private enum KeyAnswer
    {
        result
    }

}
