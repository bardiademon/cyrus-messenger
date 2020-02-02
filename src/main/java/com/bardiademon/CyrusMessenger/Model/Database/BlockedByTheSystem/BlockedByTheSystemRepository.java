package com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BlockedByTheSystemRepository extends JpaRepository<BlockedByTheSystem, Long>
{

    BlockedByTheSystem findByMainAccountIdAndBlockedForAndDescriptionAndActiveTrue (long id , BlockedFor blockedFor , String des);

    BlockedByTheSystem findByMainAccountIdAndBlockedForAndActiveTrue (long id , BlockedFor blockedFor);

    BlockedByTheSystem findByIpAndBlockedForAndDescriptionAndActiveTrue (String ip , BlockedFor blockedFor , String des);

    BlockedByTheSystem findByIpAndBlockedForAndActiveTrue (String ip , BlockedFor blockedFor);

    @Transactional
    @Modifying
    @Query ("update BlockedByTheSystem blockedByTheSystem set blockedByTheSystem.active=false where " +
            "blockedByTheSystem.ip=:IP and blockedByTheSystem.blockedFor=:BLOCK_FOR and blockedByTheSystem.description=:DES")
    void deactive (@Param ("IP") String ip , @Param ("BLOCK_FOR") BlockedFor blockedFor , @Param ("DES") String type);

    @Transactional
    @Modifying
    @Query ("update BlockedByTheSystem blockedByTheSystem set blockedByTheSystem.active=false where " +
            "blockedByTheSystem.mainAccount.id=:ID_USER and blockedByTheSystem.blockedFor=:BLOCK_FOR and blockedByTheSystem.description=:DES")
    void deactive (@Param ("ID_USER") long idUser , @Param ("BLOCK_FOR") BlockedFor blockedFor , @Param ("DES") String des);

    @Transactional
    @Modifying
    @Query ("update BlockedByTheSystem blockedByTheSystem set blockedByTheSystem.active=false where " +
            "blockedByTheSystem.ip=:IP and blockedByTheSystem.blockedFor=:BLOCK_FOR")
    void deactive (@Param ("IP") String ip , @Param ("BLOCK_FOR") BlockedFor blockedFor);

    @Transactional
    @Modifying
    @Query ("update BlockedByTheSystem blockedByTheSystem set blockedByTheSystem.active=false where " +
            "blockedByTheSystem.mainAccount.id=:ID_USER and blockedByTheSystem.blockedFor=:BLOCK_FOR")
    void deactive (@Param ("ID_USER") long idUser , @Param ("BLOCK_FOR") BlockedFor blockedFor);


    int countByMainAccountIdAndBlockedForAndUnBlockedAtIsNullAndActiveTrue (long id , BlockedFor blockedFor);

    int countByIpAndBlockedForAndUnBlockedAtIsNullAndActiveTrue (String ip , BlockedFor blockedFor);
}
