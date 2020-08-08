package com.bardiademon.CyrusMessenger.Controller.Rest.Chat.InfoUser.Modify.ModifyInfoUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

// MIU => Modify Info User
public final class RequestMIU
{
    private String bio, name, family, mylink, gender;

    @JsonProperty ("code_confirm_phone")
    private String codeConfirmPhone;

    @JsonIgnore
    private List <String> message = null;

    @JsonIgnore
    private boolean updatedBio;

    @JsonIgnore
    private boolean updatedName;

    @JsonIgnore
    private boolean updatedFamily;

    @JsonIgnore
    private boolean updatedUsername;

    @JsonIgnore
    private boolean updatedMylink;

    @JsonIgnore
    private boolean updatedGender;

    @JsonIgnore
    private boolean updatePhone;

    public RequestMIU ()
    {
    }

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
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

    public String getMylink ()
    {
        return mylink;
    }

    public void setMylink (String mylink)
    {
        this.mylink = mylink;
    }

    public boolean isUpdatedBio ()
    {
        return updatedBio;
    }

    public void setUpdatedBio ()
    {
        this.updatedBio = true;
    }

    public boolean isUpdatedName ()
    {
        return updatedName;
    }

    public void setUpdatedName ()
    {
        this.updatedName = true;
    }

    public boolean isUpdatedFamily ()
    {
        return updatedFamily;
    }

    public void setUpdatedFamily ()
    {
        this.updatedFamily = true;
    }

    public boolean isUpdatedUsername ()
    {
        return updatedUsername;
    }

    public void setUpdatedUsername ()
    {
        this.updatedUsername = true;
    }

    public boolean isUpdatedMylink ()
    {
        return updatedMylink;
    }

    public void setUpdatedMylink ()
    {
        this.updatedMylink = true;
    }

    public void setUpdatedGender ()
    {
        this.updatedGender = true;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public boolean isUpdatedGender ()
    {
        return updatedGender;
    }

    public String getCodeConfirmPhone ()
    {
        return codeConfirmPhone;
    }

    public void setCodeConfirmPhone (String codeConfirmPhone)
    {
        this.codeConfirmPhone = codeConfirmPhone;
    }

    public boolean isUpdatePhone ()
    {
        return updatePhone;
    }

    public void setUpdatePhone ()
    {
        this.updatePhone = true;
    }

    public List <String> getMessage ()
    {
        return message;
    }

    public void setMessage (Message message)
    {
        if (this.message == null) this.message = new ArrayList <> ();
        this.message.add (message.name ());
    }

    public enum Message
    {
        duplicate_phone_number
    }
}
