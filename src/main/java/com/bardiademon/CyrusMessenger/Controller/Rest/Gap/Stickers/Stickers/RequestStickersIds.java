package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Stickers.Stickers;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestStickersIds
{

    @JsonProperty ("id_stickers_group")
    private long idStickersGroup;

    @JsonProperty ("group_id")
    private long groupId;

    private String groupname;

    private String type;

    public RequestStickersIds ()
    {
    }

    public long getIdStickersGroup ()
    {
        return idStickersGroup;
    }

    public void setIdStickersGroup (long idStickersGroup)
    {
        this.idStickersGroup = idStickersGroup;
    }

    public long getGroupId ()
    {
        return groupId;
    }

    public void setGroupId (long id)
    {
        this.groupId = id;
    }

    public String getGroupname ()
    {
        return groupname;
    }

    public void setGroupname (String username)
    {
        this.groupname = username;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }
}
