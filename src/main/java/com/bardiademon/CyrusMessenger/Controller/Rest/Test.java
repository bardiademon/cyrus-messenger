package com.bardiademon.CyrusMessenger.Controller.Rest;

import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = "/test", method = RequestMethod.POST)
public class Test
{

    private MainAccountService mainAccountService;
    private ShowProfileForService showProfileForService;
    private SecurityUserProfileService securityUserProfileService;
    private ShowChatForService showChatForService;
    private SecurityUserChatService securityUserChatService;

    @Autowired
    public Test
            (MainAccountService mainAccountService ,
             ShowProfileForService showProfileForService ,
             SecurityUserProfileService securityUserProfileService,
             ShowChatForService showChatForService,
             SecurityUserChatService securityUserChatService
            )
    {

        this.mainAccountService = mainAccountService;
        this.showProfileForService = showProfileForService;
        this.securityUserProfileService = securityUserProfileService;
        this.showChatForService = showChatForService;
        this.securityUserChatService = securityUserChatService;
    }

    @RequestMapping (value = {"/" , ""}, method = RequestMethod.POST)
    public boolean test ()
    {
        CheckUserAccessLevel checkUserAccessLevel =
                new CheckUserAccessLevel ("bardiademon" , "bardia_demon" , mainAccountService);

        checkUserAccessLevel.setServiceSecurityUserChat (securityUserChatService);
        checkUserAccessLevel.setServiceShowChatFor (showChatForService);
        checkUserAccessLevel.setCheckChat (CheckUserAccessLevel.CheckChat.send_file);


        return checkUserAccessLevel.check (checkUserAccessLevel.CHK_CHAT);
    }

}
