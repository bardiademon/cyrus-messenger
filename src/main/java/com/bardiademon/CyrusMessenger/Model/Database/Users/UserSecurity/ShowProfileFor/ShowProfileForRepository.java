package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile.SecurityUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowProfileForRepository extends JpaRepository<ShowProfileFor, Long>
{
    ShowProfileFor findBySecurityUserProfile (SecurityUserProfile securityUserProfile);

}
