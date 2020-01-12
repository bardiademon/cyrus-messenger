package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityUserChatRepository extends JpaRepository<SecurityUserChat, Long>
{
    SecurityUserChat findByMainAccount (MainAccount mainAccount);
}
