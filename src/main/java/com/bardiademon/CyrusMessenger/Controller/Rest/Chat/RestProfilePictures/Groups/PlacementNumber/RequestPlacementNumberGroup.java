package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Groups.PlacementNumber;

import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class RequestPlacementNumberGroup
{

    @JsonProperty ("id_group")
    private ID idGroup;

    @JsonProperty ("info")
    private List<PlacementNumberGroup> placementNumberGroup;

    public RequestPlacementNumberGroup ()
    {
    }

    public List<PlacementNumberGroup> getPlacementNumberGroup ()
    {
        return placementNumberGroup;
    }

    public void setPlacementNumberGroup (List<PlacementNumberGroup> placementNumberGroup)
    {
        this.placementNumberGroup = placementNumberGroup;
    }

    public ID getIdGroup ()
    {
        return idGroup;
    }

    public void setIdGroup (ID idGroup)
    {
        this.idGroup = idGroup;
    }
}
