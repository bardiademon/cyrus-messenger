package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapFilesService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Usernames.UsernamesService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase.FITD_Username;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.Gap.GapType;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;
import java.util.ArrayList;
import java.util.List;

public final class NewPrivateMessage
{
    private final SocketIOClient client;
    private final RequestPrivateGap request;

    private AnswerToClient answer;

    private final List <GapType> gapTypes = new ArrayList <> ();
    private final List <GapFiles> gapFiles = new ArrayList <> ();

    private MainAccount mainAccount, to;

    public NewPrivateMessage (SocketIOClient Client , RequestPrivateGap Request)
    {
        this.client = Client;
        this.request = Request;

        if (checkRequest ())
        {

        }
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
                    FITD_Username fitd_username = new FITD_Username (request.getTo () , (UsernamesService) ThisApp.S ().getService (UsernamesService.class));
                    if (fitd_username.isValid ())
                    {
                        to = fitd_username.getMainAccount ();

//                        UserProfileAccessLevel accessLevel = new UserProfileAccessLevel (mainAccount , to);
//
//                        if (accessLevel.hasAccess (Which.find_me) && accessLevel.hasAccess (Which.seen_message//error seen > send))
//                        {
//                            online.setAnnouncementOfPresence (LocalDateTime.now ());
//                            online.setClient (client);
//
//                            SIServer.Onlines.replace (request.getCodeOnline () , online);
//
//                            if (Str.IsEmpty (request.getText ()) && !request.isHasFile ())
//                            {
//                                answer = AnswerToClient.RequestIsNull ();
//                                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (AnswerToClient.CUV.request_is_null.name ()) , null);
//                            }
//                            else return (determineTheType ());
//                        }
                    }
                    else answer = fitd_username.getAnswer ();
                }
                else
                {
                    answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.online_code_empty.name ());
                    l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.online_code_empty.name ()) , null);
                }
            }
            else
            {
                answer = AnswerToClient.OneAnswer (AnswerToClient.error400 () , ValAnswer.online_code_invalid.name ());
                l.n (ToJson.To (request) , EventName.pvgp_send_message.name () , mainAccount , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.online_code_invalid.name ()) , null);
            }
        }
        else answer = both.getAnswerToClient ();

        return false;
    }

    private void saveMessage ()
    {
        Gaps gap = new Gaps ();

        gap.setText (request.getText ());
        gap.setFrom (mainAccount);
        gap.setToUser (to);
    }

    public boolean determineTheType ()
    {
        GapFilesService gapFilesService = (GapFilesService) ThisApp.S ().getService (GapFilesService.class);

        if (!Str.IsEmpty (request.getText ()))
            gapTypes.add (GapType.text);

        for (String code : request.getFileCode ())
        {
            GapFiles gapFile = gapFilesService.byCode (code);
            if (gapFile != null)
            {
                switch (gapFile.getTypes ())
                {
                    case gif:
                        gapTypes.add (GapType.gif);
                        break;
                    case image:
                        gapTypes.add (GapType.pic);
                        break;
                    case video:
                        gapTypes.add (GapType.video);
                        break;
                    case voice:
                        gapTypes.add (GapType.voice);
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
        file_code_invalid, online_code_empty, online_code_invalid
    }

    private enum KeyAnswer
    {
        code
    }

}
