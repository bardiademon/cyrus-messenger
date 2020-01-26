package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserBlocked;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlockedRepository extends JpaRepository<UserBlocked, Long>
{
    UserBlocked findByMainAccountIdAndMainAccountBlockedIdAndUnblockedFalse (long idUser , long idUserBlock);

}
