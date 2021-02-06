package com.bardiademon.CyrusMessenger.ServerSocket.Gap;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RequestGap extends PublicRequest
{
    /**
     * agar payam toye personal gap ersal shode in barabar ba UserId ast
     * ama agar groups bashe in barabar ba GroupId ast
     */
    private long to;

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

    @JsonProperty ("personal_gap")
    private boolean personalGap;

    /*
     * baraye ke agar gap forward shode bod
     */
    @JsonProperty ("gap_id")
    private long gapId;

    private boolean comment;

    @JsonProperty ("gap_text_type")
    private String gapTextType;

    /**
     * <h1>in baraye in ast ke:</h1>
     * <p>
     * gap momkene dobare ersal beshe (forward) pas dakhel darkhast gapId agar bod ba check kardan gapId ,
     * gap meghdar dehi mishe
     * baraye in gozashtam inja ke be service gap faghat in class (RequestGap) ro baraye zakhire midam ,
     * va vazife barasi in darkhast (RequestGap) ro be class dige midam
     * </p>
     */
    @JsonIgnore
    private Gaps gaps;

    @JsonProperty ("personal_gap_id")
    private long personalGapId;

    public RequestGap ()
    {
    }

    public long getTo ()
    {
        return to;
    }

    public void setTo (long to)
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

    public long getGapId ()
    {
        return gapId;
    }

    public void setGapId (long gapId)
    {
        this.gapId = gapId;
    }

    public boolean isComment ()
    {
        return comment;
    }

    public void setComment (boolean comment)
    {
        this.comment = comment;
    }

    public String getGapTextType ()
    {
        return gapTextType;
    }

    public void setGapTextType (String gapTextType)
    {
        this.gapTextType = gapTextType;
    }

    public Gaps getGaps ()
    {
        return gaps;
    }

    public void setGaps (Gaps gaps)
    {
        this.gaps = gaps;
    }

    public boolean isPersonalGap ()
    {
        return personalGap;
    }

    public void setPersonalGap (boolean personalGap)
    {
        this.personalGap = personalGap;
    }

    public long getPersonalGapId ()
    {
        return personalGapId;
    }

    public void setPersonalGapId (long personalGapId)
    {
        this.personalGapId = personalGapId;
    }
}
