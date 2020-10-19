package com.bardiademon.CyrusMessenger.Controller.Security;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelType;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups.StickerGroups;

public final class HasStickerAccessLevel
{
    private final StickerAccessLevelService service;

    public HasStickerAccessLevel (StickerAccessLevelService Service)
    {
        this.service = Service;
    }

    public boolean hasAccess (StickerGroups stickerGroups , long userId , StickerAccessLevelType type)
    {
        if (stickerGroups.getAddedBy ().getId () == userId) return true;

        switch (type)
        {
            case user:
                return service.hasAccessUser (stickerGroups.getId () , userId);
            case group:
                service.hasAccessGroup (stickerGroups.getId () , userId);
            default:
                return false;
        }
    }


    public StickerAccessLevelService getService ()
    {
        return service;
    }
}
