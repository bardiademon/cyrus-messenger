package com.bardiademon.CyrusMessenger.bardiademon.IO.UploadFile;

import com.bardiademon.CyrusMessenger.Code;
import com.bardiademon.CyrusMessenger.Model.Database.UploadedFiles.UploadedFiles;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public final class UploadFile
{

    public UploadedFiles upload (final MultipartFile multipartFile , final String saveTo)
    {
        return upload (multipartFile , saveTo , 0);
    }

    private UploadedFiles upload (final MultipartFile multipartFile , final String saveTo , int counter)
    {
        try
        {
            final File saveToFile = new File (saveTo);
            if (saveToFile.exists () || saveToFile.mkdirs ())
            {
                final String type = FilenameUtils.getExtension (multipartFile.getOriginalFilename ());
                final String name = Code.Name ();
                final File path = new File (String.format ("%s/%s.%s" , saveTo , name , type));
                if (path.exists ())
                {
                    if (counter >= 10) return null;
                    else return upload (multipartFile , saveTo , ++counter);
                }
                else
                {
                    write (multipartFile.getInputStream () , new FileOutputStream (path));

                    final UploadedFiles file = new UploadedFiles ();

                    file.setName (name);
                    file.setType (type);
                    file.setSize (multipartFile.getSize ());
                    file.setContentType (multipartFile.getContentType ());
                    file.setSavedPath (path.getParent ());
                    file.setUploadedAt (LocalDateTime.now ());

                    return file;
                }
            }
            else throw new IOException ("Can not create dir " + saveToFile.getPath ());
        }
        catch (IOException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e);
            return null;
        }
    }

    private void write (final InputStream inputStream , final OutputStream outputStream) throws IOException
    {
        int len;
        final byte[] buffer = new byte[1024 * 1024];
        while ((len = inputStream.read (buffer)) > 0) outputStream.write (buffer , 0 , len);
    }

}




