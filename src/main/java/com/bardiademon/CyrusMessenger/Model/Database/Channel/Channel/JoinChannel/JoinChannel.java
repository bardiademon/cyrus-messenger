package com.bardiademon.CyrusMessenger.Model.Database.Channel.Channel.JoinChannel;

import com.bardiademon.CyrusMessenger.Model.Database.Channel.Channel.Channel.Channel;
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
import java.time.LocalDateTime;

@Entity
@Table (name = "list_join_channel")
public class JoinChannel
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_channel", referencedColumnName = "id")
    private Channel channels;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Column (name = "time_join", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime timeJoin;

    @Column (name = "time_leave", nullable = false, insertable = false)
    @UpdateTimestamp
    private LocalDateTime timeLeave;

    public JoinChannel ()
    {
    }

    public JoinChannel (Channel channels , MainAccount mainAccount , LocalDateTime timeJoin , LocalDateTime timeLeave)
    {
        this.channels = channels;
        this.mainAccount = mainAccount;
        this.timeJoin = timeJoin;
        this.timeLeave = timeLeave;
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public Channel getChannels ()
    {
        return channels;
    }

    public void setChannels (Channel channels)
    {
        this.channels = channels;
    }

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public void setMainAccount (MainAccount mainAccount)
    {
        this.mainAccount = mainAccount;
    }

    public LocalDateTime getTimeJoin ()
    {
        return timeJoin;
    }

    public void setTimeJoin (LocalDateTime timeJoin)
    {
        this.timeJoin = timeJoin;
    }

    public LocalDateTime getTimeLeave ()
    {
        return timeLeave;
    }

    public void setTimeLeave (LocalDateTime timeLeave)
    {
        this.timeLeave = timeLeave;
    }
}
