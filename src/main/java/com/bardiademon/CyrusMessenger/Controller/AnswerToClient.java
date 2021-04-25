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
    public static AnswerToClient BadRequest ()
    {
        return new AnswerToClient (400 , false);
    }


    @JsonIgnore
    public static AnswerToClient OneAnswer (final AnswerToClient _AnswerToClient , final Enum <?> Answer)
    {
        return OneAnswer (_AnswerToClient , CUK.answer , Answer);
    }

    @JsonIgnore
    public static AnswerToClient OneAnswer (final AnswerToClient _AnswerToClient , final Object Answer)
    {
        return OneAnswer (_AnswerToClient , CUK.answer , Answer);
    }

    @JsonIgnore
    public static AnswerToClient OneAnswer (final AnswerToClient _AnswerToClient , final Enum <?> Key , final Enum <?> Answer)
    {
        _AnswerToClient.put (Key , Answer);
        return _AnswerToClient;
    }

    @JsonIgnore
    public static AnswerToClient OneAnswer (final AnswerToClient _AnswerToClient , final Enum <?> Key , final Object Answer)
    {
        _AnswerToClient.put (Key , Answer);
        return _AnswerToClient;
    }

    @Deprecated
    @JsonIgnore
    public static AnswerToClient OneAnswer (final AnswerToClient _AnswerToClient , final String Key , final Object Answer)
    {
        _AnswerToClient.put (Key , Answer);
        return _AnswerToClient;
    }

    /**
     * KeyAnswer => Key => KeyAnswer[0] , Answer => KeyAnswer[1] | Key => KeyAnswer[2] , Answer => KeyAnswer[3] ,....
     */
    @JsonIgnore
    public static AnswerToClient KeyAnswer (final AnswerToClient _AnswerToClient , final Object... KeyAnswer)
    {
        for (int i = 0, len = ((KeyAnswer.length) - 1); i < len; i += 2)
            _AnswerToClient.put (String.valueOf (KeyAnswer[i]) , KeyAnswer[(i + 1)]);

        return _AnswerToClient;
    }

    @JsonIgnore
    public static AnswerToClient RequestIsNull ()
    {
        AnswerToClient answerToClient = BadRequest ();
        answerToClient.put (CUK.answer , CUV.request_is_null);
        return answerToClient;
    }

    @JsonIgnore
    public static AnswerToClient AccessDenied ()
    {
        return OneAnswer (AnswerToClient.New (HttpServletResponse.SC_FORBIDDEN) , CUV.access_denied);
    }

    @JsonIgnore
    public static AnswerToClient AccountDeactive ()
    {
        AnswerToClient answerToClient = New (HttpServletResponse.SC_FORBIDDEN);
        answerToClient.put (CUK.answer , CUV.account_deactive);
        return answerToClient;
    }

    @JsonIgnore
    public static AnswerToClient ServerError ()
    {
        AnswerToClient answerToClient = new AnswerToClient (500 , false);
        answerToClient.put (CUK.answer , CUV.please_try_again);
        answerToClient.put (AnswerToClient.CUK.system , AnswerToClient.CUV.sorry_for_this_error);
        return answerToClient;
    }

    @JsonIgnore
    public static AnswerToClient IdInvalid ()
    {
        return IdInvalid (CUV.id_invalid);
    }

    @JsonIgnore
    public static AnswerToClient IdInvalid (final Enum <?> ValAnswer)
    {
        return AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer);
    }

    @Deprecated
    @JsonIgnore
    public static AnswerToClient IdInvalid (final String ValAnswer)
    {
        return AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer);
    }

    @JsonIgnore
    public static AnswerToClient NotLoggedIn ()
    {
        AnswerToClient answerToClient = AnswerToClient.BadRequest ();
        answerToClient.put (CUK.answer , CUV.not_logged_in);
        return answerToClient;
    }

    @JsonIgnore
    public static AnswerToClient New (final int StatusCode , final boolean Ok)
    {
        return new AnswerToClient (StatusCode , Ok);
    }

    @JsonIgnore
    public static AnswerToClient New (final int StatusCode)
    {
        return New (StatusCode , false);
    }

    @JsonIgnore
    public void setResponse (HttpServletResponse response)
    {
        this.response = response;
        setStatusCode ();
    }

    public void setMessage (final Map <String, Object> message)
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
    public void setRequest (final HttpServletRequest request)
    {
        this.request = request;
    }

    @JsonIgnore
    public void setReqRes (final HttpServletRequest request , final HttpServletResponse response)
    {
        if (getRequest () == null) setRequest (request);
        if (getResponse () == null) setResponse (response);
    }

    private void setStatusCode ()
    {
        if (response != null) response.setStatus (getStatusCode ());
    }

    public AnswerToClient put (final Enum <?> key , final Enum <?> value)
    {
        message.put (key.name () , value);
        return this;
    }

    public AnswerToClient put (final Enum <?> key , final Object value)
    {
        message.put (key.name () , value);
        return this;
    }

    public AnswerToClient put (final String key , final Object value)
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
        request_is_null, account_deactive, please_try_again, username_invalid,
        removed, id, changed, added, ok, anonymous, mkdirs_error, not_logged_in, recorded, online_code_invalid
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
