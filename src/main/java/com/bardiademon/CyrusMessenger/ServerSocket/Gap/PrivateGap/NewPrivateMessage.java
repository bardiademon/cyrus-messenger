package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserGapAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapFilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapRead.GapReadService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType.GapTypes;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType.GapTypesService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapFor;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.GapsService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.UserSecurity.SecurityUserGap.SecurityUserGap;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
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
    private final List <GapFiles> gapFiles = new ArrayList <> ();

    private MainAccount mainAccount, to;

    private List <Which> which;

    private SecurityUserGap securityUserGap;

    private UserGapAccessLevel gapAccessLevel;

    private Gaps gapReply;

    public NewPrivateMessage (final SocketIOClient Client , final RequestPrivateGap Request)
    {
        this.client = Client;
        this.request = Request;

        if (checkRequest ())
        {
            if ((!request.isHasFile () || Str.IsEmpty (securityUserGap.getCanSendFileTypes ()) || checkAccessType ()))
            {
                GapReadService gapReadService = ThisApp.S ().getService (GapReadService.class);

                if (to.getId () == mainAccount.getId () || (securityUserGap.getCanSendNumberOfMessageUnread ().equals (0) || securityUserGap.getCanSendNumberOfMessageUnread () > gapReadService.findUnRead (to.getId () , mainAccount.getId ()).size ()))
                {
                    if (checkText ())
                        new SendPrivateMessage (saveMessage ());
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.ceiling_to_send_unread_messages.name ());
                    l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.ceiling_to_send_unread_messages.name ()) , null);
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.unacceptable_file_type.name ());
                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.unacceptable_file_type.name ()) , null);
            }
        }

        client.sendEvent (EventName.e_pvgp_send_message.name () , ToJson.To (answer));
    }

    private boolean checkText ()
    {
        if (Str.IsEmpty (request.getText ())) return true;

        FoundStkrEmjLnk foundStkrEmjLnk = new FoundStkrEmjLnk (request.getText ());
        answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.access_denied.name ());
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
        CBSIL both = CBSIL.Both (request , request.getCodeLogin () , EventName.pvgp_send_message.name ());
        if (both.isOk ())
        {
            assert both.getIsLogin () != null;
            mainAccount = both.getIsLogin ().getVCodeLogin ().getMainAccount ();

            if (!Str.IsEmpty (request.getCodeOnline ()))
            {
                Online online = SIServer.Onlines.get (request.getCodeOnline ());
                if (online != null)
                {
                    FITD_Username fitd_username = new FITD_Username (request.getTo () , ThisApp.S ().getService (UsernamesService.class));
                    if (fitd_username.isValid ())
                    {
                        to = fitd_username.getMainAccount ();

                        UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (mainAccount , to);

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
                                    if (determineTheType ())
                                    {
                                        if (!Str.IsEmpty (request.getIdStrReplyChat ()))
                                        {
                                            ID idReply = new ID (request.getIdStrReplyChat ());
                                            if (idReply.isValid ())
                                            {
                                                gapReply = ThisApp.S ().getService (GapsService.class).Repository.findById (idReply.getId ());
                                                if (gapReply == null)
                                                {
                                                    answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_reply_not_found.name ());
                                                    l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.id_reply_not_found.name ()) , null);
                                                    return false;
                                                }
                                            }
                                            else
                                            {
                                                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.id_reply_invalid.name ());
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
                                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.access_denied.name ());
                                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.access_denied.name ()) , null);
                            }
                        }
                        else
                        {
                            answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , AnswerToClient.CUV.user_not_found.name ());
                            l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.user_not_found.name ()) , null);
                        }
                    }
                    else answer = fitd_username.getAnswer ();
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.online_code_invalid.name ());
                    l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.online_code_invalid.name ()) , null);
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.online_code_empty.name ());
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
            for (GapFiles gapFile : gapFiles) if (!accessType.contains (gapFile.getFileExtension ())) return false;

        for (Which wh : which) if (!gapAccessLevel.hasAccess (wh)) return false;

        return true;
    }

    private Gaps saveMessage ()
    {
        Gaps gap = new Gaps ();

        gap.setText (request.getText ());
        gap.setFrom (mainAccount);
        gap.setToUser (to);
        gap.setSendAt (Time.localDateTime (request.getTimeSend ()));
        gap.setFilesGaps (gapFiles);
        if (gapReply != null) gap.setReply (gapReply);
        gap.setGapFor (GapFor.sec_gprivate);

        GapTypesService gapTypesService = ThisApp.S ().getService (GapTypesService.class);
        GapsService gapsService = ThisApp.S ().getService (GapsService.class);

        gap = gapsService.Repository.save (gap);

        List <GapTypes> gapTypes = new ArrayList <> ();

        for (GapType gapType : this.gapTypes)
        {
            GapTypes types = new GapTypes ();
            types.setGaps (gap);
            types.setGapType (gapType);
            gapTypes.add (types);
        }

        gapTypesService.Repository.saveAll (gapTypes);

        gap.setGapTypes (gapTypes);
        gapsService.Repository.save (gap);

        answer = AnswerToClient.OneAnswer (AnswerToClient.OK () , AnswerToClient.CUV.ok.name ());
        answer.put (KeyAnswer.was_send.name () , true);

        return gap;
    }

    public boolean determineTheType ()
    {
        if (request.getFileCode () == null) return true;

        GapFilesService gapFilesService = ThisApp.S ().getService (GapFilesService.class);

        if (!Str.IsEmpty (request.getText ()))
            gapTypes.add (GapType.text);

        which = new ArrayList <> ();
        for (String code : request.getFileCode ())
        {
            GapFiles gapFile = gapFilesService.byCode (code);
            if (gapFile != null)
            {
                switch (gapFile.getTypes ())
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

                gapFiles.add (gapFile);
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.file_code_invalid.name ());
                answer.put (KeyAnswer.code.name () , code);
                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.file_code_invalid.name ()) , ToJson.CreateClass.nj (KeyAnswer.code.name () , code));
                gapFiles.clear ();
                gapTypes.clear ();
                return false;
            }
        }
        return true;
    }

    private enum ValAnswer
    {
        file_code_invalid, online_code_empty, online_code_invalid, unacceptable_file_type,
        ceiling_to_send_unread_messages, sticker, emoji, link, id_reply_invalid, id_reply_not_found
    }

    private enum KeyAnswer
    {
        code, which, was_send
    }

}
