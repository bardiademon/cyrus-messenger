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
        this.gapsService = This.GetService (GapsService.class);
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
                                    l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , ToJson.To (answer) , SubmitRequestType.socket , false);
                                }
                                else
                                {
                                    answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_personal_gap));
                                    l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.not_found_personal_gap) , ToJson.To (answer) , SubmitRequestType.socket , true);
                                }
                            }
                            else
                            {
                                answer = new CheckRequestGapAnswer (idUsernameMainAccount.getAnswerToClient ());
                                l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , l.e (IdUsernameMainAccount.class.getName ()) , ToJson.To (answer) , SubmitRequestType.socket , true);
                            }
                        }
                    }
                    else
                    {
                        answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_personal_gap_id));
                        l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.invalid_personal_gap_id) , ToJson.To (answer) , SubmitRequestType.socket , true);
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
                                        l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , null , ToJson.To (answer) , SubmitRequestType.socket , false);
                                    }
                                    else
                                    {
                                        gaps = gapsService.byId (gapId.getId () , mainAccount.getId ());
                                        if (gaps != null)
                                        {
                                            answer = new CheckRequestGapAnswer (gaps);
                                            l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , null , ToJson.To (answer) , SubmitRequestType.socket , false);
                                        }
                                        else
                                        {
                                            answer = new CheckRequestGapAnswer (AnswerToClient.AccessDenied ());
                                            l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.access_denied) , ToJson.To (answer) , SubmitRequestType.socket , true);
                                        }
                                    }
                                }
                                else
                                {
                                    answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.you_are_not_gap_a_member));
                                    l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.you_are_not_gap_a_member) , ToJson.To (answer) , SubmitRequestType.socket , true);
                                }
                            }
                            else
                            {
                                answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_group));
                                l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.not_found_group) , ToJson.To (answer) , SubmitRequestType.socket , true);
                            }
                        }
                    }
                    else
                    {
                        answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_gap_id));
                        l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , l.e (ValAnswer.invalid_gap_id) , ToJson.To (answer) , SubmitRequestType.socket , true);
                    }
                }
            }
            else
            {
                answer = new CheckRequestGapAnswer (AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.id_invalid));
                l.n (request , mainAccount , answer.answerToClient , Thread.currentThread ().getStackTrace () , l.e (AnswerToClient.CUV.id_invalid) , ToJson.To (answer) , SubmitRequestType.socket , true);
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
