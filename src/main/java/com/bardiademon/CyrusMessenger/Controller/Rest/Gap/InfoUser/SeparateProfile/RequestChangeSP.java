package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.InfoUser.SeparateProfile;

// SP => Separate Profile

import com.bardiademon.CyrusMessenger.Model.Database.EnumTypes.ETIdName;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public final class RequestChangeSP
{
    private ID id;
    private String bio, family, mylink, name, gender;

    @JsonProperty ("bio_null")
    private boolean bioNull;

    @JsonProperty ("family_null")
    private boolean familyNull;

    @JsonProperty ("emailNull")
    private boolean emailNull;

    @JsonProperty ("mylink_null")
    private boolean mylinkNull;

    @JsonProperty ("profile_for")
    private List <ETIdName> profileFor;

    @JsonProperty ("email_confirmed_code")
    private String emailConfirmedCode;

    public RequestChangeSP ()
    {
    }

    public ID getId ()
    {
        return id;
    }

    public void setId (ID id)
    {
        this.id = id;
    }

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
    }

    public String getFamily ()
    {
        return family;
    }

    public void setFamily (String family)
    {
        this.family = family;
    }

    public String getMylink ()
    {
        return mylink;
    }

    public void setMylink (String mylink)
    {
        this.mylink = mylink;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public List <ETIdName> getProfileFor ()
    {
        return profileFor;
    }

    public void setProfileFor (List <ETIdName> profileFor)
    {
        this.profileFor = profileFor;
    }

    public boolean isBioNull ()
    {
        return bioNull;
    }

    public void setBioNull (boolean bioNull)
    {
        this.bioNull = bioNull;
    }

    public boolean isFamilyNull ()
    {
        return familyNull;
    }

    public void setFamilyNull (boolean familyNull)
    {
        this.familyNull = familyNull;
    }

    public boolean isMylinkNull ()
    {
        return mylinkNull;
    }

    public void setMylinkNull (boolean mylinkNull)
    {
        this.mylinkNull = mylinkNull;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getEmailConfirmedCode ()
    {
        return emailConfirmedCode;
    }

    public void setEmailConfirmedCode (String emailConfirmedCode)
    {
        this.emailConfirmedCode = emailConfirmedCode;
    }

    public boolean isEmailNull ()
    {
        return emailNull;
    }

    public void setEmailNull (boolean emailNull)
    {
        this.emailNull = emailNull;
    }
}
