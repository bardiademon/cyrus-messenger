package com.bardiademon.CyrusMessenger.Model.Database.NumberOfSubmitRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class NumberOfSubmitRequestService
{

    public final NumberOfSubmitRequestRepository Repository;

    @Autowired
    public NumberOfSubmitRequestService (NumberOfSubmitRequestRepository Repository)
    {
        this.Repository = Repository;
    }
}
