package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Groups.FindGroups;

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
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.Usernames;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.CyrusMap;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.r;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (value = Domain.RNGap.RNGroups.RN_FIND_GROUPS, method = RequestMethod.POST)
public final class RestFindGroups
{

    private final GroupsService groupsService;
    private final GroupSecurityProfileService groupSecurityProfileService;
    private final UsernamesService usernamesService;

    @Autowired
    public RestFindGroups (GroupsService _GroupsService , GroupSecurityProfileService _GroupSecurityProfileService , UsernamesService _UsernamesService)
    {
        this.groupsService = _GroupsService;
        this.groupSecurityProfileService = _GroupSecurityProfileService;
        this.usernamesService = _UsernamesService;
    }

    @RequestMapping (value = { "/u" , "/u/{username}" })
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
                {
                    Usernames forGroup = usernamesService.findForGroup (username);
                    if (forGroup != null)
                        answerToClient = getInfoGroup (req , res , forGroup.getGroups () , ValAnswer.groupname.name () , username);
                    else
                    {
                        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found);
                        answerToClient.setReqRes (req , res);
                        l.n (ToJson.CreateClass.n ("username" , username).toJson () , Domain.RNGap.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.not_found));
                        r.n (req.getRemoteAddr () , SubmitRequestType.find_groups , true);
                    }
                }
                else
                {
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.username_invalid);
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.CreateClass.n ("username" , username).toJson () , Domain.RNGap.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.username_invalid));
                    r.n (req.getRemoteAddr () , SubmitRequestType.find_groups , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.username_is_empty);
                answerToClient.setReqRes (req , res);
                l.n (ToJson.CreateClass.n ("username" , username).toJson () , Domain.RNGap.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.link_is_empty));
                r.n (req.getRemoteAddr () , SubmitRequestType.find_groups , true);
            }
        }
        else
        {
            answerToClient = checkBlockSystem.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.CreateClass.n ("username" , username).toJson () , Domain.RNGap.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.link_is_empty));
        }
        return answerToClient;
    }

    @RequestMapping (value = { "/l" , "/l/{link}" })
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
                    answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.link_invalid);
                    answerToClient.setReqRes (req , res);
                    l.n (ToJson.CreateClass.n ("link" , link).toJson () , Domain.RNGap.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.link_invalid));
                    r.n (req.getRemoteAddr () , SubmitRequestType.find_groups , true);
                }
            }
            else
            {
                answerToClient = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.link_is_empty);
                answerToClient.setReqRes (req , res);
                l.n (ToJson.CreateClass.n ("link" , link).toJson () , Domain.RNGap.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.link_is_empty));
            }
        }
        else
        {
            answerToClient = checkBlockSystem.getAnswerToClient ();
            answerToClient.setReqRes (req , res);
            l.n (ToJson.CreateClass.n ("link" , link).toJson () , Domain.RNGap.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e ("block by system"));
        }
        return answerToClient;
    }

    @NotNull
    public final AnswerToClient getInfoGroup (HttpServletRequest req , HttpServletResponse res , @Nullable Groups group , String linkUsername , String valLinkUsername)
    {
        AnswerToClient answerToClient;

        GroupSecurityProfile securityProfile;
        if (group == null || (securityProfile = groupSecurityProfileService.getSec (group)).isFamilyGroup () || !securityProfile.isShowInSearch ())
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.not_found);
            answerToClient.setReqRes (req , res);
            l.n (ToJson.CreateClass.n (linkUsername , valLinkUsername).toJson () , Domain.RNGap.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.not_found));
            r.n (req.getRemoteAddr () , SubmitRequestType.find_groups , true);
        }
        else
        {
            answerToClient = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.found);
            CyrusMap <String, Object> infoGroup = new CyrusMap <> ();

            infoGroup.put (AnswerToClient.CUK.id , group.getId ());

            LinkForJoin linkForJoin = group.getLinkForJoin ();
            if (linkForJoin != null) infoGroup.put (ValAnswer.link_join.name () , linkForJoin.getLink ());

            Usernames username = group.getGroupname ();
            if (username != null && !Str.IsEmpty (username.getUsername ()))
                infoGroup.put (ValAnswer.groupname.name () , username.getUsername ());

            String link = group.getLink ();
            if (!Str.IsEmpty (link)) infoGroup.put (ValAnswer.link , link);

            String bio = group.getBio ();
            if (!Str.IsEmpty (bio)) infoGroup.put (ValAnswer.bio , bio);

            GetOneProfilePicture getOneProfilePicture = new GetOneProfilePicture (group.getProfilePictures ());

            if (getOneProfilePicture.wasGet ())
                infoGroup.put (ValAnswer.id_profile_picture , getOneProfilePicture.getProfilePicture ().getId ());

            if (securityProfile.isShowOwner ())
                infoGroup.put (ValAnswer.owner , group.getOwner ().getUsername ().getUsername ());

            if (group.getGroupSecurityProfile ().isShowNumberOfMember ())
            {
                List <JoinGroup> joinGroups = group.getMembers ();
                long numberOfMembers = 0;
                if (joinGroups != null) numberOfMembers = joinGroups.size ();

                if (!Str.IsEmpty (bio)) infoGroup.put (ValAnswer.members , numberOfMembers);
            }

            infoGroup.put (ValAnswer.created_at , Time.toString (group.getCreatedAt ()));
            infoGroup.put (ValAnswer.description , group.getDescription ());

            infoGroup.put (ValAnswer.is_channel , group.isChannel ());

            answerToClient.put (ValAnswer.info_group , infoGroup);
            answerToClient.setReqRes (req , res);

            l.n (ToJson.CreateClass.n (linkUsername , valLinkUsername).toJson () , Domain.RNGap.RNGroups.RN_FIND_GROUPS , null , answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.found));
            r.n (req.getRemoteAddr () , SubmitRequestType.find_groups , false);
        }

        return answerToClient;
    }

    public enum ValAnswer
    {
        link_is_empty, username_is_empty, username_invalid, link_invalid, not_found, found, is_channel,

        groupname, link_join, bio, link, created_at, members, id_profile_picture, owner, info_group, description
    }

}
