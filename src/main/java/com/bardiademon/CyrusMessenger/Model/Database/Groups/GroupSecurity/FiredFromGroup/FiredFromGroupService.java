package com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.bardiademon.Time;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Service
public final class FiredFromGroupService
{
    public final FiredFromGroupRepository Repository;

    public FiredFromGroupService (FiredFromGroupRepository Repository)
    {
        this.Repository = Repository;
    }

    public FiredFromGroup isFired (long idGroup , long idUser)
    {
        FiredFromGroup firedFromGroup = Repository.findByGroupIdAndMainAccountIdAndFreedFalse (idGroup , idUser);
        if (firedFromGroup == null) return null;
        else
        {
            LocalDateTime validityTime = firedFromGroup.getValidityTime ();
            if (Time.BiggerNow (validityTime))
            {
                firedFromGroup.setFreed (true);
                firedFromGroup.setFreedAt (LocalDateTime.now ());
                Repository.save (firedFromGroup);
                return null;
            }
            else return firedFromGroup;
        }
    }

    public AnswerToClient createAnswerToClient (FiredFromGroup firedFromGroup)
    {
        return AnswerToClient.KeyAnswer (AnswerToClient.New (HttpServletResponse.SC_UNAUTHORIZED) ,
                AnswerToClient.CUK.answer.name () , ValKeyAnswer.fired.name () ,
                ValKeyAnswer.why.name () , firedFromGroup.getWhy () ,
                ValKeyAnswer.validity_time.name () , Time.toString (firedFromGroup.getValidityTime ())
        );
    }

    private enum ValKeyAnswer
    {
        fired, why, validity_time
    }
}
