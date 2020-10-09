package com.bardiademon.CyrusMessenger.Controller.Security;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel.StickerAccessLevelService;

public final class HasStickerAccessLevel
{
    private final StickerAccessLevelService service;

    public HasStickerAccessLevel (StickerAccessLevelService Service)
    {
        this.service = Service;
    }

    public boolean hasAccess (long stickerGroupId , long userId)
    {
        return service.hasAccess (stickerGroupId , userId);
    }


    public StickerAccessLevelService getService ()
    {
        return service;
    }
}
