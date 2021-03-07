package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Users.Delete;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles.UploadedFiles;
import com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles.UploadedFilesService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNGap.RNProfilePicture.RN_PROFILE_PICTURES_DELETE_USER, method = RequestMethod.POST)
public final class RestDeleteProfilePictureUser
{

    private final UserLoginService userLoginService;
    private final ProfilePicturesService profilePicturesService;
    private final UploadedFilesService uploadedFilesService;

    @Autowired
    public RestDeleteProfilePictureUser
            (UserLoginService _UserLoginService ,
             ProfilePicturesService _ProfilePicturesService ,
             UploadedFilesService _UploadedFilesService)
    {
        this.userLoginService = _UserLoginService;
        this.profilePicturesService = _ProfilePicturesService;
        this.uploadedFilesService = _UploadedFilesService;
    }

    @RequestMapping (value = { "" , "/" , "/{ID_PROFILE_PICTURE}" })
    public AnswerToClient delete
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @PathVariable (value = "ID_PROFILE_PICTURE", required = false) String id)
    {
        AnswerToClient answerToClient;
        String request = ToJson.CreateClass.n ("ID_PROFILE_PICTURE" , id).toJson ();
        String router = Domain.RNGap.RNProfilePicture.RN_PROFILE_PICTURES_DELETE_USER;
        SubmitRequestType submitType = SubmitRequestType.delete_profile_picture_user;

        CBSIL both = CBSIL.Both (request , req , res , codeLogin , userLoginService , router , submitType);
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();
            ID idProfilePicture = new ID (id);
            if (idProfilePicture.isValid ())
            {
                ProfilePictures profilePictures = profilePicturesService.getOneForUser (idProfilePicture.getId () , mainAccount.getId ());
                if (profilePictures != null)
                {
                    UploadedFiles image = profilePictures.getImage ();
                    image.setDeleted (true);
                    image.setDeletedAt (Time.now ());
                    uploadedFilesService.Repository.save (image);

                    profilePictures.setDeleted (true);
                    profilePictures.setDeletedAt (Time.now ());
                    profilePicturesService.Repository.save (profilePictures);

                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.deleted);
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.deleted.name ());
                    r.n (mainAccount , submitType , false);
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.not_found);
                    answerToClient.setReqRes (req , res);
                    l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.not_found));
                    r.n (mainAccount , submitType , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.IdInvalid ();
                answerToClient.setReqRes (req , res);
                l.n (request , router , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , l.e ("id invalid"));
                r.n (mainAccount , submitType , true);
            }
        }
        else answerToClient = both.getAnswerToClient ();

        return answerToClient;
    }

    private enum ValAnswer
    {
        deleted
    }

}
