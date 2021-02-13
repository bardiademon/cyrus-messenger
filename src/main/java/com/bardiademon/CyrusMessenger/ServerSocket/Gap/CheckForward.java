package com.bardiademon.CyrusMessenger.ServerSocket.Gap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapFor;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.ILUGroup;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.JoinGroup.IsJoined;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;

public final class CheckForward
{
    private final GapsService gapsService;

    public CheckForward ()
    {
        this.gapsService = This.Services ().Get (GapsService.class);
    }

    public CheckRequestGapAnswer forward (final RequestGap requestGap , final MainAccount mainAccount)
    {
        final String request = ToJson.To (requestGap);
        CheckRequestGapAnswer answer = null;
        if (requestGap.getGapId () > 0)
        {
            final ID gapId = new ID (requestGap.getGapId ());
            if (gapId.isValid ())
            {
                if (requestGap.isPersonalGap ())
                {
                    final ID personalGapId = new ID (requestGap.getPersonalGapId ());
                    if (personalGapId.isValid ())
                    {
                        final Gaps gaps = gapsService.byId (gapId.getId () , mainAccount.getId ());
                        if (gaps != null)
                        {
                            final IdUsernameMainAccount idUsernameMainAccount = new IdUsernameMainAccount (This.GetService (MainAccountService.class) , requestGap.getTo () , null);
                            if (idUsernameMainAccount.isValid ())
                            {
                                final PersonalGaps personalGap = (This.GetService (PersonalGapsService.class)).getPersonalGap (mainAccount.getId () , idUsernameMainAccount.getIdUser () , personalGapId.getId ());
                                if (personalGap != null)
                                {
                                    answer = new CheckRequestGapAnswer (gaps);
                                    l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , null , ToJson.To (answer) , SubmitRequestType.socket , false);
                                }
                                else
                                {
                                    answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_personal_gap.name ()));
                                    l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_personal_gap.name ()) , ToJson.To (answer) , SubmitRequestType.socket , true);
                                }
                            }
                            else
                            {
                                answer = new CheckRequestGapAnswer (idUsernameMainAccount.getAnswerToClient ());
                                l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , new Exception (IdUsernameMainAccount.class.getName ()) , ToJson.To (answer) , SubmitRequestType.socket , true);
                            }
                        }
                    }
                    else
                    {
                        answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_personal_gap_id.name ()));
                        l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_personal_gap_id.name ()) , ToJson.To (answer) , SubmitRequestType.socket , true);
                    }
                }
                else
                {
                    Gaps gaps = gapsService.byId (gapId.getId ());
                    if (gaps != null)
                    {
                        final ID groupId = new ID (requestGap.getTo ());
                        if (groupId.isValid ())
                        {
                            final ILUGroup iluGroup = new ILUGroup ();
                            iluGroup.setId (groupId.getId ());
                            if (iluGroup.isValid ())
                            {
                                final IsJoined isJoined = new IsJoined (mainAccount , groupId);
                                if (isJoined.is ())
                                {
                                    if (gaps.getGapFor ().equals (GapFor.ggroup))
                                    {
                                        answer = new CheckRequestGapAnswer (gaps);
                                        l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , null , ToJson.To (answer) , SubmitRequestType.socket , false);
                                    }
                                    else
                                    {
                                        gaps = gapsService.byId (gapId.getId () , mainAccount.getId ());
                                        if (gaps != null)
                                        {
                                            answer = new CheckRequestGapAnswer (gaps);
                                            l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , null , ToJson.To (answer) , SubmitRequestType.socket , false);
                                        }
                                        else
                                        {
                                            answer = new CheckRequestGapAnswer (AnswerToClient.AccessDenied ());
                                            l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , ToJson.To (answer) , SubmitRequestType.socket , true);
                                        }
                                    }
                                }
                                else
                                {
                                    answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.you_are_not_gap_a_member.name ()));
                                    l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.you_are_not_gap_a_member.name ()) , ToJson.To (answer) , SubmitRequestType.socket , true);
                                }
                            }
                            else
                            {
                                answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_group.name ()));
                                l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_group.name ()) , ToJson.To (answer) , SubmitRequestType.socket , true);
                            }
                        }
                    }
                    else
                    {
                        answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_gap_id.name ()));
                        l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_gap_id.name ()) , ToJson.To (answer) , SubmitRequestType.socket , true);
                    }
                }
            }
            else
            {
                answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.id_invalid.name ()));
                l.n (request , null , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.id_invalid.name ()) , ToJson.To (answer) , SubmitRequestType.socket , true);
            }
        }
        return answer;
    }

    public final static class CheckRequestGapAnswer
    {
        public final Gaps gaps;
        public final AnswerToClient answerToClient;
        public final boolean ok;

        private CheckRequestGapAnswer (final AnswerToClient answerToClient)
        {
            this (null , answerToClient , false);
        }

        private CheckRequestGapAnswer (final Gaps gaps)
        {
            this (gaps , null , true);
        }

        private CheckRequestGapAnswer (final Gaps gaps , final AnswerToClient answerToClient , final boolean ok)
        {
            this.gaps = gaps;
            this.answerToClient = answerToClient;
            this.ok = ok;
        }
    }

    private enum ValAnswer
    {
        invalid_personal_gap_id, not_found_personal_gap, invalid_gap_id, not_found_group, you_are_not_gap_a_member
    }
}
