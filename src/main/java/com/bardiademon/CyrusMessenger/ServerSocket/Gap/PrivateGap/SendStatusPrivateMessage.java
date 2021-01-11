package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps.Gaps;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import org.json.JSONObject;

public final class SendStatusPrivateMessage extends Thread implements Runnable
{
    private final Type type;
    private final Gaps gaps;

    public SendStatusPrivateMessage (Gaps _Gaps , Type _Type)
    {
        this.gaps = _Gaps;
        this.type = _Type;
        start ();
    }

    @Override
    public void run ()
    {
        send ();
    }

    private void send ()
    {
        if (gaps != null && type != null)
        {
            SIServer.LoopOnline ((CodeOnline , _Online) ->
            {
                if (gaps.getFrom ().getId () == _Online.getMainAccount ().getId ())
                {
                    final JSONObject statusMessage = new JSONObject ();
                    statusMessage.put (KeyAnswer.id.name () , gaps.getId ());
                    statusMessage.put (KeyAnswer.status.name () , type.name ());
                    _Online.getClient ().sendEvent (EventName.pvgp_status_message.name () , ToJson.To (statusMessage));
                    return false;
                }
                else return true;
            });
        }
    }

    private enum KeyAnswer
    {
        id, status
    }

    public enum Type
    {
        delivered, read, send
    }
}
