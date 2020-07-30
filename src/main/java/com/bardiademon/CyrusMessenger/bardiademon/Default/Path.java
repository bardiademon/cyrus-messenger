package com.bardiademon.CyrusMessenger.bardiademon.Default;

import java.io.File;
import java.util.stream.IntStream;
import org.apache.commons.io.FilenameUtils;

public abstract class Path
{

    private static final String PROJECT = System.getProperty ("user.dir");
    private static final String FILES = StickTogether (PROJECT , "files");
    public static final String IMAGES = StickTogether (FILES , "Default" , "Images");

    public static final String PROFILE_PICTURES = StickTogether (FILES , "ProfilePictures");
    public static final String PROFILE_PICTURES_USERS = StickTogether (PROFILE_PICTURES , "Users");
    public static final String PROFILE_PICTURES_GROUPS = StickTogether (PROFILE_PICTURES , "Groups");
    public static final String PROFILE_PICTURES_CHANNELS = StickTogether (PROFILE_PICTURES , "Channels");

    public static final String COVER_USER = StickTogether (FILES , "Users" , "Cover");
    public static final String TEXT_TO_IMAGE = StickTogether (FILES , "TextToImage");

    public static final String IMAGE_NOT_FOUND = "ImageNotFound.png";
    public static final String IC_NOT_LOGGED = "ic_not_logged.png";

    public static final String IC_COVER_DEFAULT = "ic_cover_default.png";
    public static final String IC_COVER_MAN = "ic_cover_man.png";
    public static final String IC_COVER_WOMAN = "ic_cover_woman.png";
    public static final String IC_COVER_BISEXUAL = "ic_cover_bisexual.png";
    public static final String IC_DO_NOT_WANT = "ic_do_not_want.png";

    public static final String IMAGE_ERROR_500 = "error_500.png";

    public static String GetImage (String Name)
    {
        return StickTogether (IMAGES , Name);
    }

    public static String StickTogether (String... Path)
    {
        StringBuilder NewPath;
        NewPath = new StringBuilder ();
        IntStream.range (0 , Path.length).forEachOrdered (i ->
        {
            String p = Path[i];
            if (((i + 1) < (Path.length - 1)) && Path[i + 1].equals (File.separator)) return;
            NewPath.append (p);
            String extension = FilenameUtils.getExtension (p);
            if (extension == null || extension.equals ("")) NewPath.append (File.separator);
        });
        return NewPath.toString ().replace (File.separator + File.separator , File.separator);
    }

    public static File StickTogetherFile (String... Path)
    {
        return new File (StickTogether (Path));
    }
}
