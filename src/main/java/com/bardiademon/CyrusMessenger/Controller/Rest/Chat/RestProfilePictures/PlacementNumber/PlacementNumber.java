package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.PlacementNumber;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlacementNumber
{
    @JsonProperty ("placement_number")
    private int placementNumber;
    private String id;

    private boolean main;

    @JsonProperty("update_main")
    private boolean updateMain;

    public PlacementNumber ()
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

    public long getId ()
    {
        return Long.parseLong (id);
    }

    public void setId (long id)
    {
        this.id = String.valueOf (id);
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
