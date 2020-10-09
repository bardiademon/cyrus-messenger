package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerGroups;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerGroupsRepository extends JpaRepository <StickerGroups, Long>
{
    @Query ("select sg.id from StickerGroups sg where sg.addedBy.id = :ID_USER")
    List <Long> getIds (@Param ("ID_USER") long idUser);
}
