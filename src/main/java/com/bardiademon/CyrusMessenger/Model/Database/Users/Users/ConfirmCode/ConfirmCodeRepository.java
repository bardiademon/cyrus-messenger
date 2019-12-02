package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, Long>
{
    @Query ("select confirmCode from ConfirmCode confirmCode " +
            "where confirmCode.id = :ID " +
            "and confirmCode.code = :CODE " +
            "and confirmCode.sendCodeTo = :PHONE " +
            "and confirmCode.timeToBeOutdated > :TIME_NOW and confirmCode.using = false")
    ConfirmCode findCode (@Param ("ID") long id , @Param ("CODE") String code , @Param ("PHONE") String phone , @Param ("TIME_NOW") LocalDateTime now);


    ConfirmCode findBySendCodeToAndMainAccountIdAndConfirmCodeForAndConfirmedTrue
            (String sendCodeTo , long mainAccountId , ConfirmCodeFor confirmCodeFor);
}
