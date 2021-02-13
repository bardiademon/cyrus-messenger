package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.CheckForward;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.GetAnswerQuestionText.GetAnswerQuestionText;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.GetAnswerQuestionText.GetAnswerQuestionTextRequest;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages.GetMessages;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages.RequestGetMessages;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing.ReqTyping;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.Typing.Typing;
import com.bardiademon.CyrusMessenger.ServerSocket.HostPort;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.bardiademon.Pagination;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;

public final class ServerSocketGap implements SIServer.Client
{
    private final SIServer _SIServer = new SIServer (HostPort.PORT_SERVER_SOCKET_GAP , this);

    private final Pagination pagination = new Pagination ();

    private final StatusOfSentMessage statusOfSentMessage;

    static final CheckForward CheckForward = new CheckForward ();

    private final GetAnswerQuestionText getAnswerQuestionText;

    public CheckPublicRequest checkPublicRequest;

    public ServerSocketGap ()
    {
        on ();
        _SIServer.startServer ();
        statusOfSentMessage = new StatusOfSentMessage ();
        getAnswerQuestionText = new GetAnswerQuestionText ();
        checkPublicRequest = new CheckPublicRequest ();
    }

    private void on ()
    {
        SIServer.CreateFirstConnection (_SIServer.Server);
        _SIServer.Server.addEventListener (EventName.ssg_send_message.name () , RequestPrivateGap.class , (client , data , ackSender) ->
                new NewPrivateMessage (client , data));

        SIServer.SetOffline (_SIServer.Server);

        _SIServer.Server.addEventListener (EventName.ssg_typing.name () , ReqTyping.class , (client , data , ackSender) ->
                new Typing (client , data));

        _SIServer.Server.addEventListener (EventName.get_messages.name () , RequestGetMessages.class , (client , data , ackSender) ->
                new GetMessages (client , data));

        _SIServer.Server.addEventListener (EventName.status_of_sent_message.name () , StatusOfSentMessage.Request.class , (client , data , ackSender) ->
                statusOfSentMessage.status (data , client));

        _SIServer.Server.addEventListener (EventName.ssg_answer_question_text.name () , GetAnswerQuestionTextRequest.class , (client , data , ackSender) ->
                getAnswerQuestionText.answerQuestionText (data , checkPublicRequest.check (data , client , EventName.ssg_answer_question_text)));
    }

    public void deletePersonalGap (MainAccount mainAccount , long personalGapId)
    {
        // ersale dastore hazf be device karbar digar ke tavasot karbar dighar in gofego hazf shode

        SIServer.LoopOnline ((CodeOnline , _Online) ->
        {
            // chon momkene chan ta online vojod dashte bashe baraye hamin ta akhar loop ro donbal mikonal ,
            // chan ta online => ba yek account
            if (_Online.getMainAccount ().getId () == mainAccount.getId ())
                sendDeletePersonalGap (CodeOnline , _Online , personalGapId);

            return true;
        });

    }

    private void sendDeletePersonalGap (String codeOnline , Online online , long personalGapId)
    {
        online.getClient ().sendEvent (EventName.delete_personal_gap.name () , personalGapId);
        online.setAnnouncementOfPresence (Time.now ());
        SIServer.Onlines.replace (codeOnline , online);
    }

    @Override
    public void Connect (SocketIOClient Client)
    {
        l.n (Thread.currentThread ().getStackTrace () , ToJson.CreateClass.nj ("private_chat_connect" , Client.getSessionId ().toString ()));
    }

    public Pagination getPagination ()
    {
        return pagination;
    }
}
