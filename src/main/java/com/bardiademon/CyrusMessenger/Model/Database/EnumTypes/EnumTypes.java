package com.bardiademon.CyrusMessenger.Model.Database.EnumTypes;

import com.bardiademon.CyrusMessenger.Model.Database.ProfilePictures.ProfilePictures;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import java.time.LocalDateTime;

@Entity
@Table (name = "enum_type")
public final class EnumTypes
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Column (name = "id_2", nullable = false)
    private long id2;

    @Column (name = "enum_type", nullable = false)
    private String enumType;

    @Column (name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column (name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;

    private boolean deleted;

    private String des;

    public EnumTypes ()
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

    public long getId2 ()
    {
        return id2;
    }

    public void setId2 (long id2)
    {
        this.id2 = id2;
    }

    public String getEnumType ()
    {
        return enumType;
    }

    public void setEnumType (String enumType)
    {
        this.enumType = enumType;
    }

    public LocalDateTime getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted ()
    {
        return deleted;
    }

    public void setDeleted (boolean deleted)
    {
        this.deleted = deleted;
    }

    public String getDes ()
    {
        return des;
    }

    public void setDes (String des)
    {
        this.des = des;
    }
}
