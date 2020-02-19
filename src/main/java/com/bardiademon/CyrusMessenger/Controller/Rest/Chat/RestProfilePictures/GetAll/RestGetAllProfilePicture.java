package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.GetAll;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.CheckLogin;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserLogin.UserLoginService;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.ProfilePictures.SortProfilePictures;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping (value = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ALL, method = RequestMethod.POST)
public final class RestGetAllProfilePicture
{

    private UserLoginService userLoginService;
    private MainAccountService mainAccountService;

    public RestGetAllProfilePicture (UserLoginService _UserLoginService , MainAccountService _MainAccountService)
    {
        this.userLoginService = _UserLoginService;
        this.mainAccountService = _MainAccountService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient getAll
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req , @RequestBody RequestGetAllProfilePicture request)
    {
        CheckLogin checkLogin = new CheckLogin (codeLogin , userLoginService.Repository);
        AnswerToClient answerToClient;
        if (checkLogin.isValid ())
        {
            IdUsernameMainAccount idUsernameMainAccount = new IdUsernameMainAccount (mainAccountService , request.getId () , request.getUsername ());
            if (idUsernameMainAccount.isValid ())
            {
                MainAccount mainAccountRequested = checkLogin.getVCodeLogin ().getMainAccount ();
                CheckUserAccessLevel checkUserAccessLevel = new CheckUserAccessLevel (mainAccountRequested , idUsernameMainAccount.getMainAccount () , mainAccountService);

                ToJson.CreateClass createClass = new ToJson.CreateClass ();
                createClass.put ("id_user_get_cover" , idUsernameMainAccount.getIdUser ());

                if (checkUserAccessLevel.hasAccessProfile (CheckUserAccessLevel.CheckProfile.cover))
                {
                    List<ProfilePictures> profilePictures = (new SortProfilePictures (idUsernameMainAccount.getMainAccount ().getProfilePictures ())).getNewProfilePictures ();
                    List<Long> linkProfilePicture = null;
                    if (profilePictures != null && profilePictures.size () > 0)
                    {
                        linkProfilePicture = new ArrayList<> ();
                        for (ProfilePictures profilePicture : profilePictures)
                            linkProfilePicture.add (profilePicture.getId ());
                    }

                    createClass.put ("found" , (linkProfilePicture != null));

                    if (linkProfilePicture == null)
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.not_found.name ());
                    else
                    {
                        String rnProfilePictureGetOne = Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ONE;
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , KeyAnswer.id_profile_pictures.name () , linkProfilePicture);
                        answerToClient.put (KeyAnswer.link.name () , String.format ("%s/%s" , Domain.MAIN_DOMAIN , rnProfilePictureGetOne));
                    }

                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ALL , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , null , createClass.toJson ());
                }
                else
                {
                    answerToClient = AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED);
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ALL , mainAccountRequested , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("Status code:" + HttpServletResponse.SC_UNAUTHORIZED) , createClass.toJson ());
                }
            }
            else
            {
                answerToClient = idUsernameMainAccount.getAnswerToClient ();
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ALL , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("Error from class IdUsername") , null);
            }
        }
        else
        {
            answerToClient = checkLogin.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , Domain.RNChat.RNProfilePicture.RN_PROFILE_PICTURES_GET_ALL , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("not login") , null);
        }
        return answerToClient;
    }

    private enum ValAnswer
    {
        not_found
    }

    private enum KeyAnswer
    {
        id_profile_pictures, link
    }

}
