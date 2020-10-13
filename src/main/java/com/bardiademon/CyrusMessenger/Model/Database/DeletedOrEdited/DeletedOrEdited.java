package com.bardiademon.CyrusMessenger.Model.Database.DeletedOrEdited;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table (name = "deleted_or_edited")
public final class DeletedOrEdited
{

    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Column (name = "tbname", nullable = false)
    private String tableName;

    @Column (name = "id_deleted")
    private long idDeleted;

    /**
     * value => meghdar haye hazf ya virayesh shode dar ghalebe json
     */
    @Column (name = "table_value", nullable = false, length = 1000000000)
    private String value;

    @Column (name = "deleted_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime deletedAt;

    @Column (name = "des")
    private String description;

    @ManyToOne
    @JoinColumn (name = "deleted_by", referencedColumnName = "id")
    private MainAccount deletedBy;

    /**
     * name classy ke in itelaat dar on hazf shode
     */
    @Column (name = "deleted_by_class")
    private String deletedByClass;

    /**
     * doe => DeletedOrEdited
     */
    @Column (name = "doe_type")
    @Enumerated (EnumType.STRING)
    private DeletedOrEditedType type;

    public DeletedOrEdited ()
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

    public String getTableName ()
    {
        return tableName;
    }

    public void setTableName (String tableName)
    {
        this.tableName = tableName;
    }

    public long getIdDeleted ()
    {
        return idDeleted;
    }

    public void setIdDeleted (long idDeleted)
    {
        this.idDeleted = idDeleted;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public LocalDateTime getDeletedAt ()
    {
        return deletedAt;
    }

    public void setDeletedAt (LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public MainAccount getDeletedBy ()
    {
        return deletedBy;
    }

    public void setDeletedBy (MainAccount deletedBy)
    {
        this.deletedBy = deletedBy;
    }

    public String getDeletedByClass ()
    {
        return deletedByClass;
    }

    public void setDeletedByClass (String deletedByClass)
    {
        this.deletedByClass = deletedByClass;
    }

    public DeletedOrEditedType getType ()
    {
        return type;
    }

    public void setType (DeletedOrEditedType type)
    {
        this.type = type;
    }
}
