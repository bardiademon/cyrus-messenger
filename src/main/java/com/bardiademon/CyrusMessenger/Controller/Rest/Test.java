package com.bardiademon.CyrusMessenger.Controller.Rest;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedByTheSystemService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup.FiredFromGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup.FiredFromGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequest;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.IdEnTy;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping (value = "/test")
public class Test
{

    private MainAccountService mainAccountService;
    private SecurityUserProfileService securityUserProfileService;
    private ShowChatForService showChatForService;
    private UserContactsService userContactsService;
    private UserBlockedService userBlockedService;
    private SubmitRequestService submitRequestService;
    private BlockedByTheSystemService blockedByTheSystemService;
    private NumberOfSubmitRequestService numberOfSubmitRequestService;
    private SecurityUserChatService securityUserChatService;
    private FiredFromGroupService firedFromGroupService;
    private GroupManagementService groupManagementService;
    private GroupsService groupsService;
    private UserSeparateProfilesService userSeparateProfilesService;

    @Autowired
    public Test
            (MainAccountService mainAccountService ,
             SecurityUserProfileService securityUserProfileService ,
             ShowChatForService showChatForService ,
             UserContactsService userContactsService ,
             UserBlockedService userBlockedService ,
             SubmitRequestService submitRequestService ,
             BlockedByTheSystemService blockedByTheSystemService ,
             NumberOfSubmitRequestService numberOfSubmitRequestService ,
             SecurityUserChatService securityUserChatService ,
             FiredFromGroupService firedFromGroupService ,
             GroupManagementService groupManagementService ,
             GroupsService groupsService ,
             UserSeparateProfilesService userSeparateProfilesService
            )
    {

        this.mainAccountService = mainAccountService;
        this.securityUserProfileService = securityUserProfileService;
        this.showChatForService = showChatForService;
        this.userContactsService = userContactsService;
        this.userBlockedService = userBlockedService;
        this.submitRequestService = submitRequestService;
        this.blockedByTheSystemService = blockedByTheSystemService;
        this.numberOfSubmitRequestService = numberOfSubmitRequestService;
        this.securityUserChatService = securityUserChatService;
        this.firedFromGroupService = firedFromGroupService;
        this.groupManagementService = groupManagementService;
        this.groupsService = groupsService;
        this.userSeparateProfilesService = userSeparateProfilesService;
    }

    @RequestMapping (value = { "" , "/" , "/{username}/{username2}" })
    public AnswerToClient test (HttpServletRequest req , HttpServletResponse res , @PathVariable (value = "username", required = false) String username , @PathVariable (value = "username2", required = false) String username2)
    {
        AnswerToClient answerToClient;
        IdUsernameMainAccount account = new IdUsernameMainAccount (mainAccountService , 0 , username);
        if (account.isValid ())
        {

            Groups groups = groupsService.hasLink ("FN1bOSNzfu3Qd13O9II8");

            if (groups != null)
            {
                IdUsernameMainAccount account2 = new IdUsernameMainAccount (mainAccountService , 0 , username2);
                if (account2.isValid ())
                {
                    if (account2.getMainAccount ().getId () == account.getMainAccount ().getId ())
                    {
                        answerToClient = AnswerToClient.ServerError ();
                    }
                    else
                    {
                        FiredFromGroup firedFromGroup = new FiredFromGroup ();
                        firedFromGroup.setFiredAt (LocalDateTime.now ());
                        firedFromGroup.setGroup (groups);
                        firedFromGroup.setValidityTime (LocalDateTime.now ().plusMinutes (50));
                        firedFromGroup.setMainAccount (account2.getMainAccount ());
                        firedFromGroup.setWhy ("this is a test");

                        firedFromGroupService.Repository.save (firedFromGroup);

                        answerToClient = AnswerToClient.OK ();
                    }
                }
                else answerToClient = account.getAnswerToClient ();

            }
            else answerToClient = AnswerToClient.ServerError ();

        }
        else answerToClient = account.getAnswerToClient ();

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


    @RequestMapping (value = "/test-find-sep-prof/{id_user}")
    public List <IdEnTy> testFindUserSeparateProfiles (@PathVariable (value = "id_user") int id)
    {
        return userSeparateProfilesService.findIdType (id);
    }

    public enum TestEnum
    {
        test_enum
    }

}
