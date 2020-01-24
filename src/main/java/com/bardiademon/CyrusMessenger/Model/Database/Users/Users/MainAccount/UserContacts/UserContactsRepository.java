package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContactsRepository extends JpaRepository<UserContacts, Long>
{
    UserContacts findByMainAccountIdAndMainAccountContactIdAndDeletedFalse (long id , long idContact);
}
