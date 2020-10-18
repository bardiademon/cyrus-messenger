package com.bardiademon.CyrusMessenger.bardiademon.IO;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.GetSize;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import java.awt.image.BufferedImage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private long size;

    public boolean valid (MultipartFile Image)
    {
        try
        {
            width = 0;
            height = 0;

            if (Image == null) throw new IOException ("Null variable");

            this.size = Image.getSize ();

            this.stream = Image.getInputStream ();
            this.extension = FilenameUtils.getExtension (Image.getOriginalFilename ());
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

    /**
     * C Check , S => Size  , W => Width , H => Height
     */
    public AnswerToClient CSWH (HttpServletRequest req , HttpServletResponse res , String request , String router , MainAccount mainAccount , SubmitRequestType type , DefaultKey keySize , DefaultKey keyMinWidth , DefaultKey keyMaxWidth , DefaultKey keyMinHeight , DefaultKey keyMaxHeight , DefaultService defaultService)
    {
        AnswerToClient answer;
        Long maxSize = defaultService.getLong (keySize);
        if (maxSize != null)
        {
            if (this.getSize () <= maxSize)
            {
                Integer minWidth, maxWidth, minHeight, maxHeight;

                if ((minWidth = defaultService.getInt (keyMinWidth)) != null
                        && (maxWidth = defaultService.getInt (keyMaxWidth)) != null
                        && (minHeight = defaultService.getInt (keyMinHeight)) != null
                        && (maxHeight = defaultService.getInt (keyMaxHeight)) != null)
                {
                    int imageWidth = this.getWidth ();
                    int imageHeight = this.getHeight ();
                    if ((imageWidth >= minWidth && imageWidth <= maxWidth) && (imageHeight >= minHeight && imageHeight <= maxHeight))
                    {
                        l.n (Thread.currentThread ().getStackTrace () , "Check size & width & height");
                        return null;
                    }
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.invalid_width_or_height.name ());

                        answer.put (AnswerToClient.CUK.acceptable_width_height.name () ,

                                ToJson.CreateClass.n (AnswerToClient.CUK.min_width.name () , minWidth)
                                        .put (AnswerToClient.CUK.max_width.name () , maxWidth)
                                        .put (AnswerToClient.CUK.min_width.name () , minWidth)
                                        .put (AnswerToClient.CUK.min_height.name () , minHeight)
                                        .put (AnswerToClient.CUK.max_height.name () , maxHeight).getCreateClass ())

                                .put (KeyAnswer.your_image_width_height.name () ,
                                        ToJson.CreateClass.n (AnswerToClient.CUK.width.name () , imageWidth)
                                                .put (AnswerToClient.CUK.height.name () , imageHeight).getCreateClass ());

                        answer.setReqRes (req , res);
                        l.n (request , router , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.invalid_width_or_height.name ()) , null , type , true);
                    }
                }
                else
                {
                    answer = AnswerToClient.ServerError ();
                    answer.setReqRes (req , res);
                    l.n (request , router , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null , type , true);
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.the_size_of_the_image_is_large.name ());
                answer.put (AnswerToClient.CUK.acceptable_size.name () , GetSize.Get (maxSize));
                answer.put (KeyAnswer.your_image_size.name () , GetSize.Get (this.getSize ()));
                answer.put (AnswerToClient.CUK.extra_size.name () , GetSize.Get ((this.getSize () - maxSize)));
                answer.setReqRes (req , res);
                l.n (request , router , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.the_size_of_the_image_is_large.name ()) , null , type , true);
            }
        }
        else
        {
            answer = AnswerToClient.ServerError ();
            answer.setReqRes (req , res);
            l.n (request , router , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , DefaultKey.max_size_sticker.name () , type , true);
        }
        return answer;
    }

    private enum KeyAnswer
    {
        your_image_size, your_image_width_height
    }

    private enum ValAnswer
    {
        the_size_of_the_image_is_large
    }

    public boolean isImage ()
    {
        return isImage;
    }

    public long getSize ()
    {
        return size;
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

