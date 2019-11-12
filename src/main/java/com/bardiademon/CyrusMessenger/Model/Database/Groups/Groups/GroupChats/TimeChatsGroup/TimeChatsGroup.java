package com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.GroupChats.TimeChatsGroup;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.GroupChats.GroupChats;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table (name = "time_chats_group")
public class TimeChatsGroup
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn (name = "id_chat", referencedColumnName = "id")
    private GroupChats groupChats;

    @Column (name = "time_send", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime timeSend;

    @Column (name = "time_delivered", insertable = false)
    @UpdateTimestamp
    private LocalDateTime timeDelivered;

    @Column (name = "time_read", insertable = false)
    @UpdateTimestamp
    private LocalDateTime timeRead;

    public TimeChatsGroup ()
    {
    }

    public TimeChatsGroup (GroupChats groupChats , LocalDateTime timeSend , LocalDateTime timeDelivered , LocalDateTime timeRead)
    {
        this.groupChats = groupChats;
        this.timeSend = timeSend;
        this.timeDelivered = timeDelivered;
        this.timeRead = timeRead;
    }

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    public GroupChats getGroupChats ()
    {
        return groupChats;
    }

    public void setGroupChats (GroupChats groupChats)
    {
        this.groupChats = groupChats;
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
