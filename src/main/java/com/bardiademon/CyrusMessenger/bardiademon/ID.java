package com.bardiademon.CyrusMessenger.bardiademon;

public final class ID
{
    private final Object idObj;

    private long id;

    private boolean valid;

    public ID (Object ID)
    {
        this.idObj = ID;
        validation ();
    }

    private void validation ()
    {
        if (idObj == null) valid = false;
        else if (idObj instanceof Integer || idObj instanceof Long) toLong ();
        else
        {
            String idStr = idObj.toString ();
            if (!Str.IsEmpty (idStr) && idStr.matches ("[0-9]*")) toLong ();
        }
    }

    private void toLong ()
    {
        id = Long.parseLong (idObj.toString ());
        valid = (id > 0);
    }

    public long getId ()
    {
        return id;
    }

    public boolean isValid ()
    {
        return valid;
    }
}
