package com.bardiademon.CyrusMessenger.Controller.Rest;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserGapAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedByTheSystemService;
import com.bardiademon.CyrusMessenger.Model.Database.Default.Default;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultService;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultType;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsTextService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.QuestionText.AnswerQuestionsText.AnswerQuestionsTextService.AnswersCountOptions;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.Stickers;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickersService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup.FiredFromGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup.FiredFromGroupService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupManagement.GroupManagement.GroupManagementService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequest;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap.SecurityUserGapService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor.ShowChatForService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked.UserBlockedService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts.UserContactsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.IdEnTy;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles.UserSeparateProfilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.This;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping (value = "/test")
public class Test
{

    private final DefaultService defaultService;
    private final MainAccountService mainAccountService;
    private final SecurityUserProfileService securityUserProfileService;
    private final ShowChatForService showChatForService;
    private final UserContactsService userContactsService;
    private final UserBlockedService userBlockedService;
    private final SubmitRequestService submitRequestService;
    private final BlockedByTheSystemService blockedByTheSystemService;
    private final NumberOfSubmitRequestService numberOfSubmitRequestService;
    private final SecurityUserGapService securityUserGapService;
    private final FiredFromGroupService firedFromGroupService;
    private final GroupManagementService groupManagementService;
    private final GroupsService groupsService;
    private final UserSeparateProfilesService userSeparateProfilesService;
    private final EnumTypesService enumTypesService;
    private final StickersService stickersService;

    @Autowired
    public Test
            (
                    DefaultService defaultService ,
                    MainAccountService mainAccountService ,
                    SecurityUserProfileService securityUserProfileService ,
                    ShowChatForService showChatForService ,
                    UserContactsService userContactsService ,
                    UserBlockedService userBlockedService ,
                    SubmitRequestService submitRequestService ,
                    BlockedByTheSystemService blockedByTheSystemService ,
                    NumberOfSubmitRequestService numberOfSubmitRequestService ,
                    SecurityUserGapService securityUserGapService ,
                    FiredFromGroupService firedFromGroupService ,
                    GroupManagementService groupManagementService ,
                    GroupsService groupsService ,
                    UserSeparateProfilesService userSeparateProfilesService ,
                    EnumTypesService enumTypesService , StickersService stickersService
            )
    {
        this.defaultService = defaultService;
        this.mainAccountService = mainAccountService;
        this.securityUserProfileService = securityUserProfileService;
        this.showChatForService = showChatForService;
        this.userContactsService = userContactsService;
        this.userBlockedService = userBlockedService;
        this.submitRequestService = submitRequestService;
        this.blockedByTheSystemService = blockedByTheSystemService;
        this.numberOfSubmitRequestService = numberOfSubmitRequestService;
        this.securityUserGapService = securityUserGapService;
        this.firedFromGroupService = firedFromGroupService;
        this.groupManagementService = groupManagementService;
        this.groupsService = groupsService;
        this.userSeparateProfilesService = userSeparateProfilesService;
        this.enumTypesService = enumTypesService;
        this.stickersService = stickersService;
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

    @RequestMapping (value = { "/get-default" , "/get-default/{key}" })
    public String getDefault (@PathVariable (value = "key", required = false) String key)
    {
        if (key != null && !key.isEmpty ()) return defaultService.getString (DefaultKey.valueOf (key));
        else return null;
    }

    @RequestMapping (value =
            {
                    "/set-default" ,
                    "/set-default/{key}" ,
                    "/set-default/{key}/{value}" ,
                    "/set-default/{key}/{value}/{type}"
            })
    public Default setDefault
            (@PathVariable (value = "key", required = false) String key ,
             @PathVariable (value = "value", required = false) String value ,
             @PathVariable (value = "type", required = false) String type)
    {
        if (key != null && !key.isEmpty () && value != null && !value.isEmpty () && type != null && !type.isEmpty ())
        {
            DefaultType defaultType = DefaultType.to (type);
            DefaultKey defaultKey = DefaultKey.to (key);

            if (defaultType != null && defaultKey != null)
            {
                Default def = defaultService.getDefault (defaultKey);
                if (def == null)
                {
                    def = new Default ();
                    def.setKey (defaultKey);
                }

                def.setValue (value);
                def.setTypeValue (defaultType);

                return defaultService.Repository.save (def);


            }
        }
        return null;
    }

    @RequestMapping (value = "/sec-user-gap")
    public String testSecUserGap ()
    {
        MainAccount applicant = mainAccountService.byUsername ("bardiademon");
        MainAccount user = mainAccountService.byUsername ("system");

        UserGapAccessLevel accessLevel = new UserGapAccessLevel (applicant , user);

        return String.valueOf (accessLevel.hasAccess (Which.s_message));

    }

    @RequestMapping (value = "/test-answer-question-text-options")
    public String textAnswerQuestionTextOptions ()
    {
        final List <AnswersCountOptions> objects = This.GetService (AnswerQuestionsTextService.class).countQuestionTextOptions (10760);

        final StringBuilder result = new StringBuilder ();
        if (objects != null)
            for (AnswersCountOptions answersCountOptions : objects) result.append (answersCountOptions.toString ());

        return result.toString ();
    }

    @RequestMapping (value = "get-sticker/{id-sticker}")
    public Stickers getSticker (@PathVariable ("id-sticker") String strIdSticker)
    {
        return stickersService.Repository.getSticker (Long.parseLong (strIdSticker));
    }

    @RequestMapping (value = "/test-id")
    public TestId testId (@RequestBody TestId testId)
    {
        return testId;
    }

    public static class TestId
    {
        private long id;

        @JsonProperty("my_name")
        private String name;

        @JsonIgnore
        private String family;

        public TestId ()
        {
        }

        public long getId ()
        {
            return id;
        }

        public void setId (long id)
        {
            this.id = id;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getFamily ()
        {
            return family;
        }

        public void setFamily (String family)
        {
            this.family = family;
        }
    }



    public enum TestEnum
    {
        test_enum
    }

}
