package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityGap.ValidFilesGroups;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.GroupSecurityGap.GroupSecurityGap;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

/*
 * in table types haye ke tavasot management group add mishe nega midam ke user ha faghat betonan in type ha ro toye group send konan
 */
@Entity
@Table (name = "valid_files_gaps")
public final class ValidFilesGroups
{
    @Id
    @GeneratedValue
    private long id;

    /*
     * type => pasvand file ha {.exe,.txt,...}
     */
    private String type;

    /*
     * in type ha baraye group ha hast va jozv security groupgap hesab mishe
     */
    @ManyToOne
    @JoinColumn (name = "group_security_id", referencedColumnName = "id")
    private GroupSecurityGap groupSecurityGap;

    /*
     * che timei in type add shode
     */
    @Column (name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /*
     * baraye in ke sabt beshe ke che useri esafe karde ino
     */
    @ManyToOne
    @JoinColumn (name = "added_by", referencedColumnName = "id")
    private MainAccount addedBy;

    /*
     * aya delete shode ya no
     *
     * insertable = false baraye ine ke time add kadrdan in meghdar dehi nashe
     */
    @Column (insertable = false)
    private boolean deleted;

    /*
     * agar delete shode che timei delete shode
     *
     * insertable = false baraye ine ke time add kadrdan in meghdar dehi nashe
     */
    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    public ValidFilesGroups ()
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

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public GroupSecurityGap getGroupSecurityGap ()
    {
        return groupSecurityGap;
    }

    public void setGroupSecurityGap (GroupSecurityGap groupSecurityGap)
    {
        this.groupSecurityGap = groupSecurityGap;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public MainAccount getAddedBy ()
    {
        return addedBy;
    }

    public void setAddedBy (MainAccount addedBy)
    {
        this.addedBy = addedBy;
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
}
