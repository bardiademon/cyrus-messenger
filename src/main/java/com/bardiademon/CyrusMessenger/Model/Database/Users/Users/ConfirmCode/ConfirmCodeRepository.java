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
            "and confirmCode.mainAccount.id = :ID_USER " +
            "and confirmCode.code = :CODE " +
            "and confirmCode.sendCodeTo = :SEND_CODE_TO " +
            "and confirmCode.timeToBeOutdated > :TIME_NOW and confirmCode.using = false")
    ConfirmCode findCode
            (
                    @Param ("ID") long id ,
                    @Param ("ID_USER") long idUser ,
                    @Param ("CODE") String code ,
                    @Param ("SEND_CODE_TO") String sendCodeTo ,
                    @Param ("TIME_NOW") LocalDateTime now
            );

    @Query ("select confirmCode from ConfirmCode confirmCode " +
            "where confirmCode.id = :ID " +
            "and confirmCode.code = :CODE " +
            "and confirmCode.confirmCodeFor = :CONFIRM_CODE_FOR " +
            "and confirmCode.sendCodeTo = :SEND_CODE_TO " +
            "and confirmCode.timeToBeOutdated > :TIME_NOW and confirmCode.using = false")
    ConfirmCode findCode
            (
                    @Param ("ID") long id ,
                    @Param ("CODE") String code ,
                    @Param ("CONFIRM_CODE_FOR") ConfirmCodeFor confirmCodeFor ,
                    @Param ("SEND_CODE_TO") String sendCodeTo ,
                    @Param ("TIME_NOW") LocalDateTime now
            );

    @Query ("select confirmCode from ConfirmCode confirmCode " +
            "where confirmCode.confirmCodeFor = :CONFIRM_CODE_FOR " +
            "and confirmCode.sendCodeTo = :SEND_CODE_TO " +
            "and confirmCode.timeToBeOutdated > :TIME_NOW and confirmCode.using = false")
    ConfirmCode findCode
            (
                    @Param ("CONFIRM_CODE_FOR") ConfirmCodeFor confirmCodeFor ,
                    @Param ("SEND_CODE_TO") String sendCodeTo ,
                    @Param ("TIME_NOW") LocalDateTime now
            );

    @Query ("select confirmCode from ConfirmCode confirmCode " +
            "where confirmCode.mainAccount.id = :ID_USER " +
            "and confirmCode.sendCodeTo = :SEND_CODE_TO " +
            "and confirmCode.confirmCodeFor = :CONFIRM_CODE_FOR " +
            "and confirmCode.timeToBeOutdated > :TIME_NOW and confirmCode.using = false")
    ConfirmCode findCode (@Param ("ID_USER") long idUser , @Param ("SEND_CODE_TO") String sendCodeTo , @Param ("TIME_NOW") LocalDateTime now , @Param ("CONFIRM_CODE_FOR") ConfirmCodeFor confirmCodeFor);

    ConfirmCode findBySendCodeToAndMainAccountIdAndConfirmCodeForAndConfirmedTrue
            (String sendCodeTo , long mainAccountId , ConfirmCodeFor confirmCodeFor);

    ConfirmCode findByCodeAndConfirmedFalseAndUsingFalse (String code);

    ConfirmCode findByMainAccountIdAndConfirmCodeForAndConfirmedTrue (long id , ConfirmCodeFor confirmCodeFor);

    ConfirmCode findById (long id);

}
