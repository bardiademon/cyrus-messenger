package com.bardiademon.CyrusMessenger.Model;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCode;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.ConfirmCode.ConfirmCodeService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountService;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccountStatus;

import java.time.LocalDateTime;

public final class AfterConfirm
{
    private MainAccountService mainAccountService;
    private ConfirmCodeService confirmCodeService;
    private ConfirmCode confirmCode;

    public AfterConfirm (MainAccountService _MainAccountService , ConfirmCodeService _ConfirmCodeService , ConfirmCode _ConfirmCode)
    {
        this.mainAccountService = _MainAccountService;
        this.confirmCodeService = _ConfirmCodeService;
        this.confirmCode = _ConfirmCode;
    }

    public void confirm (ConfirmCodeFor confirmCodeFor)
    {
        confirmCode.setUsing (true);
        confirmCode.setTimeToConfirmed (LocalDateTime.now ());

        MainAccount mainAccount = confirmCode.getMainAccount ();

        if (confirmCodeFor == ConfirmCodeFor.phone && mainAccount.getEmail () != null)
        {
            ConfirmCode isConfirmedEmail = confirmCodeService.Repository.findBySendCodeToAndMainAccountIdAndConfirmCodeForAndConfirmedTrue
                    (mainAccount.getEmail () , mainAccount.getId () , ConfirmCodeFor.email);
            if (isConfirmedEmail == null)
                mainAccount.setStatus (MainAccountStatus.email_not_confirmed);
        }
        else mainAccount.setStatus (MainAccountStatus.active);

        confirmCodeService.Repository.save (confirmCode);
        mainAccountService.Repository.save (mainAccount);
    }
}
