package com.bardiademon.CyrusMessenger.bardiademon.IO;

import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import java.awt.image.BufferedImage;
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

    private final List <String> lstExtensions = Arrays.asList ("jpg" , "png");
    private String extension;

    private int width, height;

    public boolean valid (MultipartFile File)
    {
        try
        {
            width = 0;
            height = 0;

            if (File == null) throw new IOException ("Null variable");

            this.stream = File.getInputStream ();
            this.extension = FilenameUtils.getExtension (File.getOriginalFilename ());
            return checkExtensions () && check ();
        }
        catch (IOException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e);
        }
        return false;
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
            BufferedImage image = ImageIO.read (stream);
            if (image != null)
            {
                width = image.getWidth ();
                height = image.getHeight ();

                return true;
            }
            else throw new IOException ();
        }
        catch (IOException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e);
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

    public int getWidth ()
    {
        return width;
    }

    public int getHeight ()
    {
        return height;
    }

}

