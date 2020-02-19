package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Upload.Groups;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Upload.RequestUploadProfilePictures;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.IsManager;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.ThisManagerHaveAccess;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicFor;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.apache.commons.io.FilenameUtils;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP, method = RequestMethod.POST)
public final class RestUploadProfilePictureGroup
{

    private UserLoginService userLoginService;
    private GroupsService groupsService;
    private GroupManagementService groupManagementService;
    private ProfilePicturesService profilePicturesService;

    @Autowired
    public RestUploadProfilePictureGroup
            (UserLoginService _UserLoginService ,
             GroupsService _GroupsService , GroupManagementService _GroupManagementService , ProfilePicturesService _ProfilePicturesService)
    {
        this.userLoginService = _UserLoginService;
        this.groupsService = _GroupsService;
        this.groupManagementService = _GroupManagementService;
        this.profilePicturesService = _ProfilePicturesService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient upload
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @ModelAttribute RequestUploadProfilePictures request)
    {
        AnswerToClient answerToClient;
        CheckBlockSystem checkBlockSystem = CBSIL.BSubmitRequest (req , SubmitRequestType.upload_profile_picture_group);
        if (!checkBlockSystem.isBlocked ())
        {
            IsLogin isLogin = CBSIL.isLogin (codeLogin , userLoginService);
            if (isLogin.isValid ())
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();
                checkBlockSystem = CBSIL.BSubmitRequest (mainAccount.getId () , SubmitRequestType.upload_profile_picture_group);
                if (!checkBlockSystem.isBlocked ())
                {
                    if (request != null)
                    {
                        if (request.getPic () != null)
                        {
                            if (request.getIdGroup () > 0)
                            {
                                ILUGroup iluGroup = new ILUGroup (groupsService);
                                iluGroup.setId (request.getIdGroup ());
                                if (iluGroup.isValid ())
                                {
                                    IsManager isManager = new IsManager (mainAccount , groupManagementService);
                                    isManager.setILUGroup (iluGroup);
                                    if (isManager.isManager ())
                                    {
                                        if (isManager.isOwner () || isManager.hasAccess (ThisManagerHaveAccess.AccessLevel.upload_picture))
                                        {
                                            if (request.isMainPic () && !isManager.isOwner () && !isManager.hasAccess (ThisManagerHaveAccess.AccessLevel.set_main_picture))
                                            {
                                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_do_not_have_access.name ());
                                                answerToClient.setReqRes (req , res);
                                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_do_not_have_access.name ()) , "set main pic");
                                                r.n (mainAccount , SubmitRequestType.upload_profile_picture_group , true);
                                            }
                                            else
                                            {

                                                int countUpload = profilePicturesService.Repository.countUploadGroup (request.getIdGroup ());
                                                if (countUpload >= isManager.getGroup ().getGroupSecurityProfile ().getMaxUploadProfilePicture ())
                                                {
                                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.photo_upload_limit_is_filled.name ());
                                                    answerToClient.put (AnswerToClient.CUK.limit.name () , isManager.getGroup ().getGroupSecurityProfile ().getMaxMember ());
                                                    answerToClient.setReqRes (req , res);
                                                    l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.photo_upload_limit_is_filled.name ()) , null);
                                                    r.n (mainAccount , SubmitRequestType.upload_profile_picture_group , true);
                                                }
                                                else
                                                    answerToClient = uploadAndInsertDb (res , req , request , isManager);
                                            }
                                        }
                                        else
                                        {
                                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_do_not_have_access.name ());
                                            answerToClient.setReqRes (req , res);
                                            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_do_not_have_access.name ()) , null);
                                            r.n (mainAccount , SubmitRequestType.upload_profile_picture_group , true);
                                        }
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_are_not_the_manager_of_this_group.name ());
                                        answerToClient.setReqRes (req , res);
                                        l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_are_not_the_manager_of_this_group.name ()) , null);
                                        r.n (mainAccount , SubmitRequestType.upload_profile_picture_group , true);
                                    }
                                }
                                else
                                {
                                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.group_not_found.name ());
                                    answerToClient.setReqRes (req , res);
                                    l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.group_not_found.name ()) , null);
                                    r.n (mainAccount , SubmitRequestType.upload_profile_picture_group , true);
                                }
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.id_invalid.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , null);
                                r.n (mainAccount , SubmitRequestType.upload_profile_picture_group , true);
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.pic_is_null.name ());
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.pic_is_null.name ()) , null);
                            r.n (mainAccount , SubmitRequestType.upload_profile_picture_group , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.RequestIsNull ();
                        answerToClient.setReqRes (req , res);
                        l.n (null , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("request is null") , null);
                        r.n (mainAccount , SubmitRequestType.upload_profile_picture_group , true);
                    }
                }
                else
                {
                    answerToClient = checkBlockSystem.getAnswerToClient ();
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block by system") , null);
                }
            }
            else
            {
                answerToClient = isLogin.getAnswerToClient ();
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("not login") , null);
            }
        }
        else
        {
            answerToClient = checkBlockSystem.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block by system") , null);
        }

        return answerToClient;
    }

    private AnswerToClient uploadAndInsertDb (HttpServletResponse res , HttpServletRequest req , RequestUploadProfilePictures request , IsManager isManager)
    {
        AnswerToClient answerToClient;
        try
        {
            MultipartFile pic = request.getPic ();

            CheckImage checkImage = new CheckImage ();
            if (checkImage.valid (pic))
            {
                String type = checkImage.getExtension ();

                Hash256 hash256 = new Hash256 ();
                String hash = hash256.hash ((FilenameUtils.getBaseName (pic.getOriginalFilename ()) + Timestamp.valueOf (LocalDateTime.now ()).getTime ()) , (type + Timestamp.valueOf (LocalDateTime.now ()).getTime ()));

                String name = hash256.hash (Code.Name () , hash);

                ProfilePictures profilePictures = new ProfilePictures ();
                profilePictures.setGroups (isManager.getGroup ());
                if (request.getPlacementNumber () >= 0)
                    profilePictures.setPlacementNumber (request.getPlacementNumber ());
                profilePictures.setMainAccount (isManager.getMainAccount ());
                profilePictures.setMainPic (request.isMainPic ());
                profilePictures.setName (name);
                profilePictures.setType (type);
                profilePictures.setThisPicFor (ProfilePicFor.group);
                profilePictures.setSize (pic.getSize ());

                try
                {
                    boolean upload = upload (pic.getBytes () , name , type , isManager.getGroup ().getId ());
                    if (upload)
                    {
                        profilePictures = profilePicturesService.Repository.save (profilePictures);
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.uploaded.name ());
                        answerToClient.put (AnswerToClient.CUK.id.name () , profilePictures.getId ());
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , isManager.getMainAccount () , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.uploaded.name ());
                        r.n (isManager.getMainAccount () , SubmitRequestType.upload_profile_picture_group , false);
                    }
                    else throw new IOException ();
                }
                catch (IOException e)
                {
                    answerToClient = AnswerToClient.ServerError ();
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , isManager.getMainAccount () , answerToClient , Thread.currentThread ().getStackTrace () , e , null);
                }

            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.this_is_not_image.name ());
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , isManager.getMainAccount () , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_is_not_image.name ()) , null);
            }
        }
        catch (Exception e)
        {
            answerToClient = AnswerToClient.ServerError ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP , isManager.getMainAccount () , answerToClient , Thread.currentThread ().getStackTrace () , e , "Server error");
        }
        return answerToClient;
    }

    private boolean upload (byte[] pic , String name , String type , long idGroup) throws IOException
    {
        String pathUpload = Path.StickTogether (Path.PROFILE_PICTURES_GROUPS , String.valueOf (idGroup) , String.format ("%s.%s" , name , type));

        File file = new File (pathUpload);
        File dir = file.getParentFile ();

        if (!dir.exists () && !dir.mkdirs ()) throw new IOException ("can not create dir => " + dir.getPath ());

        if (!file.createNewFile ()) throw new IOException ("can not create file => " + file.getPath ());

        if (file.exists ())
        {
            FileOutputStream outputStream = new FileOutputStream (file);
            outputStream.write (pic);
            outputStream.flush ();
            outputStream.close ();

            return true;
        }
        else throw new IOException ("after create file is not exists => " + file.getPath ());

    }

    private enum ValAnswer
    {
        pic_is_null, group_not_found, you_are_not_the_manager_of_this_group, you_do_not_have_access, this_is_not_image, uploaded, photo_upload_limit_is_filled
    }
}
