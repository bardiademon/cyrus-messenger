package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBlockedRepository extends JpaRepository<UserBlocked, Long>
{
    List<UserBlocked> findByMainAccountIdAndMainAccountBlockedIdAndUnblockedFalse (long idUser , long idUserBlock);

    UserBlocked findByMainAccountIdAndMainAccountBlockedIdAndUnblockedFalseAndType (long idUser , long idUserBlock , UserBlocked.Type type);

    List<UserBlocked> findByMainAccountIdAndUnblockedFalse (long idUser);

}
