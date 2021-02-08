package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserProfileAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesUsageReport.GapsFilesUsageReportService;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapsFilesUsageReport.WhatDidDo;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapType.GapTypes;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.ThisApp;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public final class SendPrivateMessage extends Thread implements Runnable
{
    private final NewPrivateMessage.ForSendToClient forSendToClient;

    public SendPrivateMessage (final NewPrivateMessage.ForSendToClient _ForSendToClient)
    {
        this.forSendToClient = _ForSendToClient;
        start ();
    }

    @Override
    public void run ()
    {
        SIServer.LoopOnline ((codeOnline , online) ->
        {
            if (forSendToClient.to.getId () == online.getMainAccount ().getId ())
            {
                send (codeOnline , online);
                return false;
            }
            else return true;
        });
    }

    private void send (String onlineCode , Online online)
    {
        final JSONObject message = new JSONObject ();

        if (forSendToClient.isForward)
            message.put (KeyAnswer.gap_id.name () , forSendToClient.emptyGap.getId ());
        else
            message.put (KeyAnswer.gap_id.name () , forSendToClient.gap.getId ());

        message.put (KeyAnswer.text.name () , forSendToClient.gap.getText ());
        message.put (KeyAnswer.send_at.name () , Time.timestamp (forSendToClient.gap.getSendAt ()).getTime ());

        final MainAccount from = forSendToClient.from;

        message.put (KeyAnswer.from.name () , from.getUsername ().getUsername ());
        message.put (KeyAnswer.forward_from.name () , forSendToClient.gap.getFrom ().getUsername ().getUsername ());

        final List <GapTypes> gapTypes = forSendToClient.gap.getGapTypes ();

        if (gapTypes != null && gapTypes.size () > 0)
        {
            List <String> gapsTypes = new ArrayList <> ();
            for (GapTypes gapType : gapTypes) gapsTypes.add (gapType.getGapType ().name ());
            message.put (KeyAnswer.gap_types.name () , gapsTypes);
        }

        final List <GapsFiles> filesGaps = forSendToClient.gap.getFilesGaps ();
        final MainAccount to = forSendToClient.to;

        if (filesGaps != null && filesGaps.size () > 0)
        {
            final List <String> codeFiles = new ArrayList <> ();
            final GapsFilesUsageReportService gapsFilesUsageReportService = ThisApp.Services ().Get (GapsFilesUsageReportService.class);

            for (final GapsFiles filesGap : filesGaps)
            {
                gapsFilesUsageReportService.used (filesGap , to , WhatDidDo.get_link);
                codeFiles.add (filesGap.getCode ());
            }
            message.put (KeyAnswer.files.name () , codeFiles);
        }

        online.setAnnouncementOfPresence (LocalDateTime.now ());
        SIServer.Onlines.replace (onlineCode , online);

        online.getClient ().sendEvent (EventName.pvgp_new_message.name () , message.toString ());

        /*
         * ba aks hast chon from mikhad bebine payami ke ersal karde to khondash ya na
         * pas agar tanzimar to seen_message baraye from baz bashe ersal mishe be from ke to khonde
         */
        if ((new UserProfileAccessLevel (to , from)).hasAccess (Which.seen_message))
            new SendStatusPrivateMessage (forSendToClient , SendStatusPrivateMessage.Type.send);
    }

    private enum KeyAnswer
    {
        gap_id, text, send_at, from, files, gap_types, forward_from;
    }
}
