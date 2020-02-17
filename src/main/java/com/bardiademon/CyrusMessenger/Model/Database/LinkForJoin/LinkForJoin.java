package com.bardiademon.CyrusMessenger.Model.Database.LinkForJoin;

import com.bardiademon.CyrusMessenger.Model.Database.Channel.Channel.Channel.Channel;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table (name = "link_for_join")
public class LinkForJoin
{
    public final static int MAX_LEN_CODE = 20;

    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @Column (nullable = false, updatable = false, length = 1000)
    private String link;

    @Column (name = "link_for", nullable = false)
    private LinkFor linkFor;

    @ManyToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private Groups groups;

    @ManyToOne
    @JoinColumn (name = "id_channel", referencedColumnName = "id")
    private Channel channel;

    @Column (name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean deleted;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    public LinkForJoin ()
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

    public String getLink ()
    {
        return link;
    }

    public void setLink (String link)
    {
        this.link = link;
    }

    public LinkFor getLinkFor ()
    {
        return linkFor;
    }

    public void setLinkFor (LinkFor linkFor)
    {
        this.linkFor = linkFor;
    }

    public Groups getGroups ()
    {
        return groups;
    }

    public void setGroups (Groups groups)
    {
        this.groups = groups;
    }

    public Channel getChannel ()
    {
        return channel;
    }

    public void setChannel (Channel channel)
    {
        this.channel = channel;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
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

    public enum LinkFor
    {
        channel, group
    }
}
