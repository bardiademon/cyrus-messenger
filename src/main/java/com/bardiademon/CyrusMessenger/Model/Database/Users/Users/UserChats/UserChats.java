package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.UserChats;

import com.bardiademon.CyrusMessenger.Model.Database.TypeChat;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.time.LocalDateTime;

@Entity
@Table (name = "user_chats")
public class UserChats
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn (name = "message_from", referencedColumnName = "id")
    private MainAccount from;

    @ManyToOne
    @JoinColumn (name = "message_to", referencedColumnName = "id")
    private MainAccount to;

    @ManyToOne
    @JoinColumn (name = "reply", referencedColumnName = "id")
    private UserChats reply;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private TypeChat type;

    @Column (nullable = false)
    private String text;

    @Column (name = "real_text")
    private String realText;

    @Column (name = "time_send", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime timeSend;

    @Column (name = "time_delivered", insertable = false)
    @UpdateTimestamp
    private LocalDateTime timeDelivered;

    @Column (name = "time_read", insertable = false)
    @UpdateTimestamp
    private LocalDateTime timeRead;

    public UserChats ()
    {
    }

    public UserChats (MainAccount from , MainAccount to , UserChats reply , TypeChat type , String text , String realText , LocalDateTime timeSend , LocalDateTime timeDelivered , LocalDateTime timeRead)
    {
        this.from = from;
        this.to = to;
        this.reply = reply;
        this.type = type;
        this.text = text;
        this.realText = realText;
        this.timeSend = timeSend;
        this.timeDelivered = timeDelivered;
        this.timeRead = timeRead;
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

    public MainAccount getTo ()
    {
        return to;
    }

    public void setTo (MainAccount to)
    {
        this.to = to;
    }

    public UserChats getReply ()
    {
        return reply;
    }

    public void setReply (UserChats reply)
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

    public LocalDateTime getTimeSend ()
    {
        return timeSend;
    }

    public void setTimeSend (LocalDateTime timeSend)
    {
        this.timeSend = timeSend;
    }

    public LocalDateTime getTimeDelivered ()
    {
        return timeDelivered;
    }

    public void setTimeDelivered (LocalDateTime timeDelivered)
    {
        this.timeDelivered = timeDelivered;
    }

    public LocalDateTime getTimeRead ()
    {
        return timeRead;
    }

    public void setTimeRead (LocalDateTime timeRead)
    {
        this.timeRead = timeRead;
    }
}
