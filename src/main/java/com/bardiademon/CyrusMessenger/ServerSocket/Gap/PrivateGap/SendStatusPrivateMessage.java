package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.bardiademon.CyrusJSON;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;

public final class SendStatusPrivateMessage extends Thread implements Runnable
{
    private final Type type;
    private final NewPrivateMessage.ForSendToClient forSendToClient;

    public SendStatusPrivateMessage (final NewPrivateMessage.ForSendToClient _ForSendToClient , final Type _Type)
    {
        forSendToClient = _ForSendToClient;
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
        if (forSendToClient.gap != null && type != null)
        {
            SIServer.LoopOnline ((CodeOnline , _Online) ->
            {
                if (forSendToClient.from.getId () == _Online.getMainAccount ().getId ())
                {
                    final CyrusJSON statusMessage = new CyrusJSON ();
                    statusMessage.put (KeyAnswer.personal_gap_id , forSendToClient.gap.getPersonalGaps ().getId ());
                    statusMessage.put (KeyAnswer.gap_id , forSendToClient.gap.getId ());
                    statusMessage.put (KeyAnswer.status , type);
                    _Online.getClient ().sendEvent (EventName.ssg_status_message.name () , statusMessage.toString ());
                    return false;
                }
                else return true;
            });
        }
    }

    private enum KeyAnswer
    {
        gap_id, status, personal_gap_id
    }

    public enum Type
    {
        delivered, read, send;

        public static Type to (final String name)
        {
            try
            {
                return valueOf (name);
            }
            catch (Exception e)
            {
                l.n (Thread.currentThread ().getStackTrace () , new Exception (name));
                return null;
            }
        }
    }
}
