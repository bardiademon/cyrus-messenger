package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class StickerAccessLevelService
{
    public final StickerAccessLevelRepository Repository;

    @Autowired
    public StickerAccessLevelService (final StickerAccessLevelRepository Repository)
    {
        this.Repository = Repository;
    }

    public boolean hasAccess (long stickerGroupId , long userId)
    {
        return Repository.findByStickerGroupsIdAndMainAccountIdAndDeletedFalse (stickerGroupId , userId) != null;
    }

    public int delete (long idStickerGroup)
    {
        return Repository.delete (idStickerGroup);
    }
}
