package com.bardiademon.CyrusMessenger.Controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnswerToClient
{

    private int statusCode;
    private Map <String, Object> message;
    private boolean ok;

    @JsonIgnore
    private HttpServletResponse response;

    @JsonIgnore
    private HttpServletRequest request;

    @Deprecated
    public AnswerToClient (int StatusCode , boolean Ok)
    {
        this.statusCode = StatusCode;
        this.ok = Ok;
        message = new LinkedHashMap <> ();
    }

    @JsonIgnore
    public static AnswerToClient OK ()
    {
        return new AnswerToClient (200 , true);
    }

    @JsonIgnore
    public static AnswerToClient error400 ()
    {
        return new AnswerToClient (400 , false);
    }

    @JsonIgnore
    public static AnswerToClient OneAnswer (AnswerToClient _AnswerToClient , Object Answer)
    {
        return OneAnswer (_AnswerToClient , CUK.answer.name () , Answer);
    }

    @JsonIgnore
    public static AnswerToClient OneAnswer (AnswerToClient _AnswerToClient , String Key , Object Answer)
    {
        _AnswerToClient.put (Key , Answer);
        return _AnswerToClient;
    }

    /**
     * KeyAnswer => Key => KeyAnswer[0] , Answer => KeyAnswer[1] | Key => KeyAnswer[2] , Answer => KeyAnswer[3] ,....
     */
    @JsonIgnore
    public static AnswerToClient KeyAnswer (AnswerToClient _AnswerToClient , Object... KeyAnswer)
    {
        for (int i = 0, len = ((KeyAnswer.length) - 1); i < len; i += 2)
            _AnswerToClient.put (String.valueOf (KeyAnswer[i]) , KeyAnswer[(i + 1)]);

        return _AnswerToClient;
    }

    @JsonIgnore
    public static AnswerToClient RequestIsNull ()
    {
        AnswerToClient answerToClient = error400 ();
        answerToClient.put (CUK.answer.name () , CUV.request_is_null.name ());
        return answerToClient;
    }

    @JsonIgnore
    public static AnswerToClient AccessDenied ()
    {
        return OneAnswer (AnswerToClient.New (HttpServletResponse.SC_FORBIDDEN) , CUV.access_denied.name ());
    }

    @JsonIgnore
    public static AnswerToClient AccountDeactive ()
    {
        AnswerToClient answerToClient = New (HttpServletResponse.SC_FORBIDDEN);
        answerToClient.put (CUK.answer.name () , CUV.account_deactive.name ());
        return answerToClient;
    }

    @JsonIgnore
    public static AnswerToClient ServerError ()
    {
        AnswerToClient answerToClient = new AnswerToClient (500 , false);
        answerToClient.put (CUK.answer.name () , CUV.please_try_again.name ());
        answerToClient.put (AnswerToClient.CUK.system.name () , AnswerToClient.CUV.sorry_for_this_error.name ());
        return answerToClient;
    }

    @JsonIgnore
    public static AnswerToClient IdInvalid ()
    {
        return IdInvalid (CUV.id_invalid.name ());
    }

    @JsonIgnore
    public static AnswerToClient IdInvalid (String ValAnswer)
    {
        return AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer);
    }

    @JsonIgnore
    public static AnswerToClient NotLoggedIn ()
    {
        AnswerToClient answerToClient = AnswerToClient.error400 ();
        answerToClient.put (CUK.answer.name () , "not_logged_in");
        return answerToClient;
    }

    @JsonIgnore
    public static AnswerToClient New (int StatusCode , boolean Ok)
    {
        return new AnswerToClient (StatusCode , Ok);
    }

    @JsonIgnore
    public static AnswerToClient New (int StatusCode)
    {
        return New (StatusCode , false);
    }

    @JsonIgnore
    public void setResponse (HttpServletResponse response)
    {
        this.response = response;
        setStatusCode ();
    }

    public void setMessage (Map <String, Object> message)
    {
        this.message = message;
    }

    @JsonIgnore
    public HttpServletRequest getRequest ()
    {
        return request;
    }

    @JsonIgnore
    public HttpServletResponse getResponse ()
    {
        return response;
    }

    @JsonIgnore
    public void setRequest (HttpServletRequest request)
    {
        this.request = request;
    }

    @JsonIgnore
    public void setReqRes (HttpServletRequest request , HttpServletResponse response)
    {
        if (getRequest () == null) setRequest (request);
        if (getResponse () == null) setResponse (response);
    }

    private void setStatusCode ()
    {
        if (response != null) response.setStatus (getStatusCode ());
    }

    public AnswerToClient put (String key , Object value)
    {
        message.put (key , value);
        return this;
    }

    @JsonProperty ("status_code")
    public int getStatusCode ()
    {
        return statusCode;
    }

    @JsonProperty ("message")
    public Map <String, Object> getMessage ()
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
        answer, id, ids, system, time, limit, which, result, min_width, min_height,
        max_width, max_height,
        width, height,
        acceptable_size, extra_size, acceptable_width_height
    }

    // CUK => Commonly used value
    public enum CUV
    {
        sorry_for_this_error, id_invalid, not_found, user_not_found, invalid_width_or_height,
        access_has_been_disabled, access_denied, found, error, not_found_id,
        request_is_null, account_deactive, please_try_again, username_invalid, removed, id, changed, added, ok,
    }

    @JsonIgnore
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
            e.printStackTrace ();
            return null;
        }
    }

    public void setStatusCode (int statusCode)
    {
        this.statusCode = statusCode;
    }

    public void setOk (boolean ok)
    {
        this.ok = ok;
    }

}
