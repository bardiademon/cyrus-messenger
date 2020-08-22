package com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatRead;

import com.bardiademon.CyrusMessenger.Model.Database.Chat.Chats.Chats;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "chat_read")
public final class ChatRead
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_chat", referencedColumnName = "id")
    private Chats chats;

    @Column (name = "read_at", updatable = false, nullable = false)
    private LocalDateTime readAt;

    @Column (name = "received_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime receivedAt;

    private boolean read;

    private boolean received;

    @ManyToOne
    @Column (name = "read_by", updatable = false, nullable = false)
    private MainAccount readBy;

    public ChatRead ()
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

    public Chats getChats ()
    {
        return chats;
    }

    public void setChats (Chats chats)
    {
        this.chats = chats;
    }

    public LocalDateTime getReadAt ()
    {
        return readAt;
    }

    public void setReadAt (LocalDateTime readAt)
    {
        this.readAt = readAt;
    }

    public LocalDateTime getReceivedAt ()
    {
        return receivedAt;
    }

    public void setReceivedAt (LocalDateTime receivedAt)
    {
        this.receivedAt = receivedAt;
    }

    public boolean isRead ()
    {
        return read;
    }

    public void setRead (boolean read)
    {
        this.read = read;
    }

    public boolean isReceived ()
    {
        return received;
    }

    public void setReceived (boolean received)
    {
        this.received = received;
    }

    public MainAccount getReadBy ()
    {
        return readBy;
    }

    public void setReadBy (MainAccount readBy)
    {
        this.readBy = readBy;
    }
}
