package com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatFiles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatFilesRepository extends JpaRepository <ChatFiles, Long>
{
}