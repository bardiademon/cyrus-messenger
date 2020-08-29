package com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "gap_read")
public final class GapRead
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn (name = "id_gap", referencedColumnName = "id")
    private Gaps gaps;

    @Column (name = "read_at", updatable = false, nullable = false)
    private LocalDateTime readAt;

    @Column (name = "received_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime receivedAt;

    @Column (name = "is_read")
    private boolean read;

    @Column (name = "is_received")
    private boolean received;

    @OneToOne
    @JoinColumn (name = "read_by", referencedColumnName = "id", nullable = false)
    private MainAccount readBy;

    public GapRead ()
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

    public Gaps getGaps ()
    {
        return gaps;
    }

    public void setGaps (Gaps gaps)
    {
        this.gaps = gaps;
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
