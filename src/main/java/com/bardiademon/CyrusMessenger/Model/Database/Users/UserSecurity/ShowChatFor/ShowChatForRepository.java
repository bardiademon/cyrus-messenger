package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor;

import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserChat.SecurityUserChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowProfileFor.ShowProfileFor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowChatForRepository extends JpaRepository<ShowChatFor, Long>
{
    ShowProfileFor findBySecurityUserChat (SecurityUserChat securityUserChat);
}
