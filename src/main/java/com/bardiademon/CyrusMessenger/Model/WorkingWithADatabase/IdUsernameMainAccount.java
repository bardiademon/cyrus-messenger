package com.bardiademon.CyrusMessenger.Model.WorkingWithADatabase;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Controller.Rest.Vaidation.VUsername;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.bardiademon.Str;

public class IdUsernameMainAccount
{
    private AnswerToClient answerToClient;
    private boolean valid;
    private MainAccountService service;
    private long idUser;
    private String username;

    private MainAccount mainAccount;

    public IdUsernameMainAccount (MainAccountService Service , long IdUser , String Username)
    {
        this.service = Service;
        this.idUser = IdUser;
        this.username = Username;
        valid = check ();
    }

    private boolean check ()
    {
        if (Str.IsEmpty (username) && idUser <= 0)
        {
            setAnswerToClient (ValAnswer.id_invalid);
            return false;
        }

        if (!Str.IsEmpty (username))
        {
            VUsername vUsername = new VUsername (username);
            if (!vUsername.check ())
            {
                setAnswerToClient (ValAnswer.username_invalid);
                return false;
            }

            long newIdUser = service.toId (username);
            if (newIdUser <= 0)
            {
                setAnswerToClient (ValAnswer.username_not_found);
                return false;
            }

            if (idUser > 0 && newIdUser != idUser)
            {
                setAnswerToClient (ValAnswer.id_username_not_match);
                return false;
            }
            else idUser = newIdUser;
        }

        if (idUser > 0)
        {
            mainAccount = service.findId (idUser);
            if (mainAccount == null)
            {
                setAnswerToClient (ValAnswer.id_not_found);
                return false;
            }
        }
        else
        {
            setAnswerToClient (ValAnswer.id_not_found);
            return false;
        }

        idUser = mainAccount.getId ();
        username = mainAccount.getUsername ().getUsername ();

        return true;
    }

    private void setAnswerToClient (ValAnswer valAnswer)
    {
        answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , valAnswer.name ());
    }

    public long getIdUser ()
    {
        return idUser;
    }

    public AnswerToClient getAnswerToClient ()
    {
        return answerToClient;
    }

    public boolean isValid ()
    {
        return valid;
    }

    public MainAccount getMainAccount ()
    {
        return mainAccount;
    }

    public String getUsername ()
    {
        return username;
    }

    private enum ValAnswer
    {
        username_invalid, id_invalid, username_not_found, id_not_found, id_username_not_match
    }
}
