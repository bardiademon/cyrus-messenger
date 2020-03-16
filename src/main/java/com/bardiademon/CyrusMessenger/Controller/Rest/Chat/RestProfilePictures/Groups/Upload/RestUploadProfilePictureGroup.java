package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Groups.Upload;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.CheckRequestUploadProfilePicture;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.CanManageGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicFor;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.apache.commons.io.FilenameUtils;
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
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP, method = RequestMethod.POST)
public final class RestUploadProfilePictureGroup
{

    private final UserLoginService userLoginService;
    private final ProfilePicturesService profilePicturesService;
    private final CanManageGroup.Service service;

    @Autowired
    public RestUploadProfilePictureGroup
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             GroupsService _GroupsService ,
             GroupManagementService _GroupManagementService ,
             ProfilePicturesService _ProfilePicturesService)
    {
        this.userLoginService = _UserLoginService;
        this.profilePicturesService = _ProfilePicturesService;
        this.service = new CanManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient upload
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @ModelAttribute RequestUploadProfilePictureGroup request)
    {
        AnswerToClient answerToClient;
        String router = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP;
        SubmitRequestType type = SubmitRequestType.upload_profile_picture_group;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , type);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            CheckRequestUploadProfilePicture checkRequest = new CheckRequestUploadProfilePicture (request , mainAccount , res , req , router , type);
            if ((answerToClient = checkRequest.getAnswerToClient ()) == null)
            {
                ID idGroup;
                if ((idGroup = request.getIdGroup ()).isValid ())
                {
                    CanManageGroup canManageGroup = new CanManageGroup (service , idGroup , mainAccount , AccessLevel.upload_picture);
                    if (canManageGroup.canManage ())
                    {
                        Groups group = canManageGroup.getManager ().getGroup ();

                        int maxUploadProfilePicture
                                = group.getGroupSecurityProfile ().getMaxUploadProfilePicture ();
                        int countUploadPicGroup = profilePicturesService.countUploadPicGroup (group.getId ());

                        if (countUploadPicGroup < maxUploadProfilePicture)
                        {
                            boolean ok = false;

                            if (request.isMain ())
                            {
                                CanManageGroup canManageGroupSetMainPic = new CanManageGroup (service , idGroup , mainAccount , AccessLevel.set_main_picture);
                                if (canManageGroupSetMainPic.canManage ()) ok = true;
                                else answerToClient = canManageGroupSetMainPic.getAnswerToClient ();
                            }
                            else ok = true;

                            if (ok)
                            {
                                ok = false;

                                ID idProfilePicture = request.getIdProfilePicture ();

                                ProfilePictures oldProfilePicture = null;

                                if (request.isReplace ())
                                {
                                    if (idProfilePicture != null && idProfilePicture.isValid ())
                                    {
                                        CanManageGroup canManageGroupDelete = new CanManageGroup (service , idGroup , mainAccount , AccessLevel.del_picture);
                                        if (canManageGroupDelete.canManage ())
                                        {
                                            oldProfilePicture = profilePicturesService.getOneGroup (idProfilePicture.getId () , idGroup.getId ());
                                            if (oldProfilePicture != null)
                                            {
                                                oldProfilePicture.setDeleted (true);
                                                oldProfilePicture.setDeletedAt (LocalDateTime.now ());
                                                profilePicturesService.Repository.save (oldProfilePicture);
                                                ok = true;
                                            }
                                            else
                                            {
                                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_profile_picture_not_found.name ());
                                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_profile_picture_not_found.name ()) , null);
                                                r.n (mainAccount , type , false);
                                            }
                                        }
                                        else answerToClient = canManageGroupDelete.getAnswerToClient ();
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_profile_picture_invalid.name ());
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_profile_picture_invalid.name ()) , null);
                                        r.n (mainAccount , type , false);
                                    }

                                }
                                else ok = true;

                                if (ok)
                                {
                                    answerToClient = upload (res , req , request , router , mainAccount , group , oldProfilePicture);
                                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.uploaded.name ());
                                    r.n (mainAccount , type , false);
                                }
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.IdInvalid (ValAnswer.upload_limit_completed.name ());
                            answerToClient.put (KeyAnswer.limit.name () , maxUploadProfilePicture);
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.upload_limit_completed.name ()) , null);
                            r.n (mainAccount , type , true);
                        }
                    }
                    else
                    {
                        answerToClient = canManageGroup.getAnswerToClient ();
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("error from class CanManageGroup") , null);
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("id group invalid") , null);
                    r.n (mainAccount , type , true);
                }
            }


        }
        else answerToClient = both.getAnswerToClient ();


        return answerToClient;
    }

    private AnswerToClient upload
            (HttpServletResponse res , HttpServletRequest req ,
             RequestUploadProfilePictureGroup request , String router , MainAccount mainAccount , Groups groups , ProfilePictures oldProfilePicture)
    {
        AnswerToClient answerToClient;
        try
        {
            MultipartFile picture = request.getPicture ();

            String type = FilenameUtils.getExtension (picture.getOriginalFilename ());

            Hash256 hash256 = new Hash256 ();

            String hash = hash256.hash ((FilenameUtils.getBaseName (request.getPicture ().getOriginalFilename ()) + Timestamp.valueOf (LocalDateTime.now ()).getTime ()) , (type + Timestamp.valueOf (LocalDateTime.now ()).getTime ()));
            String name = hash256.hash (Code.Name () , hash);

            String path = Path.StickTogether (Path.PROFILE_PICTURES_GROUPS , String.valueOf (groups.getId ()) , String.format ("%s.%s" , name , type));

            File pathUploadPicture = new File (path);

            File parentFile = pathUploadPicture.getParentFile ();
            if (parentFile.exists () || parentFile.mkdirs ())
            {

                Files.write (pathUploadPicture.toPath () , picture.getBytes ());

                if (pathUploadPicture.exists ())
                {
                    if (request.isMain ())
                        profilePicturesService.disableMainPhotoGroup (groups.getId ());

                    ProfilePictures profilePictures = new ProfilePictures ();
                    profilePictures.setGroups (groups);
                    profilePictures.setThisPicFor (ProfilePicFor.group);
                    profilePictures.setName (name);
                    profilePictures.setType (type);
                    profilePictures.setSize (picture.getSize ());

                    if (request.getPlacement_number () > -1)
                        profilePictures.setPlacementNumber (request.getPlacement_number ());
                    else
                    {
                        if (request.isReplace ())
                            profilePictures.setPlacementNumber (oldProfilePicture.getPlacementNumber ());
                        else profilePictures.setPlacementNumber (0);
                    }

                    if (!request.isReplace ()) profilePictures.setMainPic (request.isMain ());
                    else
                    {
                        if (request.isUpdate_main ()) profilePictures.setMainPic (request.isMain ());
                        else profilePictures.setMainPic (oldProfilePicture.isMainPic ());
                    }

                    profilePictures.setMainAccount (mainAccount);

                    profilePicturesService.Repository.save (profilePictures);
                    if (request.isReplace ())
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.updated.name ());
                    else
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.uploaded.name ());
                }
                else throw new Exception ("Cannot upload, path=" + path);
            }
            else throw new Exception ("Cannot mkdirs, path=" + path);
        }
        catch (Exception e)
        {
            answerToClient = AnswerToClient.ServerError ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , e , null);
        }

        return answerToClient;
    }

    public enum ValAnswer
    {
        uploaded, updated, id_profile_picture_not_found, id_profile_picture_invalid, upload_limit_completed
    }

    private enum KeyAnswer
    {
        limit
    }
}
