package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Security.CBSIL;
import com.bardiademon.CyrusMessenger.Model.Database.Gap.Online.Online;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.EventName.EventName;
import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;
import com.bardiademon.CyrusMessenger.ServerSocket.SIServer;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.corundumstudio.socketio.SocketIOClient;
import java.time.LocalDateTime;

public final class CheckPublicRequest
{
    public Client check (final PublicRequest request , final SocketIOClient client , final EventName eventName)
    {
        CBSIL cbsil = null;
        AnswerToClient answer = null;

        final Online online = SIServer.Onlines.get (request.getCodeOnline ());
        if (online != null)
        {
            online.setAnnouncementOfPresence (LocalDateTime.now ());
            online.setClient (client);
            SIServer.Onlines.replace (request.getCodeOnline () , online);
            cbsil = CBSIL.Both (request , request.getCodeLogin () , eventName.name ());
            if (!cbsil.isOk ()) answer = cbsil.getAnswerToClient ();
        }
        else
        {
            answer = AnswerToClient.NotLoggedIn ();
            answer.put (KeyAnswer.description.name () , ValAnswer.invalid_online_code.name ());
            l.n (ToJson.To (request) , null , answer , Thread.currentThread ().getStackTrace () , new Exception (ValAnswer.invalid_online_code.name ()) , null);
        }

        if (answer != null)
        {
            try
            {
                final EventName answerEvent = EventName.valueOf (String.format ("e_%s" , eventName.name ()));
                client.sendEvent (answerEvent.name () , ToJson.To (answer));
                l.n (Thread.currentThread ().getStackTrace () , ToJson.To (answer));
            }
            catch (Exception e)
            {
                l.n (Thread.currentThread ().getStackTrace () , e);
            }
        }

        return new Client (online , cbsil , request , answer == null);
    }

    private enum KeyAnswer
    {
        /*
         * tosihat baraye ke be client begam code online moshkel dare
         *
         * key description == ValAnswer.invalid_online_code
         */
        description
    }

    private enum ValAnswer
    {
        invalid_online_code
    }

    public static class Client
    {
        public final boolean ok;
        public final Online online;
        public final CBSIL cbsil;
        public final PublicRequest request;

        private MainAccount mainAccount;

        private Client ()
        {
            this (null , null , null , false);
        }

        /*
         * private hast chon niyaz nist classi az in class new kone faghat az variable ha estefade bayad beshe
         * variable ha ham final hast va niyazi be taghir nist
         */
        private Client (final Online _Online , final CBSIL _CBSIL , final PublicRequest Request , final boolean Ok)
        {
            this.online = _Online;
            this.cbsil = _CBSIL;
            this.request = Request;
            this.ok = Ok;

            if (ok)
            {
                /*
                 * vaghti ok == true bashe yani answer == null hast pas khataii vojod nadare
                 * baabarin cbsil != null && cbsil.isOk ()) == true hast
                 */
                assert cbsil != null;
                assert cbsil.isOk ();

                /*
                 * baraye dastresi rahat tar dar baghiye class ha inja mainAccount ro daryaft mikonam
                 */
                assert cbsil.getIsLogin () != null;
                mainAccount = cbsil.getIsLogin ().getVCodeLogin ().getMainAccount ();
            }
        }

        public MainAccount getMainAccount ()
        {
            return mainAccount;
        }
    }
}
