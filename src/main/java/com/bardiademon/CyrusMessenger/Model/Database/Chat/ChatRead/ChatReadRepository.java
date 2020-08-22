package com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatRead;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatReadRepository extends JpaRepository <ChatRead, Long>
{
}
