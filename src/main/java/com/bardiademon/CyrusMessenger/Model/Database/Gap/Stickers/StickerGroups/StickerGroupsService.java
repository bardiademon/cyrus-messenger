package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class StickerGroupsService
{
    public final StickerGroupsRepository Repository;

    @Autowired
    public StickerGroupsService (final StickerGroupsRepository Repository)
    {
        this.Repository = Repository;
    }
}
