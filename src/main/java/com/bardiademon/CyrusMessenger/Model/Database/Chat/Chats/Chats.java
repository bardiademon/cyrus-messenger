package com.bardiademon.CyrusMessenger.Model.Database.Chat.Chats;

import com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatFiles.ChatFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatRead.ChatRead;
import com.bardiademon.CyrusMessenger.Model.Database.Chat.ChatType.ChatTypes;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "chats")
public final class Chats
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    private String text;

    @OneToMany (mappedBy = "chats")
    private List <ChatTypes> chatTypes;

    @Enumerated (EnumType.STRING)
    @Column (name = "chat_for", nullable = false)
    private ChatFor chatFor;

    @OneToOne
    @Column (name = "chat_from", nullable = false)
    private MainAccount from;


    // if private chat else is null
    @OneToOne
    @Column (name = "chat_to_user")
    private MainAccount toUser;

    // if group chat else is null
    @OneToOne
    @Column (name = "chat_to_group")
    private Groups toGroup;

    private boolean delete;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @Column (name = "deleted_both")
    private boolean deletedBoth;

    @OneToOne
    @Column (name = "deleted_by")
    private MainAccount deletedBy;

    @OneToMany (mappedBy = "chats")
    @JoinTable (joinColumns = @JoinColumn (name = "id_chat_read", referencedColumnName = "id"))
    private List <ChatRead> chatRead;

    @OneToMany
    private List <ChatFiles> filesChats;

    @OneToOne
    @JoinColumn (name = "reply", referencedColumnName = "id")
    private Chats reply;

    public Chats ()
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

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public List <ChatTypes> getChatTypes ()
    {
        return chatTypes;
    }

    public void setChatTypes (List <ChatTypes> chatTypes)
    {
        this.chatTypes = chatTypes;
    }

    public ChatFor getChatFor ()
    {
        return chatFor;
    }

    public void setChatFor (ChatFor chatFor)
    {
        this.chatFor = chatFor;
    }

    public MainAccount getFrom ()
    {
        return from;
    }

    public void setFrom (MainAccount from)
    {
        this.from = from;
    }

    public MainAccount getToUser ()
    {
        return toUser;
    }

    public void setToUser (MainAccount toUser)
    {
        this.toUser = toUser;
    }

    public Groups getToGroup ()
    {
        return toGroup;
    }

    public void setToGroup (Groups toGroup)
    {
        this.toGroup = toGroup;
    }

    public boolean isDelete ()
    {
        return delete;
    }

    public void setDelete (boolean delete)
    {
        this.delete = delete;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public boolean isDeletedBoth ()
    {
        return deletedBoth;
    }

    public void setDeletedBoth (boolean deletedBoth)
    {
        this.deletedBoth = deletedBoth;
    }

    public MainAccount getDeletedBy ()
    {
        return deletedBy;
    }

    public void setDeletedBy (MainAccount deletedBy)
    {
        this.deletedBy = deletedBy;
    }

    public List <ChatRead> getChatRead ()
    {
        return chatRead;
    }

    public void setChatRead (List <ChatRead> chatRead)
    {
        this.chatRead = chatRead;
    }

    public List <ChatFiles> getFilesChats ()
    {
        return filesChats;
    }

    public void setFilesChats (List <ChatFiles> filesChats)
    {
        this.filesChats = filesChats;
    }

    public Chats getReply ()
    {
        return reply;
    }

    public void setReply (Chats reply)
    {
        this.reply = reply;
    }
}
