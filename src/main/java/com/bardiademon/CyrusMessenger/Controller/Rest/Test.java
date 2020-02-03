package com.bardiademon.CyrusMessenger.Controller.Rest;

import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedByTheSystemService;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequest;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlocked;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContacts;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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


    @RequestMapping (value = {"/" , ""})
    public boolean test ()
    {

        List<NumberOfSubmitRequest> list = new ArrayList<> ();


        list.add (create (10 , 3 , SubmitRequestType.register));
        list.add (create (10 , 3 , SubmitRequestType.login));
        list.add (create (15 , 3 , SubmitRequestType.new_email));
        list.add (create (15 , 3 , SubmitRequestType.confirmed_phone));
        list.add (create (10 , 3 , SubmitRequestType.is_valid_uep));

        numberOfSubmitRequestService.Repository.saveAll (list);

        return false;


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
