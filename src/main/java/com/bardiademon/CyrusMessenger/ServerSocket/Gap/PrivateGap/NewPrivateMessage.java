package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserGapAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.SendGapsFilesTo.SendGapsFilesToService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead.GapReadService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType.GapTypes;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType.GapTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapFor;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsPostedAgain.GapsPostedAgain;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsPostedAgain.GapsPostedAgainService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.PersonalGaps.PersonalGapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap.SecurityUserGap;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.IdUsernameMainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.CheckForward;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.FoundStkrEmjLnk;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.GapType;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NewPrivateMessage
{
    private final SocketIOClient client;
    private final RequestPrivateGap request;

    private AnswerToClient answer;

    private final List <GapType> gapTypes = new ArrayList <> ();
    private final List <GapsFiles> gapsFiles = new ArrayList <> ();

    private MainAccount mainAccount, to;

    private List <Which> which;

    private SecurityUserGap securityUserGap;

    private UserGapAccessLevel gapAccessLevel;

    private Gaps gapReply;

    private PersonalGaps personalGaps;

    private PersonalGapsService personalGapsService;
    private CheckForward.CheckRequestGapAnswer forward;

    public NewPrivateMessage (final SocketIOClient Client , final RequestPrivateGap Request)
    {
        this.client = Client;
        this.request = Request;

        if (checkRequest ())
        {
            if ((!request.isHasFile () || Str.IsEmpty (securityUserGap.getCanSendFileTypes ()) || checkAccessType ()))
            {
                final GapReadService gapReadService = ThisApp.GetService (GapReadService.class);

                if ((to != null && to.getId () == mainAccount.getId ()) || (securityUserGap.getCanSendNumberOfMessageUnread ().equals (0) || securityUserGap.getCanSendNumberOfMessageUnread () > gapReadService.findUnRead (to.getId () , mainAccount.getId ()).size ()))
                {
                    forward = PrivateGap.CheckForward.forward (request , mainAccount);
                    if ((forward == null && checkText ()) || (forward != null && forward.ok))
                        new SendPrivateMessage (saveMessage ());
                    else
                    {
                        if (forward != null)
                        {
                            answer = forward.answerToClient;
                            l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (forward.getClass ().getName ()) , null , SubmitRequestType.socket , true);
                        }
                    }
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.ceiling_to_send_unread_messages.name ());
                    l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.ceiling_to_send_unread_messages.name ()) , null);
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.unacceptable_file_type.name ());
                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.unacceptable_file_type.name ()) , null);
            }
        }

        client.sendEvent (EventName.e_pvgp_send_message.name () , ToJson.To (answer));
    }

    private boolean checkText ()
    {
        if (Str.IsEmpty (request.getText ())) return true;

        final FoundStkrEmjLnk foundStkrEmjLnk = new FoundStkrEmjLnk (request.getText ());
        answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.access_denied.name ());
        if ((!foundStkrEmjLnk.isFoundEmoji () || gapAccessLevel.hasAccess (Which.s_emoji)))
        {
            if (!foundStkrEmjLnk.isFoundSticker () || gapAccessLevel.hasAccess (Which.s_sticker))
            {
                if (!foundStkrEmjLnk.isFoundLink () || gapAccessLevel.hasAccess (Which.s_link))
                    return true;
                else
                {
                    answer = answerAccessDenied ();
                    answer.put (KeyAnswer.which.name () , ValAnswer.link.name ());
                    l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , null);
                }
            }
            else
            {
                answer = answerAccessDenied ();
                answer.put (KeyAnswer.which.name () , ValAnswer.sticker.name ());
                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , null);
            }
        }
        else
        {
            answer = answerAccessDenied ();
            answer.put (KeyAnswer.which.name () , ValAnswer.emoji.name ());
            l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , null);
        }

        return false;
    }

    private AnswerToClient answerAccessDenied ()
    {
        return AnswerToClient.AccessDenied ();
    }

    private boolean checkRequest ()
    {
        final CBSIL both = CBSIL.Both (request , request.getCodeLogin () , EventName.pvgp_send_message.name ());
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            if (!Str.IsEmpty (request.getCodeOnline ()))
            {
                final Online online = SIServer.Onlines.get (request.getCodeOnline ());
                if (online != null)
                {
                    final ID idPersonalGap = new ID (request.getPersonalGapId ());
                    if (idPersonalGap.isValid ())
                    {
                        final IdUsernameMainAccount fitd_username = new IdUsernameMainAccount (ThisApp.GetService (MainAccountService.class) , request.getTo () , null);
                        if (fitd_username.isValid ())
                        {
                            to = fitd_username.getMainAccount ();

                            if ((personalGaps = ((personalGapsService = ThisApp.Services ().Get (PersonalGapsService.class))).getPersonalGap (mainAccount.getId () , to.getId () , request.getPersonalGapId ())) != null)
                            {
                                // agar karbar dare dakhel save massage payam mide va key to ro karbar dige gozashte bod modiriyat shode to nanide gerfte beshe
                                if (to != null && mainAccount.getId () == personalGaps.getCreatedBy ().getId () && mainAccount.getId () == personalGaps.getGapWith ().getId ())
                                    to = mainAccount;

                                final UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (mainAccount , to);

                                if (accessLevel.hasAccess (Which.profile) && accessLevel.hasAccess (Which.find_me))
                                {
                                    gapAccessLevel = new UserGapAccessLevel (mainAccount , to);

                                    if (gapAccessLevel.hasAccess (Which.s_message) && (!request.isHasFile () || gapAccessLevel.hasAccess (Which.s_file)))
                                    {
                                        online.setAnnouncementOfPresence (LocalDateTime.now ());
                                        online.setClient (client);

                                        SIServer.Onlines.replace (request.getCodeOnline () , online);

                                        if (Str.IsEmpty (request.getText ()) && !request.isHasFile ())
                                        {
                                            answer = AnswerToClient.RequestIsNull ();
                                            l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
                                        }
                                        else
                                        {
                                            if (determineTheType (mainAccount.getId ()))
                                            {
                                                if (!Str.IsEmpty (request.getIdStrReplyChat ()))
                                                {
                                                    final ID idReply = new ID (request.getIdStrReplyChat ());
                                                    if (idReply.isValid ())
                                                    {
                                                        gapReply = ThisApp.Services ().Get (GapsService.class).Repository.findById (idReply.getId ());
                                                        if (gapReply == null)
                                                        {
                                                            answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.id_reply_not_found.name ());
                                                            l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_reply_not_found.name ()) , null);
                                                            return false;
                                                        }
                                                    }
                                                    else
                                                    {
                                                        answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.id_reply_invalid.name ());
                                                        l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_reply_invalid.name ()) , null);
                                                        return false;
                                                    }
                                                }

                                                this.securityUserGap = gapAccessLevel.getSecurityUserGap ();
                                                return true;
                                            }
                                            else return false;
                                        }
                                    }
                                    else
                                    {
                                        answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.access_denied.name ());
                                        l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , null);
                                    }
                                }
                                else
                                {
                                    answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , AnswerToClient.CUV.user_not_found.name ());
                                    l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.user_not_found.name ()) , null);
                                }
                            }
                            else
                            {
                                answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.not_found_personal_gap_id.name ());
                                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.not_found_personal_gap_id.name ()) , null);
                            }
                        }
                        else answer = fitd_username.getAnswerToClient ();
                    }
                    else
                    {
                        answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.invalid_personal_gap_id.name ());
                        l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_personal_gap_id.name ()) , null);
                    }
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.online_code_invalid.name ());
                    l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.online_code_invalid.name ()) , null);
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.online_code_empty.name ());
                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.online_code_empty.name ()) , null);
            }
        }
        else answer = both.getAnswerToClient ();

        return false;
    }

    private boolean checkAccessType ()
    {
        List <String> accessType = Arrays.asList (securityUserGap.getCanSendFileTypes ().split (","));
        if (accessType.size () > 0)
            for (GapsFiles gapFile : gapsFiles)
                if (!accessType.contains (gapFile.getUploadedFiles ().getType ())) return false;

        for (Which wh : which) if (!gapAccessLevel.hasAccess (wh)) return false;

        return true;
    }

    private ForSendToClient saveMessage ()
    {
        Gaps gap;
        final long lastIndex = ((personalGaps.getLastIndex ()) + 1);

        final GapsService gapsService = ThisApp.GetService (GapsService.class);

        /*
         * forward info
         */
        Gaps emptyGap = null;

        if (forward == null)
        {
            gap = new Gaps ();

            gap.setText (request.getText ());
            gap.setFrom (mainAccount);
            gap.setToUser (to);
            gap.setSendAt (Time.localDateTime (request.getTimeSend ()));
            gap.setFilesGaps (gapsFiles);
            gap.setIndexGap (lastIndex);
            gap.setPersonalGaps (personalGaps);

            if (gapReply != null) gap.setReply (gapReply);
            gap.setGapFor (GapFor.gprivate);

            final GapTypesService gapTypesService = ThisApp.Services ().Get (GapTypesService.class);


            gap = gapsService.Repository.save (gap);

            final List <GapTypes> gapTypes = new ArrayList <> ();

            for (final GapType gapType : this.gapTypes)
            {
                GapTypes types = new GapTypes ();
                types.setGaps (gap);
                types.setGapType (gapType);
                gapTypes.add (types);
            }
            gapTypesService.Repository.saveAll (gapTypes);

            gap.setGapTypes (gapTypes);
            gapsService.Repository.save (gap);
        }
        else
        {
            gap = forward.gaps;

            GapsPostedAgain gapsPostedAgain = new GapsPostedAgain ();
            gapsPostedAgain.setGap (gap);
            gapsPostedAgain.setMessageFrom (mainAccount);
            gapsPostedAgain.setTo (to);
            gapsPostedAgain = ThisApp.GetService (GapsPostedAgainService.class).Repository.save (gapsPostedAgain);

            emptyGap = new Gaps ();
            emptyGap.setSendAt (gap.getSendAt ());

            /*
             * from va to va personalGap ro baraye search kardan gap set mikonam
             */
            emptyGap.setFrom (gapsPostedAgain.getMessageFrom ());
            emptyGap.setToUser (gapsPostedAgain.getTo ());
            emptyGap.setIndexGap (lastIndex);
            emptyGap.setPersonalGaps (personalGaps);
            emptyGap.setPostedAgain (gapsPostedAgain);

            emptyGap = gapsService.Repository.save (emptyGap);

            gap = emptyGap.getPostedAgain ().getGap ();

//            /*
//             * chon forward shode gap asli daron table GapPostedAgain hast
//             */
//            gap = gap.getPostedAgain ().getGap ();
        }


        answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.ok.name ());
        answer.put (KeyAnswer.was_send.name () , true);

        personalGaps.setLastIndex (lastIndex);
        personalGapsService.Repository.save (personalGaps);

        return new ForSendToClient (mainAccount , to , gap , emptyGap);
    }

    public boolean determineTheType (final long userId)
    {
        if (request.getFileCode () == null) return true;

        final GapsFilesService gapsFilesService = ThisApp.Services ().Get (GapsFilesService.class);

        if (!Str.IsEmpty (request.getText ()))
            gapTypes.add (GapType.text);

        which = new ArrayList <> ();

        final SendGapsFilesToService sendGapsFilesToService = ThisApp.Services ().Get (SendGapsFilesToService.class);

        for (String code : request.getFileCode ())
        {
            GapsFiles gapFile = gapsFilesService.byCode (code);
            if (gapFile != null && ((gapFile.getUploadedFiles ().getUploadedBy ().getId () == userId) || (sendGapsFilesToService.sendTo (userId , gapFile.getCode ()) != null)))
            {
                switch (gapFile.getType ())
                {
                    case gif:
                        which.add (Which.s_gif);
                        gapTypes.add (GapType.gif);
                        break;
                    case image:
                        which.add (Which.s_image);
                        gapTypes.add (GapType.pic);
                        break;
                    case video:
                        which.add (Which.s_video);
                        gapTypes.add (GapType.video);
                        break;
                    case voice:
                        which.add (Which.s_voice);
                        gapTypes.add (GapType.voice);
                        break;
                    case file:
                        which.add (Which.s_file);
                        gapTypes.add (GapType.file);
                        break;
                }

                gapsFiles.add (gapFile);
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.BadRequest () , ValAnswer.file_code_invalid.name ());
                answer.put (KeyAnswer.code.name () , code);
                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.file_code_invalid.name ()) , ToJson.CreateClass.nj (KeyAnswer.code.name () , code));
                gapsFiles.clear ();
                gapTypes.clear ();
                return false;
            }
        }
        return true;
    }

    private enum ValAnswer
    {
        file_code_invalid, online_code_empty, online_code_invalid, unacceptable_file_type,
        ceiling_to_send_unread_messages, sticker, emoji, link, id_reply_invalid, id_reply_not_found, invalid_personal_gap_id, not_found_personal_gap_id
    }

    private enum KeyAnswer
    {
        code, which, was_send
    }

    /*
     * vaghti payam forward shode miyad , ye gap asli darim , ye GapsPostedAgain ba ye gap empty darim pas vakhti payam forward beshe
     * baraye jologiri az in ke payam be hamon kasi ke aval payamo arsal karde nare in class ro misazam ke ,
     *  From , To , Gap ro dakhele in bezaram
     * ke az from va to ke dakhele table gap asli hast estefade nashe
     */
    public static final class ForSendToClient
    {
        public final MainAccount from, to;
        public final Gaps gap;

        public final boolean isForward;

        /*
         * gap forwardi , khaliye va faghat eshare be gap asli dare
         */
        public final Gaps emptyGap;

        public ForSendToClient (final MainAccount From , final MainAccount To , final Gaps Gap)
        {
            this (From , To , Gap , null);
        }

        public ForSendToClient (final MainAccount From , final MainAccount To , final Gaps Gap , final Gaps EmptyGap)
        {
            this.from = From;
            this.to = To;
            this.gap = Gap;
            this.emptyGap = EmptyGap;
            this.isForward = (emptyGap != null);
        }
    }
}
