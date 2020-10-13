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

    public boolean hasAccessUser (long stickerGroupId , long userId)
    {
        return hasAccess (stickerGroupId , userId , StickerAccessLevelType.user);
    }

    /*
     * groupId => Groups id , Gap Groups
     */
    public boolean hasAccessGroup (long stickerGroupId , long groupId)
    {
        return hasAccess (stickerGroupId , groupId , StickerAccessLevelType.group);
    }

    public boolean hasAccess (long stickerGroupId , long groupId , StickerAccessLevelType type)
    {
        return Repository.findByStickerGroupsIdAndGroupsIdAndTypeAndDeletedFalse (stickerGroupId , groupId , type) != null;
    }

    public int delete (long idStickerGroup)
    {
        return Repository.delete (idStickerGroup);
    }
}
