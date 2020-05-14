package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFriendsRepository extends JpaRepository <UserFriends, Long>
{
    UserFriends findByMainAccountAndMainAccountFriendAndDeletedFalse (MainAccount user , MainAccount friend);

    UserFriends findByMainAccountAndMainAccountFriendAndStatus
            (MainAccount user , MainAccount friend , StatusFriends statusFriends);

    List <UserFriends> findByMainAccountAndDeletedFalse (MainAccount user);

    List <UserFriends> findByMainAccountAndDeletedAtIsNotNull (MainAccount user);

    List <UserFriends> findByMainAccountAndStatus (MainAccount user , StatusFriends statusFriends);

    List <UserFriends> findAllByMainAccountAndStatus (MainAccount user , StatusFriends statusFriends);

    List <UserFriends> findAllByMainAccount (MainAccount user);

    @Query ("select userFriends.mainAccountFriend.username from UserFriends userFriends " +
            "where userFriends.mainAccount.id = :ID and userFriends.status = :STATUS")
    List <String> findUsernameUser (@Param ("ID") long id , @Param ("STATUS") StatusFriends status);

    @Query ("select usernames.username from Usernames usernames where usernames.active = true and usernames.mainAccount.id in (" +
            "select account.id from MainAccount account where account.id in (" +
            "select friends.mainAccount.id from UserFriends friends where friends.status = 'awaiting_approval' and friends.mainAccountFriend.id = :ID_USER_FRIEND))")
    List <String> getUsernameRequests (@Param ("ID_USER_FRIEND") long idUserFriend);

}
