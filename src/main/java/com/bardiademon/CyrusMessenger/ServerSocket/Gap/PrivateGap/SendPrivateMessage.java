package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.UserGapAccessLevel;
import com.bardiademon.CyrusMessenger.Controller.Security.UserAccessLevel.Which;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.GapFiles.GapFiles;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public final class SendPrivateMessage extends Thread implements Runnable
{
    private final Gaps gaps;

    public SendPrivateMessage (final Gaps _Gaps)
    {
        this.gaps = _Gaps;
        start ();
    }

    @Override
    public void run ()
    {
        SIServer.LoopOnline ((codeOnline , online) ->
        {
            if (gaps.getToUser ().getId () == online.getMainAccount ().getId ())
            {
                send (codeOnline , online);
                return false;
            }
            else return true;
        });
    }

    private void send (String onlineCode , Online online)
    {
        JSONObject message = new JSONObject ();

        message.put (KeyAnswer.id.name () , gaps.getId ());
        message.put (KeyAnswer.text.name () , gaps.getText ());
        message.put (KeyAnswer.send_at.name () , Time.timestamp (gaps.getSendAt ()).getTime ());
        message.put (KeyAnswer.from.name () , gaps.getFrom ().getUsername ().getUsername ());

        if (gaps.getFilesGaps () != null)
        {
            List <String> codeFiles = new ArrayList <> ();
            for (GapFiles filesGap : gaps.getFilesGaps ()) codeFiles.add (filesGap.getCode ());
            message.put (KeyAnswer.files.name () , codeFiles);
        }

        online.setAnnouncementOfPresence (LocalDateTime.now ());
        SIServer.Onlines.replace (onlineCode , online);

        online.getClient ().sendEvent (EventName.pvgp_new_message.name () , message.toString ());

        UserGapAccessLevel gapAccessLevel = new UserGapAccessLevel (gaps.getFrom () , gaps.getToUser ());
        if (gapAccessLevel.hasAccess (Which.seen_message))
            new SendStatusPrivateMessage (gaps , SendStatusPrivateMessage.Type.send);
    }

    private enum KeyAnswer
    {
        id, text, send_at, from, files
    }
}
