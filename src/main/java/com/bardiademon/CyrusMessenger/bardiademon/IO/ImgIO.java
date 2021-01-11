package com.bardiademon.CyrusMessenger.bardiademon.IO;

import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;

public final class ImgIO
{

    public BufferedImage read (final File image)
    {
        try
        {
            return ImageIO.read (image);
        }
        catch (IOException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , image.getPath ());
            return null;
        }
    }

    public BufferedImage resize (final BufferedImage image , final int width , final int height)
    {
        final BufferedImage newImage = new BufferedImage (width , height , BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics = newImage.createGraphics ();
        graphics.drawImage (image.getScaledInstance (width , height , BufferedImage.TYPE_INT_RGB) , 0 , 0 , null);
        graphics.dispose ();
        return newImage;
    }

    public InputStreamResource toInputStreamResource (final BufferedImage image , final String name , final HttpServletResponse res)
    {
        try
        {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream ();
            ImageIO.write (image , FilenameUtils.getExtension (name) , outputStream);
            return toInputStreamResource (new ByteArrayInputStream (outputStream.toByteArray ()) , name , res);
        }
        catch (IOException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , name);
            return null;
        }
    }

    public InputStreamResource toInputStreamResource (final File image , final String name , final HttpServletResponse res)
    {
        try
        {
            return toInputStreamResource (new FileInputStream (image) , name , res);
        }
        catch (FileNotFoundException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , image.getPath ());
            return null;
        }
    }

    public InputStreamResource toInputStreamResource (final InputStream image , final String name , final HttpServletResponse res)
    {
        final String fileName = URLDecoder.decode (URLEncoder.encode (name , StandardCharsets.UTF_8) , StandardCharsets.ISO_8859_1);
        res.setContentType ("application/x-msdownload");
        res.setHeader ("Content-disposition" , "attachment; filename=" + fileName);
        return new InputStreamResource (image);
    }
}
