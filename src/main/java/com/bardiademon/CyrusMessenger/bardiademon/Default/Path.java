package com.bardiademon.CyrusMessenger.bardiademon.Default;

import com.bardiademon.CyrusMessenger.bardiademon.Str;
import java.io.File;
import org.apache.commons.io.FilenameUtils;

public abstract class Path
{

    private static final String PROJECT = System.getProperty ("user.dir");
    private static final String FILES = StickTogether (PROJECT , "files");
    public static final String IMAGES = StickTogether (FILES , "Default" , "Images");

    public static final String PROFILE_PICTURES = StickTogether (FILES , "ProfilePictures");
    public static final String GAP_FILES = StickTogether (FILES , "GapFiles");
    public static final String STICKERS = StickTogether (FILES , "Stickers");
    public static final String STICKERS_GROUPS = StickTogether (STICKERS , "Groups");
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

    public static String StickerGroups (long idMainAccount)
    {
        File file = new File (StickTogether (STICKERS_GROUPS , String.valueOf (idMainAccount)));
        if (!file.exists ()) file.mkdirs ();
        return file.getPath ();
    }

    public static String StickTogether (String... Path)
    {
        return StickTogether (null , Path);
    }

    public static String StickTogether (String Type , String[] Path)
    {
        StringBuilder NewPath;
        NewPath = new StringBuilder ();
        for (int i = 0, len = Path.length; i < len; i++)
        {
            String p = Path[i];
            if (((i + 1) < (Path.length - 1)) && Path[i + 1].equals (File.separator)) continue;
            NewPath.append (p);
            String extension = FilenameUtils.getExtension (p);
            if (extension == null || extension.equals ("")) NewPath.append (File.separator);
        }
        String replace = NewPath.toString ().replace (File.separator + File.separator , File.separator);
        if (!Str.IsEmpty (Type))
        {
            if (replace.substring (replace.length () - 1).equals (File.separator))
                replace = replace.substring (0 , replace.length () - 1);
            replace += "." + Type;
        }

        return replace;
    }

    public static File StickTogetherFile (String... Path)
    {
        return new File (StickTogether (Path));
    }
}
