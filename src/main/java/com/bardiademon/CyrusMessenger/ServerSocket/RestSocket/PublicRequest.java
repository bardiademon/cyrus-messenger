package com.bardiademon.CyrusMessenger.ServerSocket.RestSocket;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PublicRequest
{
    @JsonProperty ("code_login")
    private String codeLogin;

    @JsonProperty ("online_code")
    private String codeOnline;

    @JsonIgnore
    private AnswerToClient answer;

    public PublicRequest ()
    {
    }

    public String getCodeLogin ()
    {
        return codeLogin;
    }

    public void setCodeLogin (String codeLogin)
    {
        this.codeLogin = codeLogin;
    }

    public String getCodeOnline ()
    {
        return codeOnline;
    }

    public void setCodeOnline (String codeOnline)
    {
        this.codeOnline = codeOnline;
    }

    public boolean checkOnlineCode ()
    {
        if ((!Str.IsEmpty (codeOnline) && SIServer.Onlines.containsKey (codeOnline)))
            return true;
        else
        {
            l.n (Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_online_code.name ()) , codeOnline);
            answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.invalid_online_code.name ());
            return false;
        }
    }

    public Online getOnline ()
    {
        if ((!Str.IsEmpty (codeOnline) && SIServer.Onlines.containsKey (codeOnline)))
            return SIServer.Onlines.get (codeOnline);
        else
        {
            l.n (Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_online_code.name ()) , codeOnline);
            answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.invalid_online_code.name ());
            return null;
        }
    }

    public AnswerToClient getAnswer ()
    {
        return answer;
    }

    private enum ValAnswer
    {
        invalid_online_code
    }
}
