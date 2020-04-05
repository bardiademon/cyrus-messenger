package com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures;

import com.bardiademon.CyrusMessenger.Model.Database.Channel.Channel.Channel.Channel;
import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.EnumTypes;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "profile_pictures")
public final class ProfilePictures
{
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @JsonIgnore
    private MainAccount mainAccount;

    @ManyToOne
    @JoinColumn(name = "id_group", referencedColumnName = "id")
    private Groups groups;

    @ManyToOne
    @JoinColumn(name = "id_channel", referencedColumnName = "id")
    private Channel channel;

    private String name;

    private String type;

    private long size;

    private boolean deleted;

    @Column(name = "main_pic", nullable = false)
    private boolean mainPic = false;

    @Column(name = "placement_number", nullable = false)
    private int placementNumber = 0;

    @Column(name = "this_pic_for", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfilePicFor thisPicFor;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @Column(name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    @Column(name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean separate = false;

    @Column(name = "separate_for")
    @OneToMany(mappedBy = "id2")
    @Where(clause = "`deleted` = false and `separate` = true")
    private List <EnumTypes> separateFor = null;

    public ProfilePictures ()
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

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }

    public boolean isMainPic ()
    {
        return mainPic;
    }

    public void setMainPic (boolean mainPic)
    {
        this.mainPic = mainPic;
    }

    public int getPlacementNumber ()
    {
        return placementNumber;
    }

    public void setPlacementNumber (int placementNumber)
    {
        this.placementNumber = Math.max (placementNumber , 0);
    }

    public ProfilePicFor getThisPicFor ()
    {
        return thisPicFor;
    }

    public void setThisPicFor (ProfilePicFor thisPicFor)
    {
        this.thisPicFor = thisPicFor;
    }

    public LocalDateTime getUploadedAt ()
    {
        return uploadedAt;
    }

    public void setUploadedAt (LocalDateTime uploadedAt)
    {
        this.uploadedAt = uploadedAt;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public LocalDateTime getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public long getSize ()
    {
        return size;
    }

    public void setSize (long size)
    {
        this.size = size;
    }

    public boolean isSeparate ()
    {
        return separate;
    }

    public void setSeparate (boolean separate)
    {
        this.separate = separate;
    }

    public List <EnumTypes> getSeparateFor ()
    {
        return separateFor;
    }

    public void setSeparateFor (List <EnumTypes> separateFor)
    {
        this.separateFor = separateFor;
    }
}
