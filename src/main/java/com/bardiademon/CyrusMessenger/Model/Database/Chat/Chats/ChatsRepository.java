package com.bardiademon.CyrusMessenger.Model.Database.Chat.Chats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatsRepository extends JpaRepository <Chats, Long>
{
}
