package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Groups.Delete;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.ManageGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
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
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNGap.RNProfilePicture.RN_PROFILE_PICTURES_DELETE_GROUP, method = RequestMethod.POST)
public final class RestDeleteProfilePictureGroup
{

    private final ProfilePicturesService profilePicturesService;
    private final UserLoginService userLoginService;
    private final ManageGroup.Service service;

    @Autowired
    public RestDeleteProfilePictureGroup
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             GroupsService _GroupsService , GroupManagementService _GroupManagementService , ProfilePicturesService _ProfilePicturesService)
    {
        this.userLoginService = _UserLoginService;
        this.profilePicturesService = _ProfilePicturesService;
        this.service = new ManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient delete
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @RequestBody RequestDeleteProfilePictureGroup request)
    {

        AnswerToClient answerToClient;

        String router = Domain.RNGap.RNProfilePicture.RN_PROFILE_PICTURES_DELETE_GROUP;
        SubmitRequestType type = SubmitRequestType.delete_profile_picture_group;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            ID idGroup = request.getIdGroup ();
            if (idGroup.isValid ())
            {
                ManageGroup manageGroup = new ManageGroup (service , idGroup , mainAccount , AccessLevel.del_picture);
                if (manageGroup.canManage ())
                {
                    ID idProfilePicture = request.getIdProfilePicture ();
                    if (idProfilePicture.isValid ())
                    {
                        ProfilePictures profilePictures = profilePicturesService.getOneGroup (idProfilePicture.getId () , idGroup.getId ());
                        if (profilePictures != null)
                        {
                            profilePictures.setDeleted (true);
                            profilePictures.setDeletedAt (LocalDateTime.now ());
                            profilePicturesService.Repository.save (profilePictures);

                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.deleted.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.deleted.name ()) , null);
                            r.n (mainAccount , type , false);
                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.id_profile_picture_not_found.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_profile_picture_not_found.name ()) , null);
                            r.n (mainAccount , type , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_profile_picture_invalid.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_profile_picture_invalid.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = manageGroup.getAnswerToClient ();
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ManageGroup.class.getName ()) , null);
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
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        id_group_invalid, id_profile_picture_invalid, id_profile_picture_not_found, deleted
    }
}
