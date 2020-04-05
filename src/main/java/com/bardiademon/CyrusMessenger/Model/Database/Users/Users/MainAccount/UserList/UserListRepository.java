package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserListRepository extends JpaRepository <UserList, Long>
{
    UserList findByMainAccountIdAndUserIdAndTypeAndDeletedFalse (long idUser , long idUser2 , UserListType type);

    UserList findByMainAccountIdAndUserIdAndDeletedFalseAndTypeNot (long idUser , long idUser2 , UserListType type);

    UserList findByMainAccountIdAndUserIdAndDeletedFalse (long idUser , long idUser2 );
}
