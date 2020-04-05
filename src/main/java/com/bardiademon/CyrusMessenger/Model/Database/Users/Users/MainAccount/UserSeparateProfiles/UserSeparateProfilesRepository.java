package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.UserSeparateProfiles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSeparateProfilesRepository extends JpaRepository <UserSeparateProfiles, Long>
{
    List <UserSeparateProfiles> findByMainAccountIdAndDeletedFalse (long idUser);
}
