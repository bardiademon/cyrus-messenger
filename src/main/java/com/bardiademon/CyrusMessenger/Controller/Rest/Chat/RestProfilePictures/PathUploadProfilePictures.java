package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.RestProfilePictures;

import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;

import java.io.File;

public final class PathUploadProfilePictures
{
    private PathUploadProfilePictures ()
    {
    }

    public static String User (long id , String name , String type)
    {
        return Path.StickTogether (User (id) , name + "." + type);
    }

    public static String User (long id)
    {
        String uploadImage = Path.StickTogether (Path.PROFILE_PICTURES_USERS , String.valueOf (id));
        File file = new File (uploadImage);
        if (file.exists () || file.mkdirs ()) return file.getPath ();
        else return null;
    }
}
