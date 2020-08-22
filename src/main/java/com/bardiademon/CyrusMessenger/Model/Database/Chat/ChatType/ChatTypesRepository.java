package com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatTypesRepository extends JpaRepository <ChatTypes, Long>
{
}
