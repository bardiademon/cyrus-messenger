package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UsersStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersStatusRepository extends JpaRepository<UsersStatus, Long>
{
    UsersStatus findByMainAccountIdAndStatusAndActiveRowTrue (long idUser , Status status);
}
