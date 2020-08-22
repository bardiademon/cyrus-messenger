package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEmailsRepository extends JpaRepository <UserEmails, Long>
{
    UserEmails findByEmailForAndDeletedFalseOrConfirmedFalse (EmailFor emailFor);

    UserEmails findByEmailForAndEmailAndDeletedFalseOrConfirmedFalse (EmailFor emailFor , String email);
}
