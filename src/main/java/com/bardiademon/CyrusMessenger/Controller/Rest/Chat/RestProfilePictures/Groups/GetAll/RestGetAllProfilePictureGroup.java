package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Groups.GetAll;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.IsManager;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.IsJoined;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
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
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ALL_GROUP, method = RequestMethod.POST)
public final class RestGetAllProfilePictureGroup
{

    private final UserLoginService userLoginService;
    private final GroupsService groupsService;
    private final JoinGroupService joinGroupService;
    private final GroupManagementService groupManagementService;

    @Autowired
    public RestGetAllProfilePictureGroup
            (UserLoginService _UserLoginService ,
             GroupsService _GroupsService , JoinGroupService _JoinGroupService , GroupManagementService _GroupManagementService)
    {
        this.userLoginService = _UserLoginService;
        this.groupsService = _GroupsService;
        this.joinGroupService = _JoinGroupService;
        this.groupManagementService = _GroupManagementService;
    }

    @RequestMapping (value = {"" , "/" , "/{ID_GROUP}"})
    public AnswerToClient getAll
            (HttpServletRequest req , HttpServletResponse res ,
             @CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             @PathVariable (value = "ID_GROUP") String id)
    {

        AnswerToClient answerToClient;

        String request = ToJson.CreateClass.n ("id_group" , id).toJson ();
        String router = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ALL_GROUP;
        SubmitRequestType type = SubmitRequestType.get_all_profile_pictures_group;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            ID idGroup = new ID (id);
            if (idGroup.isValid ())
            {
                ILUGroup iluGroup = new ILUGroup (groupsService);
                iluGroup.setId (idGroup.getId ());
                if (iluGroup.isValid ())
                {
                    assert iluGroup.getGroup () != null;
                    List<ProfilePictures> profilePictures = (iluGroup.getGroup ()).getProfilePictures ();

                    if (profilePictures != null && profilePictures.size () > 0)
                    {
                        IsJoined isJoined = new IsJoined (joinGroupService , mainAccount , idGroup);

                        List<Long> profilePicturesIds = (new SortProfilePictures (profilePictures)).getIds ();
                        assert profilePicturesIds != null;

                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());

                        if (isJoined.is ())
                            answerToClient.put (AnswerToClient.CUK.ids.name () , profilePicturesIds);
                        else
                        {
                            IsManager isManager = new IsManager (mainAccount , groupManagementService);
                            isManager.setILUGroup (iluGroup);
                            if (isManager.isManager ())
                                answerToClient.put (AnswerToClient.CUK.ids.name () , profilePicturesIds);
                            else
                                answerToClient.put (AnswerToClient.CUK.id.name () , profilePicturesIds.get (0));
                        }

                        answerToClient.setReqRes (req , res);
                        l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , null);
                        r.n (mainAccount , type , false);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.not_found.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null);
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.group_not_found.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.group_not_found.name ()) , null);
                    r.n (mainAccount , type , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.IdInvalid ();
                answerToClient.setReqRes (req , res);
                l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("id invalid") , null);
                r.n (mainAccount , type , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        group_not_found
    }

}
