package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityUserGapRepository extends JpaRepository<SecurityUserGap, Long>
{
    SecurityUserGap findByMainAccount (MainAccount mainAccount);
}
