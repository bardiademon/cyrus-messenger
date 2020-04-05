package com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.ShowChatFor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowChatForRepository extends JpaRepository<ShowChatFor, Long>
{
}
