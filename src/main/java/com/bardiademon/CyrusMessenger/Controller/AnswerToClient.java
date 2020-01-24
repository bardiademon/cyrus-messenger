package com.bardiademon.CyrusMessenger.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnswerToClient
{

    private int statusCode;
    private Map<String, Object> message;
    private boolean ok;
    private HttpServletResponse response;

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

    public static AnswerToClient OneAnswer (AnswerToClient _AnswerToClient , Object Answer)
    {
        _AnswerToClient.put (CUK.answer.name () , Answer);
        return _AnswerToClient;
    }

    /**
     * KeyAnswer => Key => KeyAnswer[0] , Answer => KeyAnswer[1] | Key => KeyAnswer[2] , Answer => KeyAnswer[3] ,....
     */
    public static AnswerToClient KeyAnswer (AnswerToClient _AnswerToClient , Object... KeyAnswer)
    {
        for (int i = 0, len = ((KeyAnswer.length) - 1); i < len; i += 2)
            _AnswerToClient.put (String.valueOf (KeyAnswer[i]) , KeyAnswer[(i + 1)]);

        return _AnswerToClient;
    }

    public static AnswerToClient RequestIsNull ()
    {
        AnswerToClient answerToClient = error400 ();
        answerToClient.put (CUK.answer.name () , "request_is_null");
        return answerToClient;
    }

    public static AnswerToClient AccountDeactive ()
    {
        AnswerToClient answerToClient = New (HttpServletResponse.SC_FORBIDDEN);
        answerToClient.put (CUK.answer.name () , "account_deactive");
        return answerToClient;
    }

    public static AnswerToClient ServerError ()
    {
        AnswerToClient answerToClient = new AnswerToClient (500 , false);
        answerToClient.put (CUK.answer.name () , "please_try_again");
        return answerToClient;
    }

    public static AnswerToClient NotLoggedIn ()
    {
        AnswerToClient answerToClient = new AnswerToClient (400 , false);
        answerToClient.put (CUK.answer.name () , "not_logged_in");
        return answerToClient;
    }

    public static AnswerToClient New (int StatusCode , boolean Ok)
    {
        return new AnswerToClient (StatusCode , Ok);
    }

    public static AnswerToClient New (int StatusCode)
    {
        return New (StatusCode , false);
    }

    public void setResponse (HttpServletResponse response)
    {
        this.response = response;
        setStatusCode ();
    }

    private void setStatusCode ()
    {
        response.setStatus (getStatusCode ());
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

    // CUK => Commonly used keys
    public enum CUK
    {
        answer
    }
}
