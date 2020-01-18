package com.bardiademon.CyrusMessenger.bardiademon.SendMail;

import com.bardiademon.CyrusMessenger.bardiademon.Default.Default;

import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail
{
    private static final String SIGNATURE = "\n\n" + Default.NameEnglish;

    private static final String EMAIL = "bardiademon.cyrusmessenger@gmail.com",
            PASSWORD = "3+8|q9Vr49D7g|WW1yM6gR0|@D75~J6\\4|)p^2_3i~9_)4-(Ub_0R_4t|gr$8h=^M`50UuVq89bDcKJK6DK~V2t8)c@h`c5+C|M=";

    private String to;
    private String contentType;
    private String subject;
    private String msg;

    public static final String CONTENT_TEXT_PLAIN = "text/plain", CONTENT_TEXT_HTML = "text/html";

    public SendMail (String To)
    {
        this.to = To;
    }

    private boolean validContentType ()
    {
        return (contentType.equals (CONTENT_TEXT_PLAIN) || contentType.equals (CONTENT_TEXT_HTML));
    }

    public boolean send ()
    {
        if (!validContentType () || (to == null || to.isEmpty ()) || (subject == null || subject.isEmpty ()) || (msg == null || msg.isEmpty ()))
            return false;

        Properties properties = new Properties ();

        properties.put ("mail.smtp.auth" , "true");
        properties.put ("mail.smtp.starttls.enable" , "true");
        properties.put ("mail.smtp.host" , "smtp.gmail.com");
        properties.put ("mail.smtp.port" , "587");

        Session session = Session.getInstance (properties , new Authenticator ()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication ()
            {
                return new PasswordAuthentication (SendMail.EMAIL , SendMail.PASSWORD);
            }
        });

        try
        {
            Address from = new InternetAddress (EMAIL);
            Address to = new InternetAddress (this.to);

            Message message = new MimeMessage (session);
            message.addFrom (new Address[]{from});
            message.setRecipient (Message.RecipientType.TO , to);
            message.setContent ("" , contentType);
            message.setSubject (subject);
            message.setText (msg + SIGNATURE);

            Transport.send (message);
            return true;

        }
        catch (MessagingException ignored)
        {
            return false;
        }
    }

    public void setContentType (String contentType)
    {
        this.contentType = contentType;
    }

    public void setSubject (String subject)
    {
        this.subject = subject;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }
}
