package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.GroupChats;

import com.bardiademon.CyrusMessenger.Model.Database.TypeChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Entity
@Table (name = "group_chats")
public class GroupChats
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn (name = "message_from", referencedColumnName = "id")
    private MainAccount from;

    @ManyToOne
    @JoinColumn (name = "reply", referencedColumnName = "id")
    private GroupChats reply;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private TypeChat type;

    @Column (nullable = false)
    private String text;

    @Column (name = "real_text")
    private String realText;

    public GroupChats ()
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

    public MainAccount getFrom ()
    {
        return from;
    }

    public void setFrom (MainAccount from)
    {
        this.from = from;
    }

    public GroupChats getReply ()
    {
        return reply;
    }

    public void setReply (GroupChats reply)
    {
        this.reply = reply;
    }

    public TypeChat getType ()
    {
        return type;
    }

    public void setType (TypeChat type)
    {
        this.type = type;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getRealText ()
    {
        return realText;
    }

    public void setRealText (String realText)
    {
        this.realText = realText;
    }
}
