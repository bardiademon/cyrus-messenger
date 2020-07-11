package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Contacts;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestContacts
{

    @JsonIgnore
    public static final int MIN_PHONE = 10;

    @JsonIgnore
    public static final int MAX_PHONE = 20;

    private String name, family, phone;

    @JsonInclude (JsonInclude.Include.NON_NULL)
    @JsonProperty ("id")
    private Long idContacts;

    @JsonInclude (JsonInclude.Include.NON_NULL)
    private String region;

    @JsonInclude (JsonInclude.Include.NON_NULL)
    @JsonProperty ("has_account")
    private boolean hasAccount;

    @JsonInclude (JsonInclude.Include.NON_NULL)
    @JsonProperty ("id_user")
    private Long idUserContacts;


    public RequestContacts ()
    {
    }

    public Long getIdContacts ()
    {
        return idContacts;
    }

    public void setIdContacts (Long idContacts)
    {
        this.idContacts = idContacts;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getFamily ()
    {
        return family;
    }

    public void setFamily (String family)
    {
        this.family = family;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getRegion ()
    {
        return region;
    }

    public void setRegion (String region)
    {
        this.region = region;
    }

    public boolean isHasAccount ()
    {
        return hasAccount;
    }

    public void setHasAccount (boolean hasAccount)
    {
        this.hasAccount = hasAccount;
    }

    public Long getIdUserContacts ()
    {
        return idUserContacts;
    }

    public void setIdUserContacts (Long idUserContacts)
    {
        this.idUserContacts = idUserContacts;
    }
}
