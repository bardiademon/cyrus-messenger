package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Users.Upload;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.CheckRequestUploadProfilePicture;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.PathUploadProfilePictures;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicFor;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_USER, method = RequestMethod.POST)
public final class RestUploadProfilePictureUser
{

    private UserLoginService userLoginService;
    private ProfilePicturesService profilePicturesService;
    private SecurityUserProfileService securityUserProfileService;

    @Autowired
    public RestUploadProfilePictureUser
            (UserLoginService _UserLoginService , ProfilePicturesService _ProfilePicturesService ,
             SecurityUserProfileService _SecurityUserProfileService)
    {
        this.userLoginService = _UserLoginService;
        this.profilePicturesService = _ProfilePicturesService;
        this.securityUserProfileService = _SecurityUserProfileService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient upload
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @ModelAttribute RequestUploadProfilePictureUser request)
    {
        AnswerToClient answerToClient;

        String router = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD_USER;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , SubmitRequestType.upload_profile_picture_user);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            CheckRequestUploadProfilePicture checkRequest = new CheckRequestUploadProfilePicture (request , mainAccount , res , req , router , SubmitRequestType.upload_profile_picture_user);
            if ((answerToClient = checkRequest.getAnswerToClient ()) == null)
            {
                if (!request.isReplace () || request.getIdProfilePicture ().isValid ())
                {
                    ProfilePictures profilePictures = null;
                    if (!request.isReplace () || (profilePictures = profilePicturesService.getOneForUser (request.getIdProfilePicture ().getId () , mainAccount.getId ())) != null)
                    {
                        if (request.isReplace ())
                        {
                            assert profilePictures != null;
                            profilePictures.setDeleted (true);
                            profilePictures.setDeletedAt (LocalDateTime.now ());
                            profilePicturesService.Repository.save (profilePictures);
                        }
                        int maxUploadProfilePictures = securityUserProfileService.Repository.getMaxUploadProfilePictures (mainAccount.getId ());
                        int countUploadPicUser = profilePicturesService.countUploadPicUser (mainAccount.getId ());
                        if (countUploadPicUser < maxUploadProfilePictures)
                            answerToClient = upload (request , profilePictures , checkRequest.getCheckImage () , mainAccount , res , req , router);
                        else
                        {
                            answerToClient = AnswerToClient.IdInvalid (ValAnswer.upload_limit_completed.name ());
                            answerToClient.put (KeyAnswer.limit.name () , maxUploadProfilePictures);
                            answerToClient.setReqRes (req , res);
                            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.upload_limit_completed.name ()) , null);
                            r.n (mainAccount , SubmitRequestType.upload_profile_picture_user , true);
                        }
                    }
                    else
                    {
                        answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_not_found.name ());
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_not_found.name ()) , null);
                        r.n (mainAccount , SubmitRequestType.upload_profile_picture_user , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.IdInvalid (ValAnswer.id_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_invalid.name ()) , null);
                    r.n (mainAccount , SubmitRequestType.upload_profile_picture_user , true);
                }
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private AnswerToClient upload (RequestUploadProfilePictureUser request , ProfilePictures profilePictures , CheckImage checkImage , MainAccount mainAccount , HttpServletResponse res , HttpServletRequest req , String router)
    {
        AnswerToClient answerToClient;
        try
        {
            Hash256 hash256 = new Hash256 ();

            String type = checkImage.getExtension ();

            String hash = hash256.hash ((FilenameUtils.getBaseName (request.getPicture ().getOriginalFilename ()) + Timestamp.valueOf (LocalDateTime.now ()).getTime ()) , (type + Timestamp.valueOf (LocalDateTime.now ()).getTime ()));
            String name = hash256.hash (Code.Name () , hash);

            String pathUpload = PathUploadProfilePictures.User (mainAccount.getId () , name , type);

            File file = new File (pathUpload);
            if (!file.exists ())
            {
                FileOutputStream fileOutputStream = new FileOutputStream (file);
                fileOutputStream.write (request.getPicture ().getBytes ());
                fileOutputStream.flush ();
                fileOutputStream.close ();

                if (request.isMain ()) profilePicturesService.disableMainPhotoUser (mainAccount.getId ());

                if (request.getPlacement_number () < 0) request.setPlacement_number (0);

                ProfilePictures newProfilePictures = new ProfilePictures ();
                newProfilePictures.setSize (request.getPicture ().getSize ());
                newProfilePictures.setName (name);
                newProfilePictures.setType (type);
                newProfilePictures.setMainAccount (mainAccount);
                newProfilePictures.setThisPicFor (ProfilePicFor.user);
                newProfilePictures.setPlacementNumber (request.getPlacement_number ());
                newProfilePictures.setMainPic (request.isMain ());

                if (profilePictures != null && !request.isUpdate_main ())
                    newProfilePictures.setMainPic (profilePictures.isMainPic ());

                else newProfilePictures.setMainPic (request.isMain ());

                newProfilePictures = profilePicturesService.Repository.save (newProfilePictures);

                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.uploaded.name ());
                answerToClient.put (AnswerToClient.CUK.id.name () , newProfilePictures.getId ());

                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.uploaded.name ());
                r.n (mainAccount , SubmitRequestType.upload_profile_picture_user , false);
            }
            else throw new Exception ("file_exists");
        }
        catch (Exception e)
        {
            answerToClient = AnswerToClient.ServerError ();
            l.n (ToJson.To (request) , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , e , null);
        }

        return answerToClient;
    }

    private enum ValAnswer
    {
        id_invalid, id_not_found, upload_limit_completed, uploaded
    }

    private enum KeyAnswer
    {
        limit
    }

}
