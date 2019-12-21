package com.bardiademon.CyrusMessenger.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnswerToClient
{

    private int statusCode;
    private Map<String, Object> message;
    private boolean ok;


    public AnswerToClient (int StatusCode , boolean Ok)
    {
        this.statusCode = StatusCode;
        this.ok = Ok;
        message = new LinkedHashMap<> ();
    }

    public static AnswerToClient OK ()
    {
        return new AnswerToClient (200 , true);
    }

    public static AnswerToClient error400 ()
    {
        return new AnswerToClient (400 , false);
    }

    public static AnswerToClient ServerError ()
    {
        return new AnswerToClient (500 , false);
    }

    public static AnswerToClient NotLoggedIn ()
    {
        AnswerToClient answerToClient = new AnswerToClient (400 , false);
        answerToClient.put ("answer" , "not logged in");
        return answerToClient;
    }

    public void put (String key , Object value)
    {
        message.put (key , value);
    }

    @JsonProperty ("status_code")
    public int getStatusCode ()
    {
        return statusCode;
    }

    public Map<String, Object> getMessage ()
    {
        return message;
    }

    public boolean isOk ()
    {
        return ok;
    }
}
