package com.bardiademon.CyrusMessenger.Controller.Rest;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedByTheSystemService;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequest;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsername;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.ProfilePictures.SortProfilePictures;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping (value = "/test")
public class Test
{

    private MainAccountService mainAccountService;
    private ShowProfileForService showProfileForService;
    private SecurityUserProfileService securityUserProfileService;
    private ShowChatForService showChatForService;
    private UserContactsService userContactsService;
    private UserBlockedService userBlockedService;
    private SubmitRequestService submitRequestService;
    private BlockedByTheSystemService blockedByTheSystemService;
    private NumberOfSubmitRequestService numberOfSubmitRequestService;
    private SecurityUserChatService securityUserChatService;

    @Autowired
    public Test
            (MainAccountService mainAccountService ,
             ShowProfileForService showProfileForService ,
             SecurityUserProfileService securityUserProfileService ,
             ShowChatForService showChatForService ,
             UserContactsService userContactsService ,
             UserBlockedService userBlockedService ,
             SubmitRequestService submitRequestService ,
             BlockedByTheSystemService blockedByTheSystemService ,
             NumberOfSubmitRequestService numberOfSubmitRequestService ,
             SecurityUserChatService securityUserChatService
            )
    {

        this.mainAccountService = mainAccountService;
        this.showProfileForService = showProfileForService;
        this.securityUserProfileService = securityUserProfileService;
        this.showChatForService = showChatForService;
        this.userContactsService = userContactsService;
        this.userBlockedService = userBlockedService;
        this.submitRequestService = submitRequestService;
        this.blockedByTheSystemService = blockedByTheSystemService;
        this.numberOfSubmitRequestService = numberOfSubmitRequestService;
        this.securityUserChatService = securityUserChatService;
    }

    @RequestMapping (value = {"" , "/" , "/{username}" , "/{idUser}" , "/{idUser}/{username}"})
    public AnswerToClient test (HttpServletRequest req , HttpServletResponse res , @PathVariable (value = "idUser", required = false) long idUser , @PathVariable (value = "username", required = false) String username)
    {
        IdUsername idUsername = new IdUsername (mainAccountService , idUser , username);

        AnswerToClient answerToClient = idUsername.getAnswerToClient ();

        if (answerToClient == null) answerToClient = AnswerToClient.OK ();
        answerToClient.setResponse (res);
        answerToClient.setRequest (req);

        System.out.println (idUsername.isValid ());

        String request = ToJson.To (new ToJson.CreateClass ().put ("idUser" , idUser).put ("username" , username));

        l.n (request , "/test" , null , idUsername.getAnswerToClient () , Thread.currentThread ().getStackTrace () , null , null);

        List<ProfilePictures> profilePictures = idUsername.getMainAccount ().getProfilePictures ();

        ProfilePictures profilePicture = profilePictures.get (0);

        String picture = Path.StickTogether (Path.PROFILE_PICTURES_USERS , idUsername.getMainAccount ().getUsername () , profilePicture.getName () + "." + profilePicture.getType ());


        answerToClient.put ("pic" , picture);

        SortProfilePictures sort = new SortProfilePictures (profilePictures);



        return answerToClient;

    }

    public NumberOfSubmitRequest create (int block , int request , SubmitRequestType type)
    {
        NumberOfSubmitRequest numberOfSubmitRequest = new NumberOfSubmitRequest ();
        numberOfSubmitRequest.setNumberOfMinToBeBlocked (block);
        numberOfSubmitRequest.setNumberOfRequest (request);
        numberOfSubmitRequest.setType (type);
        return numberOfSubmitRequest;
    }

    public enum TestEnum
    {
        test_enum
    }

}
