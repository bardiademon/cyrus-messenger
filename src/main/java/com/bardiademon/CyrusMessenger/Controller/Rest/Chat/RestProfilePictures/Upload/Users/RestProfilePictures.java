package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Upload.Users;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Upload.RequestUploadProfilePictures;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Upload.Users.AccessUploadProfilePicture.Service;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain.RNChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicFor;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.Default.DSize;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.GetSize;
import com.bardiademon.CyrusMessenger.bardiademon.Hash256;
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping (value = RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD, method = RequestMethod.POST)
public final class RestProfilePictures
{

    private String name, type;
    private long size;

    private AnswerToClient answerToClient;

    private HttpServletRequest request;
    private HttpServletResponse response;

    private final Service service;

    private MainAccount mainAccount;

    private ProfilePictures newProfilePicture = null;

    private ProfilePictures profilePictures = null;

    @Autowired
    public RestProfilePictures (UserLoginService _UserLoginService , SecurityUserProfileService _SecurityUserProfileService ,
                                ProfilePicturesService _ProfilePicturesService)
    {
        service = new Service (_UserLoginService , _SecurityUserProfileService , _ProfilePicturesService);
    }

    private void setNull ()
    {
        newProfilePicture = null;
        profilePictures = null;
        request = null;
        response = null;
        name = null;
        type = null;
        size = 0;
        System.gc ();
    }

