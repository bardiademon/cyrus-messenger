package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures.DeleteProfilePicture;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestDeleteProfilePicture
{
    private String which;

    @JsonProperty ("delete_main_pic")
    private boolean deleteMainPic;

    @JsonProperty ("id_profile_picture")
    private long idProfilePicture;

    public String getWhich ()
    {
        return which;
    }

    public void setWhich (String which)
    {
        this.which = which;
    }

    public long getIdProfilePicture ()
    {
        return idProfilePicture;
    }

    public void setIdProfilePicture (long idProfilePicture)
    {
        this.idProfilePicture = idProfilePicture;
    }

    public boolean isDeleteMainPic ()
    {
        return deleteMainPic;
    }

    public void setDeleteMainPic (boolean deleteMainPic)
    {
        this.deleteMainPic = deleteMainPic;
    }

    public enum Which
    {
        del_one, del_all, del_placement_number_zero, del_placement_number_not_zero, del_main;

        public static Which to (String which)
        {
            try
            {
                if (which == null || which.isEmpty ()) throw new Exception ("is_empty");
                return valueOf (which);
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

}
