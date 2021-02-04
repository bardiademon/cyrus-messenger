package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo.SendGapsFilesTo;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo.SendGapsFilesToService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead.GapRead;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead.GapReadService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.SendStatusPrivateMessage.Type;
import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public final class StatusOfSentMessage
{
    public StatusOfSentMessage ()
    {

    }

    public void status (final Request request , final SocketIOClient client)
    {
        new Thread (() ->
        {
            final ID gapId, personalGapId;

            AnswerToClient answer;
            Online online = null;
            if (request != null && (online = SIServer.GetOnline (request.getCodeOnline ())) != null && (request.getType () != null && !request.getType ().isEmpty ()) && (gapId = new ID (request.gapId)).isValid () && (personalGapId = new ID (request.personalGapId)).isValid ())
            {
                final CBSIL both = CBSIL.Both (request , request.getCodeLogin () , EventName.status_of_sent_message.name ());
                if (both.isOk ())
                {
                    assert both.getIsLogin () != null;
                    final MainAccount mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

                    final Type type = Type.to (request.getType ());
                    if (type != null)
                    {
                        final PersonalGapsService personalGapsService = ThisApp.Services ().Get (PersonalGapsService.class);
                        final PersonalGaps personalGaps = personalGapsService.byId (personalGapId.getId () , mainAccount.getId ());

                        if (personalGaps != null)
                        {
                            final GapsService gapsService = ThisApp.Services ().Get (GapsService.class);
                            final Gaps gap = gapsService.Repository.byId (gapId.getId () , personalGaps.getId () , mainAccount.getId ());

                            if (gap != null)
                            {
                                final GapReadService gapReadService = ThisApp.Services ().Get (GapReadService.class);

                                GapRead gapRead = gapReadService.getGapRead (gapId.getId () , mainAccount.getId ());
                                if (gapRead == null)
                                {
                                    gapRead = new GapRead ();
                                    gapRead.setGaps (gap);
                                }
                                switch (type)
                                {
                                    case send:
                                    case delivered:
                                        gapRead.setReceived (true);
                                        gapRead.setReceivedAt (Time.now ());
                                        gapRead.setReadBy (mainAccount);

                                    case read:
                                        gapRead.setRead (true);
                                        gapRead.setReceivedAt (Time.now ());
                                        gapRead.setReceived (true);
                                        gapRead.setReadAt (Time.now ());
                                        gapRead.setReadBy (mainAccount);
                                }

                                /*
                                 * amani ke message ro daryaft mikone karbar file ha ro dakhele table SendGapsFilesTo zakhire mikonam
                                 */
                                final List <GapsFiles> filesGaps = gap.getFilesGaps ();
                                if (filesGaps != null)
                                {
                                    final SendGapsFilesToService sendGapsFilesToService = ThisApp.Services ().Get (SendGapsFilesToService.class);

                                    for (final GapsFiles filesGap : filesGaps)
                                    {
                                        if (sendGapsFilesToService.sendTo (mainAccount.getId () , filesGap.getCode ()) == null)
                                        {
                                            final SendGapsFilesTo sendGapsFilesTo = new SendGapsFilesTo ();
                                            sendGapsFilesTo.setGapsFiles (filesGap);
                                            sendGapsFilesTo.setMainAccount (mainAccount);
                                            sendGapsFilesTo.setSendAt (gap.getSendAt ());
                                            sendGapsFilesToService.Repository.save (sendGapsFilesTo);
                                        }
                                    }
                                }
                                gapReadService.Repository.save (gapRead);

                                answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , ValAnswer.recorded.name ());
                                l.n (ToJson.To (request) , EventName.status_of_sent_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , null , ValAnswer.recorded.name () , SubmitRequestType.socket , false);

                                new SendStatusPrivateMessage (gap , type);
                            }
                            else
                            {
                                answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_gap.name ());
                                l.n (ToJson.To (request) , EventName.status_of_sent_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_gap.name ()) , null , SubmitRequestType.socket , true);
                            }
                        }
                        else
                        {
                            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_personal_gap_id.name ());
                            l.n (ToJson.To (request) , EventName.status_of_sent_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_personal_gap_id.name ()) , null , SubmitRequestType.socket , true);
                        }
                    }
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_type.name ());
                        l.n (ToJson.To (request) , EventName.status_of_sent_message.name () , null , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_type.name ()) , ToJson.CreateClass.nj ("type" , request.getType ()) , SubmitRequestType.socket , true);
                    }
                }
                else answer = both.getAnswerToClient ();
            }
            else
            {
                answer = AnswerToClient.BadRequest ();
                l.n (ToJson.To (request) , EventName.status_of_sent_message.name () , null , answer , Thread.currentThread ().getStackTrace () , new Exception (getClass ().getName ()) , null , SubmitRequestType.socket , false);
            }

            if (online != null)
            {
                online.setClient (client);
                SIServer.Onlines.replace (request.getCodeOnline () , online);
            }

            client.sendEvent (EventName.e_status_of_sent_message.name () , ToJson.To (answer));
        }).start ();
    }

    public static class Request extends PublicRequest
    {
        private String type;

        @JsonProperty ("gap_id")
        private long gapId;

        @JsonProperty ("personal_gap_id")
        private long personalGapId;

        public Request ()
        {
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        public long getGapId ()
        {
            return gapId;
        }

        public void setGapId (long gapId)
        {
            this.gapId = gapId;
        }

        public long getPersonalGapId ()
        {
            return personalGapId;
        }

        public void setPersonalGapId (long personalGapId)
        {
            this.personalGapId = personalGapId;
        }
    }

    private enum ValAnswer
    {
        not_found_personal_gap_id, not_found_gap, invalid_type, recorded
    }
}
