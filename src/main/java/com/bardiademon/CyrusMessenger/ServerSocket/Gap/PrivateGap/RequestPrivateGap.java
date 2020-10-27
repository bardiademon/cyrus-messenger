package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public final class RequestPrivateGap extends PublicRequest
{
    @JsonProperty ("personal_gap_id")
    private long personalGapId;

    // to => username
    private String to;

    private String text;

    @JsonProperty ("file_code")
    private List <String> fileCode;

    @JsonProperty ("has_file")
    private boolean hasFile;

    // zamani ke payam ersal zade shode => ba ghat bodan internet dir ersal mishavad pas in zaman neshan dahande in ke key payam ersal shode
    @JsonProperty ("time_send")
    private long timeSend;

    @JsonProperty (value = "reply")
    private String idStrReplyChat;

    public RequestPrivateGap ()
    {
    }

    public long getPersonalGapId ()
    {
        return personalGapId;
    }

    public void setPersonalGapId (long personalGapId)
    {
        this.personalGapId = personalGapId;
    }

    public String getTo ()
    {
        return to;
    }

    public void setTo (String to)
    {
        this.to = to;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public List <String> getFileCode ()
    {
        return fileCode;
    }

    public void setFileCode (List <String> fileCode)
    {
        this.fileCode = fileCode;
    }

    public boolean isHasFile ()
    {
        return hasFile;
    }

    public void setHasFile (boolean hasFile)
    {
        this.hasFile = hasFile;
    }

    public long getTimeSend ()
    {
        return timeSend;
    }

    public void setTimeSend (long timeSend)
    {
        this.timeSend = timeSend;
    }

    public String getIdStrReplyChat ()
    {
        return idStrReplyChat;
    }

    public void setIdStrReplyChat (String idStrReplyChat)
    {
        this.idStrReplyChat = idStrReplyChat;
    }
}
