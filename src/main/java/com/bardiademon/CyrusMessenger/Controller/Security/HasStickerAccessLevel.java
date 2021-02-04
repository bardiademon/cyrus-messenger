package com.bardiademon.CyrusMessenger.Controller.Security;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelType;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.IsJoined;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import javax.servlet.http.HttpServletResponse;

public final class HasStickerAccessLevel
{
    private final StickerAccessLevelService service;

    public HasStickerAccessLevel (StickerAccessLevelService Service)
    {
        this.service = Service;
    }

    // group => id or groupname
    public AnswerToClient hasAccess (StickerGroups stickerGroups , MainAccount mainAccount , Object group , StickerAccessLevelType type)
    {
        if (type.equals (StickerAccessLevelType.user) && stickerGroups.getAddedBy ().getId () == mainAccount.getId ())
            return null;

        switch (type)
        {
            case user:
                if (service.hasAccessUser (stickerGroups.getId () , mainAccount.getId ())) return null;
                else return AnswerToClient.AccessDenied ();
            case group:
                return hasAccessGroup (stickerGroups , group , mainAccount);
            default:
                return null;
        }
    }

    // group => id or groupname
    private AnswerToClient hasAccessGroup (StickerGroups stickerGroups , Object groupnameOrGroupId , MainAccount mainAccount)
    {
        ILUGroup iluGroup = new ILUGroup ();
        iluGroup.setGroupnameOrId (groupnameOrGroupId);
        if (iluGroup.isValid ())
        {
            Groups group = iluGroup.getGroup ();
            assert group != null;
            ID groupId = new ID (group.getId ());

            if (service.hasAccessGroup (stickerGroups.getId () , groupId.getId ()))
            {
                if (group.getOwner ().getId () == mainAccount.getId () || new IsJoined (mainAccount , groupId).is ())
                    return null;
                else
                    return AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) , ValAnswer.you_are_not_a_member_of_this_group.name ());
            }
            else return AnswerToClient.AccessDenied ();
        }
        else return AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_group.name ());
    }

    private enum ValAnswer
    {
        invalid_group, you_are_not_a_member_of_this_group
    }

    public StickerAccessLevelService getService ()
    {
        return service;
    }
}
