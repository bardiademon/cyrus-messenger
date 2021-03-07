package com.bardiademon.CyrusMessenger.bardiademon;

import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public final class InfoLine
{

    @JsonIgnore
    private StackTraceElement[] stackTrace;

    @JsonProperty ("info_line")
    private StackTraceElement infoLine;

    @JsonIgnore
    private boolean wasGet;

    private final Exception e;

    private Map <String, String> exception;

    public InfoLine (Exception E , StackTraceElement[] StackTrace)
    {
        this.e = E;
        this.stackTrace = StackTrace;
        getInfo ();
    }

    public InfoLine (Exception E , StackTraceElement StackTrace)
    {
        this.e = E;
        this.infoLine = StackTrace;
        getInfo ();
    }

    private void getInfo ()
    {
        try
        {
            if (infoLine == null) infoLine = stackTrace[1];
            wasGet = (infoLine != null);

            if (e != null)
            {
                exception = new LinkedHashMap <> ();
                exception.put ("message" , e.getMessage ());
                try
                {
                    exception.put ("stack_trace_element" , new InfoLine (null , e.getStackTrace ()[0]).toString ());
                }
                catch (NullPointerException | ArrayIndexOutOfBoundsException e)
                {
                    l.n (Thread.currentThread ().getStackTrace () , e , InfoLine.class.getName ());
                }
                exception.put ("localized_message" , e.getLocalizedMessage ());
            }
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , InfoLine.class.getName ());
            wasGet = false;
        }
    }

    @JsonIgnore
    private boolean wasGet ()
    {
        return wasGet;
    }

    @JsonProperty ("info_line")
    public StackTraceElement getInfoLine ()
    {
        return infoLine;
    }

    @Override
    public String toString ()
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper ();
            return objectMapper.writeValueAsString (this);
        }
        catch (JsonProcessingException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , InfoLine.class.getName ());
            return null;
        }
    }

    @JsonProperty ("exception")
    public Map <String, String> getException ()
    {
        return exception;
    }
}
