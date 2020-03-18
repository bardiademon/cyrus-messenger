package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.CreateGroup;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Cookie.MCookie;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Controller.Security.Login.IsLogin;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile.GroupSecurityProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile.GroupSecurityProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.LinkForJoin.LinkForJoin;
import com.bardiademon.CyrusMessenger.Model.Database.LinkForJoin.LinkForJoinService;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernameFor;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.Usernames;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping (value = Domain.RNChat.RNGroups.RN_CREATE_GROUP, method = RequestMethod.POST)
public final class RestCreateGroup
{

    private final GroupsService groupsService;
    private final LinkForJoinService linkForJoinService;
    private final GroupSecurityProfileService groupSecurityProfileService;
    private final UsernamesService usernamesService;

    @Autowired
    public RestCreateGroup
            (GroupsService _GroupsService , LinkForJoinService _LinkForJoinService ,
             GroupSecurityProfileService _GroupSecurityProfileService , UsernamesService _UsernamesService)
    {
        this.groupsService = _GroupsService;
        this.linkForJoinService = _LinkForJoinService;
        this.groupSecurityProfileService = _GroupSecurityProfileService;
        this.usernamesService = _UsernamesService;
    }

    @RequestMapping (value = {"" , "/"})
    public AnswerToClient create
            (@CookieValue (value = MCookie.KEY_CODE_LOGIN_COOKIE, defaultValue = "") String codeLogin ,
             HttpServletResponse res , HttpServletRequest req ,
             @RequestBody RequestCreateGroup request)
    {
        AnswerToClient answerToClient;
        CheckBlockSystem checkBlockSystem = new CheckBlockSystem (req , BlockedFor.submit_request , SubmitRequestType.create_group.name ());
        if (!checkBlockSystem.isBlocked ())
        {
            IsLogin isLogin = new IsLogin (codeLogin);
            if (isLogin.isValid ())
            {
                MainAccount mainAccount = isLogin.getVCodeLogin ().getMainAccount ();
                checkBlockSystem = new CheckBlockSystem (mainAccount.getId () , BlockedFor.submit_request , SubmitRequestType.create_group.name ());
                if (!checkBlockSystem.isBlocked ())
                {
                    if (!groupsService.moreThanLimit (mainAccount.getId ()))
                    {
                        if ((answerToClient = checkRequest (request , mainAccount , req , res)) == null)
                            answerToClient = createGroup (request , mainAccount , req , res);
                    }
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.you_have_made_too_many_groups.name ());
                        answerToClient.put (KeyAnswer.construction_limit.name () , Groups.MAX_CREATE_GROUP);
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("limit create group") , null);
                        r.n (mainAccount , SubmitRequestType.create_group , true);
                    }
                }
                else
                {
                    answerToClient = checkBlockSystem.getAnswerToClient ();
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block for system") , null);
                }
            }
            else
            {
                answerToClient = isLogin.getAnswerToClient ();
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("not login") , null);
            }
        }
        else
        {
            answerToClient = checkBlockSystem.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block for system") , null);
        }

        return answerToClient;
    }

    private AnswerToClient checkRequest (RequestCreateGroup request , MainAccount mainAccount , HttpServletRequest req , HttpServletResponse res)
    {
        AnswerToClient answerToClient = null;

        if (Str.IsEmpty (request.getName ()))
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.name_empty.name ());
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.name_empty.name ()) , null);
            r.n (mainAccount , SubmitRequestType.create_group , true);
        }
        if (answerToClient == null && Str.IsEmpty (request.getUsername ()) && Str.IsEmpty (request.getCreateLinkJoin ()))
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_and_link_join_empty.name ());
            answerToClient.setReqRes (req , res);
            l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_and_link_join_empty.name ()) , null);
            r.n (mainAccount , SubmitRequestType.create_group , true);
        }
        if (answerToClient == null && !Str.IsEmpty (request.getUsername ()))
        {
            Usernames forGroup = usernamesService.findForGroup (request.getUsername ());
            if (forGroup != null)
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_is_exists.name ());
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_is_exists.name ()) , null);
                r.n (mainAccount , SubmitRequestType.create_group , true);
            }
            else
            {
                VUsername vUsername = new VUsername (request.getUsername ());
                if (!vUsername.check ())
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_invalid.name ()) , null);
                    r.n (mainAccount , SubmitRequestType.create_group , true);
                }
            }
        }
        if (answerToClient == null && !Str.IsEmpty (request.getCreateLinkJoin ()))
        {
            try
            {
                String createLinkJoin = request.getCreateLinkJoin ();
                if (!createLinkJoin.equals ("true") && !createLinkJoin.equals ("false"))
                    throw new Exception (ValAnswer.create_link_join_invalid.name ());
            }
            catch (Exception e)
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , e.getMessage ());
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , null , answerToClient , Thread.currentThread ().getStackTrace () , e , null);
                r.n (mainAccount , SubmitRequestType.create_group , true);
            }
        }
        return answerToClient;
    }

    private AnswerToClient createGroup (RequestCreateGroup request , MainAccount mainAccount , HttpServletRequest req , HttpServletResponse res)
    {
        AnswerToClient answerToClient = null;
        Groups groups = new Groups ();
        groups.setBio (request.getBio ());
        groups.setName (request.getName ());
        groups.setOwner (mainAccount);

        Usernames usernames = null;

        if (!Str.IsEmpty (request.getDescription ())) groups.setDescription (request.getDescription ());
        if (!Str.IsEmpty (request.getUsername ()))
        {
            usernames = new Usernames ();
            usernames.setUsernameFor (UsernameFor.group);
            usernames.setUsername (request.getUsername ());
            usernames = usernamesService.Repository.save (usernames);

            groups.setUsername (usernames);
        }

        boolean createCode = false;

        LinkForJoin linkForJoin = null;
        if (request.getCreateLinkJoin ().equals ("true"))
        {

            linkForJoin = linkForJoinService.create (LinkForJoin.LinkFor.group);

            if (linkForJoin == null)
            {
                answerToClient = AnswerToClient.ServerError ();
                answerToClient.setReqRes (req , res);
                l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("server error") , "error for class LinkForJoin");
            }
            else createCode = true;
        }

        if (answerToClient == null)
        {
            if (createCode) groups.setLinkForJoin (linkForJoin);

            Groups groupSave = groupsService.Repository.save (groups);

            if (usernames != null)
            {
                usernames.setGroups (groups);
                usernamesService.Repository.save (usernames);
            }

            if (createCode)
            {
                linkForJoin.setGroups (groupSave);
                linkForJoinService.Repository.save (linkForJoin);
            }


            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.created.name ());
            if (!Str.IsEmpty (request.getUsername ()))
                answerToClient.put (KeyAnswer.username.name () , groups.getUsername ().getUsername ());

            if (createCode) answerToClient.put (KeyAnswer.link.name () , linkForJoin.getLink ());

            answerToClient.put (AnswerToClient.CUK.id.name () , groupSave.getId ());

            GroupSecurityProfile groupSecurityProfile = new GroupSecurityProfile ();
            groupSecurityProfile.setFamilyGroup (request.isFamilyGroup ());
            groupSecurityProfile.setGroups (groupSave);

            groupSecurityProfile = groupSecurityProfileService.Repository.save (groupSecurityProfile);

            groupSave.setGroupSecurityProfile (groupSecurityProfile);

            groupsService.Repository.save (groupSave);


            l.n (ToJson.To (request) , Domain.RNChat.RNGroups.RN_CREATE_GROUP , mainAccount , answerToClient , Thread.currentThread ().getStackTrace () , null , ValAnswer.created.name ());
            r.n (mainAccount , SubmitRequestType.create_group , false);
        }
        return answerToClient;

    }


    private enum ValAnswer
    {
        name_empty, username_and_link_join_empty, create_link_join_invalid, created, username_is_exists, you_have_made_too_many_groups, username_invalid
    }

    private enum KeyAnswer
    {
        username, link, construction_limit
    }

}
