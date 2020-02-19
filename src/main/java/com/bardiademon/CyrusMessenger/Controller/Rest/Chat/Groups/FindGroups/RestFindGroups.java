package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.Groups.FindGroups;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Domain;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.CheckBlockSystem;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile.GroupSecurityProfile;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityProfile.GroupSecurityProfileService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.GroupsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.JoinGroup;
import com.bardiademon.CyrusMessenger.Model.Database.LinkForJoin.LinkForJoin;
import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.GetOneProfilePicture;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping (value = Domain.RNChat.RNGroups.RN_FIND_GROUPS, method = RequestMethod.POST)
public final class RestFindGroups
{

    private final GroupsService groupsService;
    private GroupSecurityProfileService groupSecurityProfileService;

    public RestFindGroups (GroupsService _GroupsService , GroupSecurityProfileService _GroupSecurityProfileService)
    {
        this.groupsService = _GroupsService;
        this.groupSecurityProfileService = _GroupSecurityProfileService;
    }

    @RequestMapping (value = {"/u" , "/u/{username}"})
    public AnswerToClient findByUsername (HttpServletResponse res , HttpServletRequest req , @PathVariable (value = "username", required = false) String username)
    {
        AnswerToClient answerToClient;
        CheckBlockSystem checkBlockSystem = new CheckBlockSystem (req , BlockedFor.submit_request , SubmitRequestType.find_groups.name ());
        if (!checkBlockSystem.isBlocked ())
        {
            if (!Str.IsEmpty (username))
            {
                VUsername vUsername = new VUsername (username);
                if (vUsername.check ())
                    answerToClient = getInfoGroup (req , res , groupsService.hasUsername (username) , ValAnswer.username.name () , username);
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.CreateClass.n ("username" , username).toJson () , Domain.RNChat.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.username_invalid.name ()) , null);
                    r.n (req.getRemoteAddr () , SubmitRequestType.create_group , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.username_is_empty.name ());
                answerToClient.setReqRes (req , res);
                l.n (ToJson.CreateClass.n ("username" , username).toJson () , Domain.RNChat.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.link_is_empty.name ()) , null);
                r.n (req.getRemoteAddr () , SubmitRequestType.create_group , true);
            }
        }
        else
        {
            answerToClient = checkBlockSystem.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.CreateClass.n ("username" , username).toJson () , Domain.RNChat.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.link_is_empty.name ()) , null);
        }
        return answerToClient;
    }

    @RequestMapping (value = {"/l" , "/l/{link}"})
    public AnswerToClient findByLink (HttpServletResponse res , HttpServletRequest req , @PathVariable (value = "link", required = false) String link)
    {
        AnswerToClient answerToClient;
        CheckBlockSystem checkBlockSystem = new CheckBlockSystem (req , BlockedFor.submit_request , SubmitRequestType.find_groups.name ());
        if (!checkBlockSystem.isBlocked ())
        {
            if (!Str.IsEmpty (link))
            {
                if (link.matches ("[0-9A-Za-z]*"))
                    answerToClient = getInfoGroup (req , res , groupsService.hasLink (link) , ValAnswer.link.name () , link);
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.link_invalid.name ());
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.CreateClass.n ("link" , link).toJson () , Domain.RNChat.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.link_invalid.name ()) , null);
                    r.n (req.getRemoteAddr () , SubmitRequestType.create_group , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.link_is_empty.name ());
                answerToClient.setReqRes (req , res);
                l.n (ToJson.CreateClass.n ("link" , link).toJson () , Domain.RNChat.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.link_is_empty.name ()) , null);
            }
        }
        else
        {
            answerToClient = checkBlockSystem.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.CreateClass.n ("link" , link).toJson () , Domain.RNChat.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception ("block by system") , null);
        }
        return answerToClient;
    }

    private AnswerToClient getInfoGroup (HttpServletRequest req , HttpServletResponse res , @Nullable Groups group , String linkUsername , String valLinkUsername)
    {
        AnswerToClient answerToClient;

        GroupSecurityProfile securityProfile;
        if (group == null || (securityProfile = groupSecurityProfileService.getSec (group)).isFamilyGroup () || !securityProfile.isShowInSearch ())
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.not_found.name ());
            answerToClient.setReqRes (req , res);
            l.n (ToJson.CreateClass.n (linkUsername , valLinkUsername).toJson () , Domain.RNChat.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found.name ()) , null);
            r.n (req.getRemoteAddr () , SubmitRequestType.create_group , true);
        }
        else
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.found.name ());
            Map<String, Object> infoGroup = new LinkedHashMap<> ();

            infoGroup.put (AnswerToClient.CUK.id.name () , group.getId ());

            LinkForJoin linkForJoin = group.getLinkForJoin ();
            if (linkForJoin != null) infoGroup.put (ValAnswer.link_join.name () , linkForJoin.getLink ());

            String username = group.getUsername ();
            if (!Str.IsEmpty (username)) infoGroup.put (ValAnswer.username.name () , username);

            String link = group.getLink ();
            if (!Str.IsEmpty (link)) infoGroup.put (ValAnswer.link.name () , link);

            String bio = group.getBio ();
            if (!Str.IsEmpty (bio)) infoGroup.put (ValAnswer.bio.name () , bio);

            GetOneProfilePicture getOneProfilePicture = new GetOneProfilePicture (group.getProfilePictures ());

            if (getOneProfilePicture.wasGet ())
                infoGroup.put (ValAnswer.id_profile_picture.name () , getOneProfilePicture.getProfilePicture ().getId ());

            if (securityProfile.isShowOwner ())
                infoGroup.put (ValAnswer.owner.name () , group.getOwner ().getUsername ());

            List<JoinGroup> joinGroups = group.getJoinGroups ();
            long numberOfMembers = 0;
            if (joinGroups != null) numberOfMembers = joinGroups.size ();

            if (!Str.IsEmpty (bio)) infoGroup.put (ValAnswer.members.name () , numberOfMembers);

            infoGroup.put (ValAnswer.created_at.name () , Time.toString (group.getCreatedAt ()));

            answerToClient.put (ValAnswer.info_group.name () , infoGroup);
            answerToClient.setReqRes (req , res);

            l.n (ToJson.CreateClass.n (linkUsername , valLinkUsername).toJson () , Domain.RNChat.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.found.name ()) , null);
            r.n (req.getRemoteAddr () , SubmitRequestType.create_group , false);
        }

        return answerToClient;
    }

    private enum ValAnswer
    {
        link_is_empty, username_is_empty, username_invalid, link_invalid, not_found, found,

        username, link_join, bio, link, created_at, members, id_profile_picture, owner, info_group
    }

}
