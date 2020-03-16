package com.bardiademon.CyrusMessenger.bardiademon.IO;

import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class ToByte
{
    private ToByte ()
    {
    }

    public static byte[] to (String path)
    {
        try
        {
            return IOUtils.toByteArray (new FileInputStream (new File (path)));
        }
        catch (IOException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , path);
            try
            {
                return IOUtils.toByteArray (new FileInputStream (new File (Path.IMAGE_ERROR_500)));
            }
            catch (IOException e2)
            {
                l.n (Thread.currentThread ().getStackTrace () , e2 , path);
            }
        }
        return null;
    }
}
