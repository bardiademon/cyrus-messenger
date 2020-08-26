package com.bardiademon.CyrusMessenger.Model.Database.Chat.Online;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.corundumstudio.socketio.SocketIOClient;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "user_online")
public final class Online
{
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_user", referencedColumnName = "id")
    private MainAccount mainAccount;

    @Column (name = "online_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime onlineAt;

    @Column (name = "offline_at", nullable = false, updatable = false)
    private LocalDateTime offlineAt;

    @Column (name = "client_uuid", length = 10000)
    private String uuid;

    @Transient
    private SocketIOClient client;

    @Transient
    private LocalDateTime announcementOfPresence;

    @Column (name = "is_last")
    private boolean last;

    public Online ()
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

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public void setMainAccount (MainAccount mainAccount)
    {
        this.mainAccount = mainAccount;
    }

    public LocalDateTime getOnlineAt ()
    {
        return onlineAt;
    }

    public void setOnlineAt (LocalDateTime onlineAt)
    {
        this.onlineAt = onlineAt;
    }

    public LocalDateTime getOfflineAt ()
    {
        return offlineAt;
    }

    public void setOfflineAt (LocalDateTime offlineAt)
    {
        this.offlineAt = offlineAt;
    }

    public String getUuid ()
    {
        return uuid;
    }

    public void setUuid (String uuid)
    {
        this.uuid = uuid;
    }

    public SocketIOClient getClient ()
    {
        return client;
    }

    public void setClient (SocketIOClient client)
    {
        this.client = client;
    }

    public LocalDateTime getAnnouncementOfPresence ()
    {
        return announcementOfPresence;
    }

    public void setAnnouncementOfPresence (LocalDateTime announcementOfPresence)
    {
        this.announcementOfPresence = announcementOfPresence;
    }

    public boolean isLast ()
    {
        return last;
    }

    public void setLast (boolean last)
    {
        this.last = last;
    }
}
