package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Groups.Upload;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.CheckRequestUploadProfilePicture;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles.UploadedFiles;
import com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles.UploadedFilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.AccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.HasAccessManage.ManageGroup;
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
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
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
@RequestMapping (value = Domain.RNGap.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP, method = RequestMethod.POST)
public final class RestUploadProfilePictureGroup
{

    private final UserLoginService userLoginService;
    private final ProfilePicturesService profilePicturesService;
    private final UploadedFilesService uploadedFilesService;
    private final ManageGroup.Service service;

    @Autowired
    public RestUploadProfilePictureGroup
            (UserLoginService _UserLoginService ,
             MainAccountService _MainAccountService ,
             GroupsService _GroupsService ,
             GroupManagementService _GroupManagementService ,
             ProfilePicturesService _ProfilePicturesService ,
             UploadedFilesService _UploadedFilesService)
    {
        this.userLoginService = _UserLoginService;
        this.profilePicturesService = _ProfilePicturesService;
        this.uploadedFilesService = _UploadedFilesService;
        this.service = new ManageGroup.Service (_MainAccountService , _GroupsService , _GroupManagementService);
    }

    @RequestMapping (value = { "" , "/" })
    public AnswerToClient upload
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @ModelAttribute RequestUploadProfilePictureGroup request)
    {
        AnswerToClient answerToClient;
        String router = Domain.RNGap.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_GROUP;
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
                    ManageGroup manageGroup = new ManageGroup (service , idGroup , mainAccount , AccessLevel.upload_picture);
                    if (manageGroup.canManage ())
                    {
                        Groups group = manageGroup.getManager ().getGroup ();

                        int maxUploadProfilePicture
                                = group.getGroupSecurityProfile ().getMaxUploadProfilePicture ();
                        int countUploadPicGroup = profilePicturesService.countUploadPicGroup (group.getId ());

                        if (countUploadPicGroup < maxUploadProfilePicture)
                        {
                            boolean ok = false;

                            if (request.isMain ())
                            {
                                ManageGroup manageGroupSetMainPic = new ManageGroup (service , idGroup , mainAccount , AccessLevel.set_main_picture);
                                if (manageGroupSetMainPic.canManage ()) ok = true;
                                else answerToClient = manageGroupSetMainPic.getAnswerToClient ();
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
                                        ManageGroup manageGroupDelete = new ManageGroup (service , idGroup , mainAccount , AccessLevel.del_picture);
                                        if (manageGroupDelete.canManage ())
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
                                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.id_profile_picture_not_found);
                                                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.id_profile_picture_not_found));
                                                r.n (mainAccount , type , false);
                                            }
                                        }
                                        else answerToClient = manageGroupDelete.getAnswerToClient ();
                                    }
                                    else
                                    {
                                        answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_profile_picture_invalid);
                                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.id_profile_picture_invalid));
                                        r.n (mainAccount , type , false);
                                    }

                                }
                                else ok = true;

                                if (ok)
                                {
                                    answerToClient = upload (res , req , request , router , checkRequest.getCheckImage () , mainAccount , group , oldProfilePicture);
                                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.uploaded.name ());
                                    r.n (mainAccount , type , false);
                                }
                            }
                        }
                        else
                        {
                            answerToClient = AnswerToClient.IdInvalid (ValAnswer.upload_limit_completed);
                            answerToClient.put (KeyAnswer.limit , maxUploadProfilePicture);
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.upload_limit_completed));
                            r.n (mainAccount , type , true);
                        }
                    }
                    else
                    {
                        answerToClient = manageGroup.getAnswerToClient ();
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e ("error from class CanManageGroup"));
                        r.n (mainAccount , type , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid ();
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e ("id group invalid"));
                    r.n (mainAccount , type , true);
                }
            }


        }
        else answerToClient = both.getAnswerToClient ();


        return answerToClient;
    }

    private AnswerToClient upload
            (HttpServletResponse res , HttpServletRequest req ,
             RequestUploadProfilePictureGroup request , String router , CheckImage checkImage , MainAccount mainAccount , Groups groups , ProfilePictures oldProfilePicture)
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

                    UploadedFiles image = new UploadedFiles ();
                    image.setName (name);
                    image.setType (type);
                    image.setSize (picture.getSize ());
                    image.setWidth (checkImage.getWidth ());
                    image.setHeight (checkImage.getHeight ());
                    image.setFileFor (RestUploadProfilePictureGroup.class.getName ());
                    image.setSavedPath (pathUploadPicture.getParent ());
                    image.setUploadedBy (mainAccount);
                    image = uploadedFilesService.Repository.save (image);

                    ProfilePictures profilePictures = new ProfilePictures ();
                    profilePictures.setGroups (groups);
                    profilePictures.setThisPicFor (ProfilePicFor.group);
                    profilePictures.setImage (image);

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
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.updated);
                    else
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.uploaded);
                }
                else throw new Exception ("Cannot upload, path=" + path);
            }
            else throw new Exception ("Cannot mkdirs, path=" + path);
        }
        catch (Exception e)
        {
            answerToClient = AnswerToClient.ServerError ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , e);
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
