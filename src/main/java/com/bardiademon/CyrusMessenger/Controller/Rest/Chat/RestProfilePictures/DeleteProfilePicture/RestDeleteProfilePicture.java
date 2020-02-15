package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.DeleteProfilePicture;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.DeleteProfilePicture.RequestDeleteProfilePicture.Which;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePicturesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
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
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE, method = RequestMethod.POST)
public final class RestDeleteProfilePicture
{

    private ProfilePicturesService profilePicturesService;
    private HttpServletResponse res;
    private HttpServletRequest req;
    private RequestDeleteProfilePicture request;

    @Autowired
    public RestDeleteProfilePicture (ProfilePicturesService _ProfilePicturesService)
    {
        this.profilePicturesService = _ProfilePicturesService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient delete
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req , @RequestBody RequestDeleteProfilePicture request)
    {
        this.res = res;
        this.req = req;
        this.request = request;
        AnswerToClient answerToClient;
        CheckLogin checkLogin = new CheckLogin (codeLogin);
        if (checkLogin.isValid ())
        {
            MainAccount mainAccountRequested = checkLogin.getVCodeLogin ().getMainAccount ();
            if (request != null)
            {
                Which which = Which.to (request.getWhich ());
                if (which != null)
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.ServerError () , ValAnswer.failed_delete.name ());
                    answerToClient.setReqRes (req , res);
                    switch (which)
                    {

                        case del_one:
                            answerToClient = deleteOne (mainAccountRequested);
                            break;
                        case del_all:
                            if (deleteAll (mainAccountRequested))
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.all_pic_deleted.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.all_pic_deleted.name ());
                            }
                            else
                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.all_pic_deleted.name ()) , ValAnswer.failed_delete.name ());
                            break;
                        case del_placement_number_zero:
                            if (deletePlacementNumberZero (mainAccountRequested))
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.deleted_placement_number_zero.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.deleted_placement_number_zero.name ());
                            }
                            else
                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.deleted_placement_number_zero.name ()) , ValAnswer.failed_delete.name ());
                            break;
                        case del_main:
                            if (deleteMainPic (mainAccountRequested))
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.deleted_main_pic.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.deleted_main_pic.name ());
                            }
                            else
                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.deleted_main_pic.name ()) , ValAnswer.failed_delete.name ());
                        case del_placement_number_not_zero:
                            if (deletePlacementNumberNotZero (mainAccountRequested))
                            {
                                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.deleted_placement_number_not_zero.name ());
                                answerToClient.setReqRes (req , res);
                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.deleted_placement_number_not_zero.name ());
                            }
                            else
                                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.deleted_main_pic.name ()) , ValAnswer.failed_delete.name ());


                            break;
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.which_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (null , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.which_invalid.name ()) , null);
                }
            }
            else
            {
                answerToClient = AnswerToClient.RequestIsNull ();
                answerToClient.setReqRes (req , res);
                l.n (null , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("request id null") , null);
            }
        }
        else
        {
            answerToClient = checkLogin.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("not login") , ToJson.CreateClass.SCLogin (codeLogin));
        }

        return answerToClient;
    }

    private AnswerToClient deleteOne (MainAccount mainAccountRequested)
    {
        AnswerToClient answerToClient;
        if (request.getIdProfilePicture () > 0)
        {
            ProfilePictures profilePicture
                    = profilePicturesService.getOneForUser (request.getIdProfilePicture () , mainAccountRequested.getId ());
            if (profilePicture != null)
            {
                if (profilePicture.isMainPic () && !request.isDeleteMainPic ())
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.this_profile_picture_is_main_pic.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.this_profile_picture_is_main_pic.name ()) , null);
                    return answerToClient;
                }

                profilePicture.setDeleted (true);
                profilePicture.setDeletedAt (LocalDateTime.now ());
                profilePicturesService.Repository.save (profilePicture);

                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.one_was_deleted.name ());
                answerToClient.put (AnswerToClient.CUK.id.name () , request.getIdProfilePicture ());
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , null , ToJson.CreateClass.n ("id" , request.getIdProfilePicture ()).toJson ());
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_not_found.name ());
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_not_found.name ()) , ToJson.CreateClass.n ("id" , request.getIdProfilePicture ()).toJson ());
            }
        }
        else
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_invalid.name ());
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_invalid.name ()) , ToJson.CreateClass.n ("id" , request.getIdProfilePicture ()).toJson ());
        }
        return answerToClient;
    }

    private boolean deleteAll (MainAccount mainAccountRequested)
    {
        try
        {
            List<ProfilePictures> profilePictures = mainAccountRequested.getProfilePictures ();
            for (int i = 0; i < profilePictures.size (); i++)
            {
                ProfilePictures profilePicture = profilePictures.get (i);

                if (profilePicture.isMainPic () && !request.isDeleteMainPic ()) continue;

                profilePicture.setDeleted (true);
                profilePicture.setDeletedAt (LocalDateTime.now ());
                profilePictures.set (i , profilePicture);
            }
            profilePicturesService.Repository.saveAll (profilePictures);
            return true;
        }
        catch (Exception e)
        {
            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , null , Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.n ("id" , request.getIdProfilePicture ()).toJson ());
            return false;
        }

    }

    private boolean deletePlacementNumberZero (MainAccount mainAccountRequested)
    {
        try
        {
            return (profilePicturesService.deletePlacementNumberZero (mainAccountRequested.getId () , request.isDeleteMainPic ()) > 0);
        }
        catch (Exception e)
        {
            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , null , Thread.currentThread ().getStackTrace () , e , null);
            return false;
        }
    }

    private boolean deletePlacementNumberNotZero (MainAccount mainAccountRequested)
    {
        try
        {
            return (profilePicturesService.deletePlacementNumberNotZero (mainAccountRequested.getId () , request.isDeleteMainPic ()) > 0);
        }
        catch (Exception e)
        {
            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , null , Thread.currentThread ().getStackTrace () , e , null);
            return false;
        }
    }

    private boolean deleteMainPic (MainAccount mainAccountRequested)
    {
        try
        {
            return (profilePicturesService.deleteMainPic (mainAccountRequested.getId ()) > 0);
        }
        catch (Exception e)
        {
            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURE_DELETE , mainAccountRequested , null , Thread.currentThread ().getStackTrace () , e , null);
            return false;
        }
    }

    private enum ValAnswer
    {
        which_invalid, id_invalid, id_not_found,
        one_was_deleted, all_pic_deleted,
        failed_delete, deleted_placement_number_zero, deleted_placement_number_not_zero, deleted_main_pic, this_profile_picture_is_main_pic
    }

}
