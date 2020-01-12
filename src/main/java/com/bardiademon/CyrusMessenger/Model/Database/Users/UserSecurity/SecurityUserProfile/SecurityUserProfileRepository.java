package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserProfile;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityUserProfileRepository extends JpaRepository<SecurityUserProfile, Long>
{
    SecurityUserProfile findByMainAccount (MainAccount mainAccount);
}
