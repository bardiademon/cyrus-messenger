package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerGroupsRepository extends JpaRepository <StickerGroups, Long>
{
    @Query ("select sg.id from StickerGroups sg where sg.addedBy.id = :ID_USER")
    List <Long> getIds (@Param ("ID_USER") long idUser);

    StickerGroups findById (long id);

    @Transactional
    @Modifying
    @Query ("update StickerGroups sg set sg.deleted = true , sg.deletedAt = current_timestamp where sg.id = :ID_STICKER_GROUP and sg.addedBy.id = :USER_ID")
    int delete (@Param ("ID_STICKER_GROUP") long idStickerGroup , @Param ("USER_ID") long userId);
}
