package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StickersRepository extends JpaRepository <Stickers, Long>
{
    Stickers findByIdAndDeletedFalseAndGroupDeletedFalseAndGroupActiveTrue (long id);

    @Query ("select s from Stickers s where s.deleted = false and s.group.deleted = false and s.group.active = true and s.id = :ID")
    Stickers getSticker (@Param ("ID") long id);
}
