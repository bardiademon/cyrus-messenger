package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Groups.PlacementNumber;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.PlacementNumber.CheckPlacementNumber;
import com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.PlacementNumber.UpdatePlacementNumber;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.ManageGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
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

@RestController
@RequestMapping (value = Domain.RNGap.RNProfilePicture.RN_PP_PLACEMENT_NUMBER_GROUP, method = RequestMethod.POST)
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

        String router = Domain.RNGap.RNProfilePicture.RN_PP_PLACEMENT_NUMBER_GROUP;
        SubmitRequestType type = SubmitRequestType.placement_number_group;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            if (request != null && request.getPlacementNumber () != null && request.getPlacementNumber ().size () > 0)
            {
                ID idGroup = request.getIdGroup ();
                if (idGroup.isValid ())
                {
                    ManageGroup manageGroup = new ManageGroup (service , idGroup , mainAccount , AccessLevel.change_placement_number);
                    if (manageGroup.canManage ())
                    {
                        CheckPlacementNumber checkPlacementNumber = new CheckPlacementNumber (profilePicturesService , mainAccount , router , type , manageGroup.getManager ().getGroup ().getId () , CheckPlacementNumber.TypeId.group , res , req , request.getPlacementNumber ());
                        if (checkPlacementNumber.isOk ())
                            answerToClient = (new UpdatePlacementNumber (checkPlacementNumber.getAnswer ().getProfilePictures () , request.getPlacementNumber () , profilePicturesService , mainAccount , router , type , res , req , request)).getAnswerToClient ();
                        else answerToClient = checkPlacementNumber.getAnswer ().getAnswerToClient ();
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


    private enum ValAnswer
    {
        id_group_invalid, updated
    }

}
