package com.bardiademon.CyrusMessenger.bardiademon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ID
{

    @JsonProperty ("id")
    private final Object idObj;

    @JsonIgnore
    private long id;

    @JsonIgnore
    private boolean valid;

    @JsonCreator
    public ID (final Object ID)
    {
        this.idObj = ID;
        validation ();
    }

    @JsonIgnore
    private void validation ()
    {
        if (idObj == null) valid = false;
        else if (idObj instanceof Integer || idObj instanceof Long) toLong ();
        else
        {
            final String idStr = idObj.toString ();
            if (!Str.IsEmpty (idStr) && idStr.matches ("[0-9]*")) toLong ();
        }
    }

    @JsonIgnore
    private void toLong ()
    {
        id = Long.parseLong (idObj.toString ());
        valid = (id > 0);
    }

    public long getId ()
    {
        return id;
    }

    @JsonIgnore
    public boolean isValid ()
    {
        return valid;
    }

    @JsonIgnore
    public Object getIdObj ()
    {
        return idObj;
    }
}
