package com.bardiademon.CyrusMessenger.Model.Database.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class LogService
{
    public final LogRepository Repository;

    @Autowired
    public LogService (LogRepository Repository)
    {
        this.Repository = Repository;
    }
}
