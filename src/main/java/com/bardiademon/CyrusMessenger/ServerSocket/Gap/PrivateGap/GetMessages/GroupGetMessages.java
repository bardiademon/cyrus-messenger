package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;

public final class GroupGetMessages
{
    private final Groups group;
    private final long idLastGet;
    private final AnswerGetMessages answerGetMessages;

    public GroupGetMessages (final Groups Group , final long IdLastGet , final AnswerGetMessages _AnswerGetMessages)
    {
        this.group = Group;
        this.idLastGet = IdLastGet;
        this.answerGetMessages = _AnswerGetMessages;
    }
}
