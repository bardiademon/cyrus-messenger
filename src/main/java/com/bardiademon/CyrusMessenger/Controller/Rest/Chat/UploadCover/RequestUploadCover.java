package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.UploadCover;

import org.springframework.web.multipart.MultipartFile;

public final class RequestUploadCover
{
    private MultipartFile cover;

    private boolean replace;

    public RequestUploadCover ()
    {
    }

    public RequestUploadCover (MultipartFile cover , boolean replace)
    {
        this.cover = cover;
        this.replace = replace;
    }

    public MultipartFile getCover ()
    {
        return cover;
    }

    public void setCover (MultipartFile cover)
    {
        this.cover = cover;
    }

    public boolean isReplace ()
    {
        return replace;
    }

    public void setReplace (boolean replace)
    {
        this.replace = replace;
    }
}
