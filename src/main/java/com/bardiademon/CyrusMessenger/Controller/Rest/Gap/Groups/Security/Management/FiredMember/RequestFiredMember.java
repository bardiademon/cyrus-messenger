package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Groups.Security.Management.FiredMember;

import com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Groups.Security.Management.SuspendManager.RequestSuspendManager;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.GroupSecurity.FiredFromGroup.FiredFromGroup;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestFiredMember extends RequestSuspendManager
{
    private String why;

    @JsonProperty ("validity_time")
    private long validityTime = FiredFromGroup.DEFAULT_VALIDITY_TIME;

    public RequestFiredMember ()
    {
    }

    public String getWhy ()
    {
        return why;
    }

    public void setWhy (String why)
    {
        this.why = why;
    }

    public long getValidityTime ()
    {
        return validityTime;
    }

    public void setValidityTime (long validityTime)
    {
        this.validityTime = validityTime;
    }
}