    @RequestMapping (value = {"" , "/"}, method = RequestMethod.POST)
    public AnswerToClient upload
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @ModelAttribute RequestUploadProfilePictures request)
    {
        setNull ();
        answerToClient = null;
        this.request = req;
        this.response = res;

        if (request != null)
        {
            boolean okProfilePicture = true;
            if (request.getId () > 0)
            {
                profilePictures = service.getProfilePicturesService ().Repository.findByIdAndDeletedFalse (request.getId ());
                okProfilePicture = (profilePictures != null);

                if (!okProfilePicture)
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_not_found.name ());
                    l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_not_found.name ()) , null);
                }
                else
                {
                    if (request.getPic () != null)
                    {
                        profilePictures.setDeleted (true);
                        profilePictures.setDeletedAt (LocalDateTime.now ());
                        service.getProfilePicturesService ().Repository.save (profilePictures);

                        ToJson.CreateClass createClass = new ToJson.CreateClass ();
                        createClass.put ("id" , profilePictures.getId ());
                        createClass.put ("the_operation" , "delete_profile_picture");
                        l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , null , Thread.currentThread ().getStackTrace () , null , createClass.toJson ());

                    }
                }
            }

            if (okProfilePicture)
            {
                if (profilePictures != null)
                {
                    if (request.getPic () != null)
                    {
                        if (request.getPlacementNumber () < 0)
                            request.setPlacementNumber (profilePictures.getPlacementNumber ());

                        if (!request.isUpdateMainPic ())
                            request.setMainPic (profilePictures.isMainPic ());
                    }
                }

                AccessUploadProfilePicture accessUpload = new AccessUploadProfilePicture (service , codeLogin , (profilePictures == null));
                if (accessUpload.hasAccess ())
                {
                    mainAccount = accessUpload.getIsLogin ().getVCodeLogin ().getMainAccount ();
                    Object dirName = mainAccount.getUsername ();

                    if ((profilePictures != null && request.getPic () == null) || upload (request , dirName))
                    {
                        if (setInDb (request , accessUpload.getIsLogin ().getVCodeLogin ().getMainAccount ()))
                        {
                            if (profilePictures == null)
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.uploaded.name ());
                                answerToClient.put (AnswerToClient.CUK.id.name () , this.newProfilePicture.getId ());
                                answerToClient.setReqRes (this.request , this.response);
                                r.n (req.getRemoteAddr () , SubmitRequestType.upload_cover , false);
                            }
                            else
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.replaced.name ());
                                answerToClient.put (KeyAnswer.id.name () , this.newProfilePicture.getId ());
                                answerToClient.setReqRes (this.request , this.response);
                                l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.replaced.name ());
                            }
                            r.n (req.getRemoteAddr () , SubmitRequestType.upload_cover , false);
                        }
                        else
                        {
                            answerToClient = AnswerToClient.ServerError ();
                            answerToClient.setReqRes (this.request , this.response);
                            l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("Server error") , null);
                        }
                    }
                }
                else
                {
                    answerToClient = accessUpload.getAnswerToClient ();
                    answerToClient.setReqRes (this.request , this.response);
                    l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("Error AccessUpload") , null);
                    r.n (req.getRemoteAddr () , SubmitRequestType.upload_cover , true);
                }
            }
        }
        else
        {
            r.n (req.getRemoteAddr () , SubmitRequestType.upload_cover , true);
            answerToClient = AnswerToClient.RequestIsNull ();
        }

        answerToClient.setResponse (res);
        setNull ();
        return answerToClient;
    }

    private boolean upload (RequestUploadProfilePictures request , Object dirName)
    {
        MultipartFile pic = request.getPic ();
        try
        {
            if (pic == null || pic.getBytes ().length == 0)
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.pic_is_empty.name ());
                answerToClient.setReqRes (this.request , this.response);
                l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.pic_is_empty.name ()) , null);
                return false;
            }
        }
        catch (IOException e)
        {
            answerToClient = AnswerToClient.ServerError ();
            answerToClient.setReqRes (this.request , this.response);
            l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , e , null);
            return false;
        }

        CheckImage checkImage = new CheckImage ();
        if (checkImage.valid (pic))
        {
            size = pic.getSize ();
            if (pic.getSize () > DSize.SIZE_COVER)
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.size_is_large.name ());
                answerToClient.put (KeyAnswer.the_size_of_your_image.name () , GetSize.Get (size));
                answerToClient.put (KeyAnswer.allowed_image_site.name () , GetSize.Get (DSize.SIZE_COVER));
                answerToClient.put (KeyAnswer.extra_size.name () , GetSize.Get ((size - DSize.SIZE_COVER)));
                answerToClient.setReqRes (this.request , this.response);

                l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.size_is_large.name ()) , null);

                return false;
            }
            else
            {
                String pathUpload = Path.PROFILE_PICTURES_USERS;

                pathUpload = Path.StickTogether (pathUpload , String.valueOf (dirName));

                try
                {
                    byte[] picBytes = pic.getBytes ();

                    type = checkImage.getExtension ();
                    Hash256 hash256 = new Hash256 ();
                    String hash = hash256.hash ((FilenameUtils.getBaseName (pic.getOriginalFilename ()) + Timestamp.valueOf (LocalDateTime.now ()).getTime ()) , (type + Timestamp.valueOf (LocalDateTime.now ()).getTime ()));
                    name = hash256.hash (Code.Name () , hash);
                    String nameFileForSave = String.format ("%s.%s" , name , type);

                    File newFile = new File (Path.StickTogether (pathUpload , nameFileForSave));

                    File dir = newFile.getParentFile ();
                    if (!dir.exists () && !dir.mkdirs ()) throw new IOException ("Cannot create dirs");
                    if (!newFile.createNewFile ()) throw new IOException ("Cannot create dirs or files");

                    FileOutputStream outputStream = new FileOutputStream (newFile);
                    outputStream.write (picBytes);

                    l.n (ToJson.To (request) ,
                            RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD ,
                            mainAccount , null , Thread.currentThread ().getStackTrace () , null , (new ToJson.CreateClass ()).put ("uploaded" , (newFile.exists ())).toJson ());

                    return (newFile.exists ());
                }
                catch (IOException e)
                {
                    answerToClient = AnswerToClient.ServerError ();
                    answerToClient.setReqRes (this.request , this.response);
                    l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , e , null);
                    return false;
                }

            }
        }
        else
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.pic_invalid.name ());
            l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.pic_invalid.name ()) , null);
            return false;
        }
    }

    private boolean setInDb (RequestUploadProfilePictures request , MainAccount mainAccount)
    {
        try
        {
            if (request.getPic () == null)
            {
                if (request.isUpdateMainPic ()) profilePictures.setMainPic (request.isMainPic ());
                if (request.getPlacementNumber () >= 0)
                    profilePictures.setPlacementNumber (request.getPlacementNumber ());

                newProfilePicture = service.getProfilePicturesService ().Repository.save (profilePictures);

                return true;
            }
            else
            {
                newProfilePicture = new ProfilePictures ();
                newProfilePicture.setMainAccount (mainAccount);
                newProfilePicture.setName (name);
                newProfilePicture.setType (type);
                newProfilePicture.setSize (size);

                if (request.isMainPic ()) service.getProfilePicturesService ().disableMainPhoto (mainAccount.getId ());

                newProfilePicture.setMainPic (request.isMainPic ());

                if (profilePictures == null && request.getPlacementNumber () < 0) request.setPlacementNumber (0);
                if (request.getPlacementNumber () >= 0)
                    newProfilePicture.setPlacementNumber (request.getPlacementNumber ());

                newProfilePicture.setThisPicFor (ProfilePicFor.user);
                newProfilePicture = service.getProfilePicturesService ().Repository.save (newProfilePicture);
                return (newProfilePicture.getId () > 0);
            }
        }
        catch (Exception e)
        {
            l.n (ToJson.To (request) , RNChat.RNProfilePicture.RN_PROFILE_PICTURES_UPLOAD , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , e , null);
            return false;
        }
    }

    private enum ValAnswer
    {
        size_is_large, uploaded, pic_is_empty, pic_invalid, id_not_found, replaced
    }

    private enum KeyAnswer
    {
        the_size_of_your_image, allowed_image_site, extra_size, id
    }
}
