package com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatFiles;

import com.bardiademon.CyrusMessenger.Model.Database.Chat.Chats.Chats;
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
@Table (name = "chat_files")
public final class ChatFiles
{
    @Id
    @GeneratedValue
    private long id;

    private String name;

    private long size;

    private ChatFilesTypes types;

    @ManyToOne
    @JoinColumn (name = "id_chat", referencedColumnName = "id")
    private Chats chats;

    @Column (name = "uploaded_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    public ChatFiles ()
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

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public long getSize ()
    {
        return size;
    }

    public void setSize (long size)
    {
        this.size = size;
    }

    public ChatFilesTypes getTypes ()
    {
        return types;
    }

    public void setTypes (ChatFilesTypes types)
    {
        this.types = types;
    }

    public Chats getChats ()
    {
        return chats;
    }

    public void setChats (Chats chats)
    {
        this.chats = chats;
    }

    public LocalDateTime getUploadedAt ()
    {
        return uploadedAt;
    }

    public void setUploadedAt (LocalDateTime uploadedAt)
    {
        this.uploadedAt = uploadedAt;
    }

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }
}
