package com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatType;

import com.bardiademon.CyrusMessenger.Model.Database.Chat.Chats.Chats;
import com.bardiademon.CyrusMessenger.ServerSocket.Chat.ChatType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "chat_types")
public final class ChatTypes
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Column (name = "chat_type", nullable = false)
    @Enumerated (EnumType.STRING)
    private ChatType chatType;

    @ManyToOne
    @JoinColumn (name = "id_chat", referencedColumnName = "id")
    private Chats chats;

    public ChatTypes ()
    {
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public ChatType getChatType ()
    {
        return chatType;
    }

    public void setChatType (ChatType chatType)
    {
        this.chatType = chatType;
    }

    public Chats getChats ()
    {
        return chats;
    }

    public void setChats (Chats chats)
    {
        this.chats = chats;
    }
}
