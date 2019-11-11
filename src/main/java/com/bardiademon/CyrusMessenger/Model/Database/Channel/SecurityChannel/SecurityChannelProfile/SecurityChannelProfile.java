package com.bardiademon.CyrusMessenger.Model.Database.Channel.SecurityChannel.SecurityChannelProfile;

import com.bardiademon.CyrusMessenger.Model.Database.Channel.Channel.Channel.Channel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;

@Entity
@Table (name = "security_channel_profile")
public class SecurityChannelProfile
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @OneToOne
    @JoinColumn (name = "id_channel", referencedColumnName = "id")
    private Channel channel;

    @Column (name = "family_chat", nullable = false)
    private boolean familyChat = false;

    @Column (name = "can_forward_message", nullable = false)
    private boolean canForwardMessage = true;

    @Column (name = "show_member", nullable = false)
    private boolean showMember = false;

    @Column (name = "show_in_search", nullable = false)
    private boolean showInSearch = true;

    @Column (name = "max_member")
    private Integer maxMember;

    public SecurityChannelProfile ()
    {
    }

    public SecurityChannelProfile (Channel channel , boolean familyChat , boolean canForwardMessage , boolean showMember , boolean showInSearch , Integer maxMember)
    {
        this.channel = channel;
        this.familyChat = familyChat;
        this.canForwardMessage = canForwardMessage;
        this.showMember = showMember;
        this.showInSearch = showInSearch;
        this.maxMember = maxMember;
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public Channel getChannel ()
    {
        return channel;
    }

    public void setChannel (Channel channel)
    {
        this.channel = channel;
    }

    public boolean isFamilyChat ()
    {
        return familyChat;
    }

    public void setFamilyChat (boolean familyChat)
    {
        this.familyChat = familyChat;
    }

    public boolean isCanForwardMessage ()
    {
        return canForwardMessage;
    }

    public void setCanForwardMessage (boolean canForwardMessage)
    {
        this.canForwardMessage = canForwardMessage;
    }

    public boolean isShowMember ()
    {
        return showMember;
    }

    public void setShowMember (boolean showMember)
    {
        this.showMember = showMember;
    }

    public boolean isShowInSearch ()
    {
        return showInSearch;
    }

    public void setShowInSearch (boolean showInSearch)
    {
        this.showInSearch = showInSearch;
    }

    public Integer getMaxMember ()
    {
        return maxMember;
    }

    public void setMaxMember (Integer maxMember)
    {
        this.maxMember = maxMember;
    }
}
