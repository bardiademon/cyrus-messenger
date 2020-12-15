package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.Pagination;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class UserGetMessages
{

    private final GapsService gapsService;
    private final PersonalGapsService personalGapsService;
    private final SocketIOClient client;
    private final MainAccount user;
    private final PersonalGaps personalGaps;
    private final int page;
    private final AnswerGetMessages answerGetMessages;
    private final RequestGetMessages request;

    private AnswerToClient answer;

    public UserGetMessages
            (final SocketIOClient Client ,
             final MainAccount User ,
             final PersonalGaps _PersonalGaps ,
             final int Page ,
             final AnswerGetMessages _AnswerGetMessages ,
             final RequestGetMessages _RequestGetMessages)
    {
        this.client = Client;
        this.user = User;
        this.personalGaps = _PersonalGaps;
        this.page = Page;
        this.answerGetMessages = _AnswerGetMessages;
        this.request = _RequestGetMessages;
        this.gapsService = ThisApp.S ().getService (GapsService.class);
        this.personalGapsService = ThisApp.S ().getService (PersonalGapsService.class);
        get ();
        answer ();
    }

    private void get ()
    {
        final long size = gapsService.Repository.countGaps (personalGaps.getId () , user.getId ());

        if (size > 0)
        {
            final Integer maxGet = (ThisApp.S ().getService (DefaultService.class)).getInt (DefaultKey.max_get_gaps);
            if (maxGet != null)
            {
                final Pagination.Answer paginationAnswer = ThisApp.getServer ().getPrivateGap ().getPagination ().computing (page , size , maxGet);
                final List <Gaps> privateGaps = gapsService.getPrivateGaps (user.getId () , personalGaps , paginationAnswer);
                if (privateGaps != null && privateGaps.size () > 0)
                {
                    final List <Map <String, Object>> gaps = new ArrayList <> ();
                    for (Gaps privateGap : privateGaps)
                        gaps.add (gapToMap (privateGap , null));

                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.found.name ());
                    answer.put (KeyAnswer.gaps.name () , gaps);
                    l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.found.name ()) , null);
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
                    l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null);
                }
            }
            else
            {
                answer = AnswerToClient.ServerError ();
                l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.please_try_again.name ()) , null);
            }
        }
        else
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.not_found.name ());
            l.n (ToJson.To (request) , EventName.get_messages.name () , user , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.not_found.name ()) , null);
        }
    }

    private Map <String, Object> gapToMap (Gaps gap , MainAccount messageForwardedFor)
    {
        if (!gap.isForwarded ())
        {
            ToJson.CreateClass createClass = new ToJson.CreateClass ();
            createClass.put (KeyAnswer.id.name () , gap.getId ());
            createClass.put (KeyAnswer.text.name () , gap.getText ());
            createClass.put (KeyAnswer.from.name () , gap.getFrom ().getId ());
            createClass.put (KeyAnswer.to.name () , gap.getToUser ().getId ());

            if (messageForwardedFor != null)
                createClass.put (KeyAnswer.forwarded.name () , messageForwardedFor.getId ());

            List <GapsFiles> filesGaps = gap.getFilesGaps ();
            if (filesGaps != null && filesGaps.size () > 0)
            {
                List <String> filesGapsCode = new ArrayList <> ();
                for (GapsFiles filesGap : filesGaps) filesGapsCode.add (filesGap.getCode ());
                createClass.put (KeyAnswer.files.name () , filesGapsCode);
            }

            if (messageForwardedFor == null && gap.getReply () != null)
            {
                Gaps reply = gap.getReply ();

                ToJson.CreateClass gapReply = new ToJson.CreateClass ();
                gapReply.put (KeyAnswer.text.name () , reply.getText ());
                gapReply.put (KeyAnswer.has_file.name () , (reply.getFilesGaps () != null));

                createClass.put (KeyAnswer.reply.name () , gapReply);
            }
            return createClass.getCreateClass ();
        }
        else return gapToMap (gap.getForwarded ().getGap () , gap.getForwarded ().getMessageFrom ());
    }

    private enum KeyAnswer
    {
        id, text, from, to, forwarded, has_file, files, reply,

        gaps
    }

    private void answer ()
    {
        answerGetMessages.Answer (answer);
    }
}
