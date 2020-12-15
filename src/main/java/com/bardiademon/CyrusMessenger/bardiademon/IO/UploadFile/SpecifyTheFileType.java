package com.bardiademon.CyrusMessenger.bardiademon.IO.UploadFile;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapFilesTypes;
import com.bardiademon.CyrusMessenger.bardiademon.IO.CheckImage;
import org.springframework.web.multipart.MultipartFile;

public final class SpecifyTheFileType
{
    private final CheckImage checkImage = new CheckImage ();

    public GapFilesTypes specify (final MultipartFile file , final boolean isVoice)
    {
        String contentType = file.getContentType ();
        if (contentType != null)
        {
            if (isVoice)
                if (contentType.contains ("audio")) return GapFilesTypes.voice;

            if (!isVoice)
            {
                if (contentType.contains ("image"))
                {
                    if (contentType.contains ("gif")) return GapFilesTypes.gif;
                    else
                    {
                        if (checkImage.valid (file)) return GapFilesTypes.image;
                    }
                }
                else if (contentType.contains ("video"))
                    return GapFilesTypes.video;
                else if (contentType.contains ("audio"))
                    return GapFilesTypes.audio;
            }
        }
        return GapFilesTypes.file;
    }
}
