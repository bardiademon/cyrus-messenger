package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerAccessLevelRepository extends JpaRepository <StickerAccessLevel, Long>
{
    StickerAccessLevel findByStickerGroupsIdAndMainAccountIdAndDeletedFalse (long stickerGroupId , long userId);

    @Transactional
    @Modifying
    @Query ("update StickerAccessLevel sal set sal.deleted = true , sal.deletedAt = current_timestamp where sal.stickerGroups.id = :ID_STICKER_GROUP and sal.mainAccount.id = :USER_ID")
    int delete (@Param ("ID_STICKER_GROUP") long idStickerGroup , @Param ("USER_ID") long userId);

}
