package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserEmails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEmailsRepository extends JpaRepository <Long, UserEmails>
{
}
