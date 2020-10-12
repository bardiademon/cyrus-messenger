package com.bardiademon.CyrusMessenger.Controller.Security;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelType;

public final class HasStickerAccessLevel
{
    private final StickerAccessLevelService service;

    public HasStickerAccessLevel (StickerAccessLevelService Service)
    {
        this.service = Service;
    }

    public boolean hasAccess (long stickerGroupId , long userId , StickerAccessLevelType type)
    {
        switch (type)
        {
            case user:
                return service.hasAccessUser (stickerGroupId , userId);
            case group:
                service.hasAccessGroup (stickerGroupId , userId);
            default:
                return false;
        }
    }


    public StickerAccessLevelService getService ()
    {
        return service;
    }
}
