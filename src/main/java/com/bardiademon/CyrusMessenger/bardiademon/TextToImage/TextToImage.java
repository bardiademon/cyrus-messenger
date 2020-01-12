package com.bardiademon.CyrusMessenger.bardiademon.TextToImage;


import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.bardiademon.Default.Path;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public final class TextToImage
{
    private String path;

    private String text;

    private boolean converted;

    public TextToImage (String Text)
    {
        this.text = Text;
        convert ();
    }

    private void convert ()
    {
        Font font = new Font ("arial" , Font.BOLD , 50);

        BufferedImage img = new BufferedImage (500 , 500 , BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics ();

        g2d.setFont (font);
        FontMetrics fm = g2d.getFontMetrics ();

        int width = fm.stringWidth (text);
        int height = fm.getHeight ();
        g2d.dispose ();

        img = new BufferedImage (width , height , BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics ();
        g2d.setFont (font);
        fm = g2d.getFontMetrics ();
        g2d.setColor (Color.WHITE);
        g2d.drawString (text , 0 , fm.getAscent ());
        g2d.dispose ();
        try
        {
            path = Path.TEXT_TO_IMAGE + (Code.Name ()) + ".png";
            ImageIO.write (img , "png" , new File (path));
            converted = (new File (path).exists ());
        }
        catch (IOException ignored)
        {
        }
    }

    public String getPath ()
    {
        return path;
    }

    public byte[] getByte ()
    {
        try
        {
            IOUtils.toByteArray (new FileInputStream (getPath ()));
        }
        catch (IOException ignored)
        {
        }
        return null;
    }

    public boolean isConverted ()
    {
        return converted;
    }

}
