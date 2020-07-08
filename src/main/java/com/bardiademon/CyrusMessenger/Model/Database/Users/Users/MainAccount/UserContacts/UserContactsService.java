package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserContacts;

import java.util.List;
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

    public UserContacts withId (long id , long idUser)
    {
        return Repository.findByIdAndMainAccountIdAndDeletedFalse (id , idUser);
    }

    public UserContacts hasPhoneForUser (long idUser , String phone)
    {
        return Repository.findByPhoneLikeAndMainAccountIdAndDeleted ("%" + phone , idUser , false);
    }

    public List <UserContacts> listContacts (long idUser)
    {
        return Repository.findByMainAccountIdAndDeletedFalse (idUser);
    }

}
