package com.bardiademon.CyrusMessenger.bardiademon.IO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public final class CheckImage
{

    private InputStream stream;

    private boolean isImage;

    private final List<String> lstExtensions = Arrays.asList ("jpg" , "png");
    private String extension;

    public boolean valid (MultipartFile File)
    {
        try
        {
            this.stream = File.getInputStream ();
            this.extension = FilenameUtils.getExtension (File.getOriginalFilename ());
            isImage = checkExtensions () && check ();
        }
        catch (IOException e)
        {
            isImage = false;
        }
        return isImage;
    }

    private boolean checkExtensions ()
    {
        for (String extension : lstExtensions)
            if (extension.equals (this.extension)) return true;
        return false;
    }

    private boolean check ()
    {
        try
        {
            return ((ImageIO.read (stream)) != null);
        }
        catch (IOException ignored)
        {
        }
        return false;
    }

    public boolean isImage ()
    {
        return isImage;
    }

    public String getExtension ()
    {
        return extension;
    }
}

