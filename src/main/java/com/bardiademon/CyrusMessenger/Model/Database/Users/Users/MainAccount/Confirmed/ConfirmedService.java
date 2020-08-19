package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.Confirmed;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmedService
{
    public final ConfirmedRepository Repository;
    private final ConfirmCodeService confirmCodeService;

    @Autowired
    public ConfirmedService (ConfirmedRepository Repository , ConfirmCodeService _ConfirmCodeService)
    {
        this.Repository = Repository;
        this.confirmCodeService = _ConfirmCodeService;
    }

    public boolean hasCode (String code)
    {
        return getConfirmedIsActiveConfirmed (code) != null;
    }

    public Confirmed getConfirmedIsActiveConfirmed (String code)
    {
        return Repository.findByCodeAndActiveTrueAndConfirmCodeConfirmedTrue (code);
    }

    public Confirmed hasCodeFor (String code , ConfirmCodeFor confirmCodeFor)
    {
        return Repository.findByCodeAndActiveTrueAndConfirmCodeConfirmedTrueAndConfirmedFor (code , confirmCodeFor);
    }

    public void deactive (String value , ConfirmCodeFor confirmedFor)
    {
        Confirmed confirmed = fromValue (value , confirmedFor);
        if (confirmed != null)
        {
            confirmed.setActive (false);
            ConfirmCode confirmCode = confirmed.getConfirmCode ();
            confirmCode.setDeleted (true);
            confirmCode.setDeletedAt (LocalDateTime.now ());

            if (confirmCode.getMainAccount () != null)
            {
                confirmCode.setId2 (confirmCode.getMainAccount ().getId ());
                confirmCode.setMainAccount (null);
            }

            Repository.save (confirmed);
            confirmCodeService.Repository.save (confirmCode);
        }
    }

    public Confirmed fromValue (String value)
    {
        return Repository.findByValueAndActiveTrue (value);
    }

    public Confirmed fromValue (String value , ConfirmCodeFor confirmedFor)
    {
        return Repository.findByCodeAndActiveTrueAndConfirmCodeConfirmedTrueAndConfirmedFor (value , confirmedFor);
    }
}
