package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmedRepository extends JpaRepository <Confirmed, Long>
{
    Confirmed findByValueAndActiveTrue (String value);

    Confirmed findByCodeAndActiveTrueAndConfirmCodeConfirmedTrue (String code);

    Confirmed findByCodeAndActiveTrueAndConfirmCodeConfirmedTrueAndConfirmedFor (String code , ConfirmCodeFor confirmedFor);

    @Transactional
    @Modifying
    @Query ("update Confirmed con set con.active = false where con.value = :VALUE and con.active = true and con.confirmedFor = :CONFIRMED_FOR")
    void deactive (@Param ("VALUE") String phone , @Param ("CONFIRMED_FOR") ConfirmedFor confirmedFor);

}
