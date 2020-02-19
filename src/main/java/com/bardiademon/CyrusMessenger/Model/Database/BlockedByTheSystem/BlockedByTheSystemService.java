package com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem;

import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestRepository;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest.SubmitRequestType;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public final class BlockedByTheSystemService
{
    public final BlockedByTheSystemRepository Repository;
    private SubmitRequestRepository submitRequestRepository;

    @Autowired
    public BlockedByTheSystemService (BlockedByTheSystemRepository Repository , SubmitRequestRepository _SubmitRequestRepository)
    {
        this.Repository = Repository;
        this.submitRequestRepository = _SubmitRequestRepository;
    }

    public BlockedByTheSystem isBlockedFor (long idUser , BlockedFor blockedFor , String description)
    {
        return checkIsBlockedFor (idUser , null , Repository.findByMainAccountIdAndBlockedForAndDescriptionAndActiveTrue (idUser , blockedFor , description));
    }

    public BlockedByTheSystem isBlockedFor (long idUser , BlockedFor blockedFor)
    {
        return checkIsBlockedFor (idUser , null , Repository.findByMainAccountIdAndBlockedForAndActiveTrue (idUser , blockedFor));
    }

    private BlockedByTheSystem checkIsBlockedFor (long idUser , String ip , BlockedByTheSystem blockedByTheSystem)
    {
        if (blockedByTheSystem == null) return null;
        else
        {
            if (Timestamp.valueOf (LocalDateTime.now ()).getTime () >= Timestamp.valueOf (blockedByTheSystem.getValidityTime ()).getTime ())
            {
                unBlocked (blockedByTheSystem , idUser , ip);
                return null;
            }
            else return blockedByTheSystem;
        }
    }

    public BlockedByTheSystem isBlockedFor (String ip , BlockedFor blockedFor , String description)
    {
        return checkIsBlockedFor (0 , ip , Repository.findByIpAndBlockedForAndDescriptionAndActiveTrue (ip , blockedFor , description));
    }

    public BlockedByTheSystem isBlockedFor (String ip , BlockedFor blockedFor)
    {
        return checkIsBlockedFor (0 , ip , Repository.findByIpAndBlockedForAndActiveTrue (ip , blockedFor));
    }

    public void newBlock (String ip , BlockedFor blockedFor , @Nullable String description , LocalDateTime validityTime)
    {
        newBlock (ip , blockedFor , validityTime , description);
    }

    public void newBlock (MainAccount mainAccount , BlockedFor blockedFor , @Nullable String description , LocalDateTime validityTime)
    {
        newBlock (mainAccount , blockedFor , validityTime , description);
    }

    public void newBlock (MainAccount mainAccount , BlockedFor blockedFor , LocalDateTime validityTime , @Nullable String description)
    {
        if (mainAccount != null && mainAccount.getId () > 0 && blockedFor != null && validityTime != null)
        {
            BlockedByTheSystem blockedByTheSystem = new BlockedByTheSystem ();
            blockedByTheSystem.setMainAccount (mainAccount);

            if (Str.IsEmpty (description)) Repository.deactive (mainAccount.getId () , blockedFor);
            else Repository.deactive (mainAccount.getId () , blockedFor , description);

            newBlock (blockedByTheSystem , blockedFor , validityTime , description);
        }
    }

    public void newBlock (String ip , BlockedFor blockedFor , LocalDateTime validityTime , @Nullable String description)
    {
        if (!Str.IsEmpty (ip) && blockedFor != null && validityTime != null)
        {
            BlockedByTheSystem blockedByTheSystem = new BlockedByTheSystem ();
            blockedByTheSystem.setIp (ip);

            if (Str.IsEmpty (description)) Repository.deactive (ip , blockedFor , description);
            else Repository.deactive (ip , blockedFor);

            newBlock (blockedByTheSystem , blockedFor , validityTime , description);
        }
    }

    private void newBlock (BlockedByTheSystem blockedByTheSystem , BlockedFor blockedFor , LocalDateTime validityTime , @Nullable String des)
    {
        blockedByTheSystem.setBlockedFor (blockedFor);
        blockedByTheSystem.setValidityTime (validityTime);
        blockedByTheSystem.setDescription (des);
        Repository.save (blockedByTheSystem);
    }


    private void unBlocked (BlockedByTheSystem blockedByTheSystem , long idUser , String ip)
    {
        if (blockedByTheSystem != null)
        {
            blockedByTheSystem.setUnBlockedAt (LocalDateTime.now ());
            blockedByTheSystem.setActive (false);
            Repository.save (blockedByTheSystem);
            try
            {
                SubmitRequestType submitRequestType = SubmitRequestType.valueOf (blockedByTheSystem.getDescription ());

                if (Str.IsEmpty (ip))
                {
                    if (idUser > 0) submitRequestRepository.deactiveAllRequest (idUser , submitRequestType);
                }
                else submitRequestRepository.deactiveAllRequest (ip , submitRequestType);

            }
            catch (Exception ignored)
            {
            }
        }
    }

    public List<BlockedByTheSystem> getListBlocksMainAccount ()
    {
        return Repository.findByActiveTrueAndMainAccountNotNull ();
    }
}
