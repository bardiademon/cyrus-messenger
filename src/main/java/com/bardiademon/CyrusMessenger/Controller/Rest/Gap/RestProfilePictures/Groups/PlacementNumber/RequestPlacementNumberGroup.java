package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.Groups.PlacementNumber;

import com.bardiademon.CyrusMessenger.Controller.Rest.Gap.RestProfilePictures.PlacementNumber.PlacementNumber;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class RequestPlacementNumberGroup
{

    @JsonProperty ("id_group")
    private ID idGroup;

    @JsonProperty ("info")
    private List<PlacementNumber> placementNumber;

    public RequestPlacementNumberGroup ()
    {
    }

    public List<PlacementNumber> getPlacementNumber ()
    {
        return placementNumber;
    }

    public void setPlacementNumber (List<PlacementNumber> placementNumber)
    {
        this.placementNumber = placementNumber;
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
