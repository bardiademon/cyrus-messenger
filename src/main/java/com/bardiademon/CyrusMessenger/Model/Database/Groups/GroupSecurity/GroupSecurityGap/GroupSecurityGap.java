package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityGap;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityGap.ValidFilesGroups.ValidFilesGroups;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "group_security_gap")
public class GroupSecurityGap
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @OneToOne
    @JoinColumn (name = "id_group", referencedColumnName = "id")
    private Groups groups;

    @Column (name = "send_message", nullable = false)
    private boolean sendMessage = true;

    @Column (name = "send_emoji", nullable = false)
    private boolean sendEmoji = true;

    @Column (name = "send_sticker", nullable = false)
    private boolean sendSticker = true;

    @Column (name = "send_gif", nullable = false)
    private boolean sendGif = true;

    @Column (name = "send_voice", nullable = false)
    private boolean sendVoice = true;

    @Column (name = "send_file", nullable = false)
    private boolean sendFile = true;

    @Column (name = "send_link", nullable = false)
    private boolean sendLink = true;

    @OneToMany
    private List <ValidFilesGroups> validFilesGroups;

    public GroupSecurityGap ()
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

    public Groups getGroups ()
    {
        return groups;
    }

    public void setGroups (Groups groups)
    {
        this.groups = groups;
    }

    public boolean isSendMessage ()
    {
        return sendMessage;
    }

    public void setSendMessage (boolean sendMessage)
    {
        this.sendMessage = sendMessage;
    }

    public boolean isSendEmoji ()
    {
        return sendEmoji;
    }

    public void setSendEmoji (boolean sendEmoji)
    {
        this.sendEmoji = sendEmoji;
    }

    public boolean isSendSticker ()
    {
        return sendSticker;
    }

    public void setSendSticker (boolean sendSticker)
    {
        this.sendSticker = sendSticker;
    }

    public boolean isSendGif ()
    {
        return sendGif;
    }

    public void setSendGif (boolean sendGif)
    {
        this.sendGif = sendGif;
    }

    public boolean isSendVoice ()
    {
        return sendVoice;
    }

    public void setSendVoice (boolean sendVoice)
    {
        this.sendVoice = sendVoice;
    }

    public boolean isSendFile ()
    {
        return sendFile;
    }

    public void setSendFile (boolean sendFile)
    {
        this.sendFile = sendFile;
    }

    public boolean isSendLink ()
    {
        return sendLink;
    }

    public void setSendLink (boolean sendLink)
    {
        this.sendLink = sendLink;
    }
}
