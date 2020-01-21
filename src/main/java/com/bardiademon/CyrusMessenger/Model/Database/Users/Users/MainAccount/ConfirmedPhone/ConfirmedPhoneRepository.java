package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ConfirmedPhone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmedPhoneRepository extends JpaRepository<ConfirmedPhone, Long>
{
    ConfirmedPhone findByPhoneAndActiveTrue (String phone);

    ConfirmedPhone findByCodeAndActiveTrueAndConfirmCodeConfirmedTrue (String code);
}
