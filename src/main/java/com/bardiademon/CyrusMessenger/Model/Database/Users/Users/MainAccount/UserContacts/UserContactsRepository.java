package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContactsRepository extends JpaRepository<UserContacts, Long>
{
    UserContacts findByMainAccountIdAndMainAccountContactIdAndDeletedFalse (long id , long idContact);

    UserContacts findByPhoneLikeAndDeleted (String phone , boolean deleted);

    @Query ("select userContacts from UserContacts userContacts " +
            "where userContacts.mainAccountContact.phone like :PHONE and userContacts.deleted = :DELETED")
    UserContacts hasPhoneMainAccount (@Param ("PHONE") String phone , @Param ("DELETED") boolean deleted);

}
