package com.bardiademon.CyrusMessenger.Model.Database.Gap.Online;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineRepository extends JpaRepository <Online, Long>
{

    @Modifying
    @Transactional
    @Query ("update Online online set online.last = false , online.offlineAt = :TIME_OFFLINE where online.mainAccount.id = :ID_USER and online.last = true")
    void disableLast (@Param ("ID_USER") long idUser , @Param ("TIME_OFFLINE") LocalDateTime timeOffline);

    Online findByMainAccountIdAndLastTrue (long idUser);
}
