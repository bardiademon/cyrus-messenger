package com.bardiademon.CyrusMessenger.Model.Database.Users.Users.SubmitRequest;

import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedByTheSystem;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedByTheSystemService;
import com.bardiademon.CyrusMessenger.Model.Database.BlockedByTheSystem.BlockedFor;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequest;
import com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest.NumberOfSubmitRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public final class SubmitRequestService
{
    public final SubmitRequestRepository Repository;
    public final NumberOfSubmitRequestRepository numberOfSubmitRequestRepository;
    public final BlockedByTheSystemService blockedByTheSystemService;

    private NumberOfSubmitRequest numberOfSubmitRequest;

    @Autowired
    public SubmitRequestService
            (SubmitRequestRepository Repository ,
             NumberOfSubmitRequestRepository _NumberOfSubmitRequestRepository ,
             BlockedByTheSystemService _BlockedByTheSystemService)
    {
        this.Repository = Repository;
        this.numberOfSubmitRequestRepository = _NumberOfSubmitRequestRepository;
        this.blockedByTheSystemService = _BlockedByTheSystemService;
    }

    public void newRequest (MainAccount mainAccount , SubmitRequestType type , boolean active)
    {
        deactiveAllRequestIfTime15 (mainAccount.getId () , type);

        SubmitRequest submitRequest = new SubmitRequest ();
        submitRequest.setMainAccount (mainAccount);
        submitRequest.setType (type);
        submitRequest.setActive (active);
        Repository.save (submitRequest);

        if (!active) deactiveAllRequest (mainAccount.getId () , type);

        if (exceedingTheLimit (mainAccount , type))
        {
            BlockedByTheSystem isBlocked
                    = blockedByTheSystemService.isBlockedFor (mainAccount.getId () , BlockedFor.submit_request , type.name ());
            if (isBlocked == null)
                blockedByTheSystemService.newBlock (mainAccount , BlockedFor.submit_request , type.name () , LocalDateTime.now ().plusMinutes (numberOfSubmitRequest.getNumberOfMinToBeBlocked ()));
            else
            {
                blockedByTheSystemService.newBlock
                        (mainAccount , BlockedFor.blocked_all_service , LocalDateTime.now ().plusMonths (1) , "Has requested a lot after being blocked again");
            }
        }
    }

    public void newRequest (String ip , SubmitRequestType type , boolean active)
    {
        deactiveAllRequestIfTime15 (ip , type);
        if (!active) deactiveAllRequest (ip , type);

        SubmitRequest submitRequest = new SubmitRequest ();
        submitRequest.setIp (ip);
        submitRequest.setType (type);
        submitRequest.setActive (active);
        Repository.save (submitRequest);

        if (exceedingTheLimit (ip , type))
        {
            BlockedByTheSystem isBlocked
                    = blockedByTheSystemService.isBlockedFor (ip , BlockedFor.submit_request , type.name ());
            if (isBlocked == null)
                blockedByTheSystemService.newBlock (ip , BlockedFor.submit_request , type.name () , LocalDateTime.now ().plusMinutes (numberOfSubmitRequest.getNumberOfMinToBeBlocked ()));
            else
            {
                blockedByTheSystemService.newBlock
                        (ip , BlockedFor.blocked_all_service , LocalDateTime.now ().plusMonths (1) , "Has requested a lot after being blocked again");
            }
        }
    }

    public void deactiveAllRequest (long idUser , SubmitRequestType type)
    {
        Repository.deactiveAllRequest (idUser , type);
    }

    public void deactiveAllRequestIfTime15 (long idUser , SubmitRequestType type)
    {
        Repository.deactiveAllRequest (idUser , type , LocalDateTime.now ().plusMinutes (15));
    }

    public void deactiveAllRequest (String ip , SubmitRequestType type)
    {
        Repository.deactiveAllRequest (ip , type);
    }

    public void deactiveAllRequestIfTime15 (String ip , SubmitRequestType type)
    {
        Repository.deactiveAllRequest (ip , type , LocalDateTime.now ().plusMinutes (15));
    }

    public boolean exceedingTheLimit (MainAccount mainAccount , SubmitRequestType type)
    {
        numberOfSubmitRequest = numberOfSubmitRequestRepository.findByType (type);
        if (numberOfSubmitRequest == null) return false;
        else
        {
            int numberOfRequest = numberOfSubmitRequest.getNumberOfRequest ();
            if (numberOfRequest > 0)
                return (Repository.countByTypeAfterAndMainAccountIdAndActiveTrue (type , mainAccount.getId ()) >= numberOfSubmitRequest.getNumberOfRequest ());
            else return false;
        }
    }

    public boolean exceedingTheLimit (String ip , SubmitRequestType type)
    {
        numberOfSubmitRequest = numberOfSubmitRequestRepository.findByType (type);
        if (numberOfSubmitRequest == null) return false;
        else
            return (Repository.countByTypeAndIpAndActiveTrue (type , ip) >= numberOfSubmitRequest.getNumberOfRequest ());
    }

}
