package com.bardiademon.CyrusMessenger.Model.Database.Default;

import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table (name = "dflt")
public final class Default
{
    @Id
    @GeneratedValue
    @Column (nullable = false, unique = true)
    private long id;

    @Column (name = "ky", nullable = false, unique = true)
    @Enumerated (EnumType.STRING)
    private DefaultKey key;

    @Column (name = "vlu", nullable = false)
    private String value;

    @Column (nullable = false, name = "type_value")
    @Enumerated (EnumType.STRING)
    private DefaultType typeValue;

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public DefaultKey getKey ()
    {
        return key;
    }

    public void setKey (DefaultKey key)
    {
        this.key = key;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public DefaultType getTypeValue ()
    {
        return typeValue;
    }

    public void setTypeValue (DefaultType typeValue)
    {
        this.typeValue = typeValue;
    }

    @Transient
    @JsonIgnore
    @Override
    public String toString ()
    {
        return ToJson.To (this);
    }
}
