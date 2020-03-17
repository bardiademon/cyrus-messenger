package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.Groups.PlacementNumber;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlacementNumberGroup
{
    @JsonProperty ("placement_number")
    private int placementNumber;
    private String id;

    private boolean main;

    @JsonProperty("update_main")
    private boolean updateMain;

    public PlacementNumberGroup ()
    {
    }

    public int getPlacementNumber ()
    {
        return placementNumber;
    }

    public void setPlacementNumber (int placementNumber)
    {
        this.placementNumber = placementNumber;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public boolean isMain ()
    {
        return main;
    }

    public void setMain (boolean main)
    {
        this.main = main;
    }

    public boolean isUpdateMain ()
    {
        return updateMain;
    }

    public void setUpdateMain (boolean updateMain)
    {
        this.updateMain = updateMain;
    }
}
