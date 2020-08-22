package com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public final class CheckBlockSystem
{
    private long idUser;
    private String ip;
    private BlockedByTheSystemService service;
    private String des;

    private AnswerToClient answerToClient;

    private BlockedByTheSystem blockedByTheSystem;
    private BlockedFor blockedFor;

    public CheckBlockSystem ()
    {
    }

    public CheckBlockSystem (long IdUser , BlockedFor _BlockedFor , String Des)
    {
        this (IdUser , ThisApp.Context ().getBean (BlockedByTheSystemService.class) , _BlockedFor , Des);
    }

    public CheckBlockSystem (long IdUser , BlockedByTheSystemService Service , BlockedFor _BlockedFor , String Des)
    {
        this (null , Service , _BlockedFor , Des);
        this.idUser = IdUser;
    }

    public CheckBlockSystem (HttpServletRequest Request , BlockedFor _BlockedFor , String Des)
    {
        this (Request , ThisApp.Context ().getBean (BlockedByTheSystemService.class) , _BlockedFor , Des);
    }

    public CheckBlockSystem (HttpServletRequest Request , BlockedByTheSystemService Service , BlockedFor _BlockedFor , String Des)
    {
        if (Request != null) this.ip = Request.getRemoteAddr ();
        this.service = Service;
        this.blockedFor = _BlockedFor;
        this.des = Des;
    }

    public boolean isBlocked ()
    {
        if (!isBlocked (BlockedFor.blocked_all_service , null)) return isBlocked (blockedFor , des);
        else return true;
    }

    private boolean isBlocked (BlockedFor blockedFor , String des)
    {
        if (Str.IsEmpty (ip))
        {
            if (des != null) blockedByTheSystem = service.isBlockedFor (idUser , blockedFor , des);
            else blockedByTheSystem = service.isBlockedFor (idUser , blockedFor);
        }
        else
        {
            if (des != null)
                blockedByTheSystem = service.isBlockedFor (ip , blockedFor , des);
            else blockedByTheSystem = service.isBlockedFor (ip , blockedFor);
        }

        if (blockedByTheSystem != null)
        {
            setAnswerToClient ();
            return true;
        }

        return false;
    }

    private void setAnswerToClient ()
    {
        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.New (HttpServletResponse.SC_FORBIDDEN) , ValAnswer.blocked_by_the_system.name ());
        answerToClient.put (KeyAnswer.time_to_free.name () , Time.toString (blockedByTheSystem.getValidityTime ()));
    }

    public AnswerToClient getAnswerToClient ()
    {
        return answerToClient;
    }

    private enum ValAnswer
    {
        blocked_by_the_system
    }

    private enum KeyAnswer
    {
        time_to_free
    }

    public List<BlockedByTheSystem> getListBlocksMainAccount ()
    {
        return (ThisApp.Context ().getBean (BlockedByTheSystemService.class)).getListBlocksMainAccount ();
    }
}
