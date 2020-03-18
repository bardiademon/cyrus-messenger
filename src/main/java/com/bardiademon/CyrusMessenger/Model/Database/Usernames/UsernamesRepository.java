package com.bardiademon.CyrusMessenger.Model.Database.Usernames;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsernamesRepository extends JpaRepository<Usernames, Long>
{
    Usernames findByUsernameAndUsernameForAndDeletedFalseAndActiveTrue (String username , UsernameFor usernameFor);

    Usernames findByUsernameAndUsernameForAndDeletedFalseAndActiveTrueAndMainAccountActiveTrue (String username , UsernameFor usernameFor);
}
