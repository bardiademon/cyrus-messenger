package com.bardiademon.CyrusMessenger.bardiademon.Default;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.stream.IntStream;

public abstract class Path
{

    private static final String PROJECT = System.getProperty ("user.dir");
    private static final String FILES = StickTogether (PROJECT , "files");
    public static final String IMAGES = StickTogether (FILES , "Default" , "Images");

    public static final String COVER_USER = StickTogether (FILES , "Users" , "Cover");
    public static final String TEXT_TO_IMAGE = StickTogether (FILES , "TextToImage");

    public static final String IMAGE_NOT_FOUND = StickTogether (IMAGES , "ImageNotFound.png");
    public static final String IC_NOT_LOGIN = StickTogether (IMAGES , "ic_not_login.png");
    public static final String IC_NO_COVER = StickTogether (IMAGES , "ic_no_cover.png");

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
