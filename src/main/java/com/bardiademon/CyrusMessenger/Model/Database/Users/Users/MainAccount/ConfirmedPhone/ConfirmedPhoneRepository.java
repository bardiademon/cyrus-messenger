package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ConfirmedPhone;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmedPhoneRepository extends JpaRepository <ConfirmedPhone, Long>
{
    ConfirmedPhone findByPhoneAndActiveTrue (String phone);

    ConfirmedPhone findByCodeAndActiveTrueAndConfirmCodeConfirmedTrue (String code);

    @Transactional
    @Modifying
    @Query ("update ConfirmedPhone con set con.active = false where con.phone = :PHONE and con.active = true")
    void deactivePhone (@Param ("PHONE") String phone);
}
