package com.bardiademon.CyrusMessenger.Controller.Rest;

import com.bardiademon.CyrusMessenger.Controller.Security.CheckUserAccessLevel;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChatService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
             SecurityUserProfileService securityUserProfileService ,
             ShowChatForService showChatForService ,
             SecurityUserChatService securityUserChatService
            )
    {

        this.mainAccountService = mainAccountService;
        this.showProfileForService = showProfileForService;
        this.securityUserProfileService = securityUserProfileService;
        this.showChatForService = showChatForService;
        this.securityUserChatService = securityUserChatService;
    }

    public static class Request
    {
        TestEnum testEnum;

        public TestEnum getTestEnum ()
        {
            return testEnum;
        }

        public void setTestEnum (TestEnum testEnum)
        {
            this.testEnum = testEnum;
        }

        public Request (TestEnum testEnum)
        {
            this.testEnum = testEnum;
        }

        public Request ()
        {
        }
    }

    @RequestMapping (value = {"/" , ""}, method = RequestMethod.POST)
    public String test (@RequestBody Request re)
    {
        return re.testEnum.name ();
    }


    public enum TestEnum
    {
        test_enum
    }

}
