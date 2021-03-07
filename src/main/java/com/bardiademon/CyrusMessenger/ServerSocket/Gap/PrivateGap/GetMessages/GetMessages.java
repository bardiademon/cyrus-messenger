package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;

public final class GetMessages
{
    private final SocketIOClient client;
    private final RequestGetMessages request;

    private AnswerToClient answer;

    private String strRequest;

    private MainAccount mainAccount;

    private final AnswerGetMessages answerGetMessages = Answer ->
    {
        this.answer = Answer;
        answerToClient ();
    };

    public GetMessages (final SocketIOClient Client , final RequestGetMessages Request)
    {
        this.client = Client;
        this.request = Request;
        checkRequest ();
    }

    private void checkRequest ()
    {
        final CBSIL both = CBSIL.Both (request , request.getCodeLogin () , EventName.get_messages);
        if (both.isOk ())
        {
            strRequest = ToJson.To (request);

            assert both.getIsLogin () != null;
            mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            final RequestGetMessages.Type type = RequestGetMessages.Type.to (request.getType ());
            if (type != null)
            {
                switch (type)
                {
                    case group:
                        checkGroup ();
                        break;
                    case user:
                        checkUser (mainAccount);
                        break;
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_type);
                l.n (strRequest , EventName.get_messages , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.invalid_type) , ToJson.CreateClass.nj ("type" , request.getType ()) , SubmitRequestType.socket , true);
            }
        }
        else answer = both.getAnswerToClient ();
    }

    private void checkGroup ()
    {
        final ILUGroup iluGroup = new ILUGroup ();
        iluGroup.setId (request.getId ());
        if (iluGroup.isValid ())
            new GroupGetMessages (iluGroup.getGroup () , request.getPage () , answerGetMessages);
        else
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_id_group);
            l.n (strRequest , EventName.get_messages , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.invalid_id_group) , ToJson.CreateClass.nj ("id" , request.getId ()) , SubmitRequestType.socket , true);
        }
    }

    private void checkUser (MainAccount mainAccount)
    {
        final ID idUser = new ID (request.getId ());
        if (idUser.isValid ())
        {
            final ID personalGapsId = new ID (request.getPersonalGapsId ());

            if (personalGapsId.isValid ())
            {
                final PersonalGaps personalGaps = ((This.GetService (PersonalGapsService.class))
                        .byId (personalGapsId.getId () , mainAccount.getId ()));
                if (personalGaps != null)
                {
                    final MainAccountService mainAccountService = This.GetService (MainAccountService.class);
                    final MainAccount user = mainAccountService.findId (idUser.getId ());
                    if (user != null)
                        new UserGetMessages (user , personalGaps , request.getPage () , answerGetMessages , request);
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_user);
                        l.n (strRequest , EventName.get_messages , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.not_found_user) , ToJson.CreateClass.nj ("id" , request.getId ()) , SubmitRequestType.socket , true);
                    }
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_personal_gaps_id);
                    l.n (strRequest , EventName.get_messages , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.not_found_personal_gaps_id) , ToJson.CreateClass.nj ("personal_gaps_id" , request.getPersonalGapsId ()) , SubmitRequestType.socket , true);
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_personal_gaps_id);
                l.n (strRequest , EventName.get_messages , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.invalid_personal_gaps_id) , ToJson.CreateClass.nj ("personal_gaps_id" , request.getPersonalGapsId ()) , SubmitRequestType.socket , true);
            }

        }
        else
        {
            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_id_user);
            l.n (strRequest , EventName.get_messages , mainAccount , answer , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.invalid_id_user) , ToJson.CreateClass.nj ("id" , request.getId ()) , SubmitRequestType.socket , true);
        }
    }

    private void answerToClient ()
    {
        client.sendEvent (EventName.e_get_messages.name () , ToJson.To (answer));
        l.n (strRequest , EventName.get_messages , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , "send to client" , SubmitRequestType.socket , false);
    }

    private enum ValAnswer
    {
        invalid_type, invalid_id_group, invalid_id_user, not_found_user, invalid_personal_gaps_id, not_found_personal_gaps_id
    }

}
