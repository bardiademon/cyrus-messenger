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
        return Repository.findByStickerGroupsIdAndMainAccountIdAndTypeAndDeletedFalse (stickerGroupId , userId , StickerAccessLevelType.user) != null;
    }

    /*
     * groupId => Groups id , Gap Groups
     */
    public boolean hasAccessGroup (long stickerGroupId , long groupId)
    {
        return Repository.findByStickerGroupsIdAndGroupsIdAndTypeAndDeletedFalse (stickerGroupId , groupId , StickerAccessLevelType.group) != null;
    }


    public int delete (long idStickerGroup)
    {
        return Repository.delete (idStickerGroup);
    }
}
