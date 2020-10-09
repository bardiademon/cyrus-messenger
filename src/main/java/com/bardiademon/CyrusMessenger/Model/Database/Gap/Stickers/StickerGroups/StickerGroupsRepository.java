package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerGroupsRepository extends JpaRepository <StickerGroups, Long>
{
}
