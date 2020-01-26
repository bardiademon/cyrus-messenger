package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserContactsService
{
    public final UserContactsRepository Repository;

    @Autowired
    public UserContactsService (UserContactsRepository Repository)
    {
        this.Repository = Repository;
    }

    public UserContacts findContact (long id , long idContact)
    {
        return Repository.findByMainAccountIdAndMainAccountContactIdAndDeletedFalse (id , idContact);
    }

    public UserContacts hasPhone (String phone)
    {
        UserContacts userContacts;

        if (phone.substring (0 , 1).equals ("0")) phone = phone.substring (1);

        userContacts = Repository.findByPhoneLikeAndDeleted ("%" + phone , false);
        if (userContacts == null) return Repository.hasPhoneMainAccount ("%" + phone , false);
        else return userContacts;
    }

}
