package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.ListUsersForUser;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// LUFU => List Users For User
@Repository
public interface LUFU_Repository extends JpaRepository<ListUsersForUser, Long>
{
    ListUsersForUser findByMainAccountAndMainAccountListAndUserForAndDeletedFalse
            (MainAccount user , MainAccount userList , UserFor userFor);
}
