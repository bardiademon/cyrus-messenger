package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserFriends;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFriendsRepository extends JpaRepository<UserFriends, Long>
{
    UserFriends findByMainAccountAndMainAccountFriendAndDeletedAtIsNull (MainAccount user , MainAccount friend);

    UserFriends findByMainAccountAndMainAccountFriendAndStatus
            (MainAccount user , MainAccount friend , StatusFriends statusFriends);

    List<UserFriends> findByMainAccountAndDeletedAtIsNull (MainAccount user);

    List<UserFriends> findByMainAccountAndDeletedAtIsNotNull (MainAccount user);

    List<UserFriends> findByMainAccountAndStatus (MainAccount user , StatusFriends statusFriends);

    List<UserFriends> findAllByMainAccountAndStatus (MainAccount user , StatusFriends statusFriends);

    List<UserFriends> findAllByMainAccount (MainAccount user);

    @Query ("select userFriends.mainAccountFriend.username from UserFriends userFriends " +
            "where userFriends.mainAccount.id = :ID and userFriends.status = :STATUS")
    List<String> findUsernameUser (@Param ("ID") long id , @Param ("STATUS") StatusFriends status);
}
