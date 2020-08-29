package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Groups.Security.Management.AddManager;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.bardiademon.ID;
import com.bardiademon.CyrusMessenger.bardiademon.Str;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RequestNewManager
{
    @JsonIgnore
    public static final String DO_NOT_SET = "DO_NOT_SET";

    @JsonProperty ("id_group")
    private ID idGroup;

    /**
     * not set => DO_NOT_SET
     */
    private String name = DO_NOT_SET;

    @JsonProperty ("id_user")
    private ID idUser;

    @JsonProperty ("dismiss_user")
    private String dismissUser;

    @JsonProperty ("delete_message_user")
    private String delMessageUser;

    @JsonProperty ("add_admin")
    private String addAdmin;

    @JsonProperty ("del_admin")
    private String delAdmin;

    @JsonProperty ("change_management_access_level")
    private String changeManagementAccessLevel;

    @JsonProperty ("change_name_group")
    private String changeNameGroup;

    @JsonProperty ("change_bio")
    private String changeBio;

    @JsonProperty ("change_link")
    private String changeLink;

    @JsonProperty ("change_description")
    private String changeDescription;

    @JsonProperty ("temporarily_closed")
    private String temporarilyClosed;

    @JsonProperty ("upload_picture")
    private String uploadPicture;

    @JsonProperty ("del_picture")
    private String delPicture;

    @JsonProperty ("set_main_picture")
    private String setMainPicture;

    @JsonProperty ("change_picture")
    private String changePicture;

    @JsonProperty ("del_main_pic")
    private String delMainPic;

    @JsonProperty ("show_list_member")
    private String showListMember;

    @JsonProperty ("add_member")
    private String addMember;

    @JsonProperty ("send_message")
    private String sendMessage;

    /**
     * karbarani ke ghesmat security (show_in_group) ra false gozashtand
     */
    @JsonProperty ("show_member_hidden")
    private String showMemberHidden;

    public RequestNewManager ()
    {
    }


    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }


    public ID getIdGroup ()
    {
        return idGroup;
    }

    public void setIdGroup (ID idGroup)
    {
        this.idGroup = idGroup;
    }

    public ID getIdUser ()
    {
        return idUser;
    }

    public void setIdUser (ID idUser)
    {
        this.idUser = idUser;
    }

    public String getDismissUser ()
    {
        return dismissUser;
    }

    public void setDismissUser (String dismissUser)
    {
        this.dismissUser = dismissUser;
    }

    public String getDelMessageUser ()
    {
        return delMessageUser;
    }

    public void setDelMessageUser (String delMessageUser)
    {
        this.delMessageUser = delMessageUser;
    }

    public String getAddAdmin ()
    {
        return addAdmin;
    }

    public void setAddAdmin (String addAdmin)
    {
        this.addAdmin = addAdmin;
    }

    public String getDelAdmin ()
    {
        return delAdmin;
    }

    public void setDelAdmin (String delAdmin)
    {
        this.delAdmin = delAdmin;
    }

    public String getChangeManagementAccessLevel ()
    {
        return changeManagementAccessLevel;
    }

    public void setChangeManagementAccessLevel (String changeManagementAccessLevel)
    {
        this.changeManagementAccessLevel = changeManagementAccessLevel;
    }

    public String getChangeNameGroup ()
    {
        return changeNameGroup;
    }

    public void setChangeNameGroup (String changeNameGroup)
    {
        this.changeNameGroup = changeNameGroup;
    }

    public String getChangeBio ()
    {
        return changeBio;
    }

    public void setChangeBio (String changeBio)
    {
        this.changeBio = changeBio;
    }

    public String getChangeLink ()
    {
        return changeLink;
    }

    public void setChangeLink (String changeLink)
    {
        this.changeLink = changeLink;
    }

    public String getChangeDescription ()
    {
        return changeDescription;
    }

    public void setChangeDescription (String changeDescription)
    {
        this.changeDescription = changeDescription;
    }

    public String getTemporarilyClosed ()
    {
        return temporarilyClosed;
    }

    public void setTemporarilyClosed (String temporarilyClosed)
    {
        this.temporarilyClosed = temporarilyClosed;
    }

    public String getUploadPicture ()
    {
        return uploadPicture;
    }

    public void setUploadPicture (String uploadPicture)
    {
        this.uploadPicture = uploadPicture;
    }

    public String getDelPicture ()
    {
        return delPicture;
    }

    public void setDelPicture (String delPicture)
    {
        this.delPicture = delPicture;
    }

    public String getSetMainPicture ()
    {
        return setMainPicture;
    }

    public void setSetMainPicture (String setMainPicture)
    {
        this.setMainPicture = setMainPicture;
    }

    public String getChangePicture ()
    {
        return changePicture;
    }

    public void setChangePicture (String changePicture)
    {
        this.changePicture = changePicture;
    }

    public String getDelMainPic ()
    {
        return delMainPic;
    }

    public void setDelMainPic (String delMainPic)
    {
        this.delMainPic = delMainPic;
    }

    public String getShowListMember ()
    {
        return showListMember;
    }

    public void setShowListMember (String showListMember)
    {
        this.showListMember = showListMember;
    }

    public String getShowMemberHidden ()
    {
        return showMemberHidden;
    }

    public void setShowMemberHidden (String showMemberHidden)
    {
        this.showMemberHidden = showMemberHidden;
    }

    public String getAddMember ()
    {
        return addMember;
    }

    public void setAddMember (String addMember)
    {
        this.addMember = addMember;
    }

    public String getSendMessage ()
    {
        return sendMessage;
    }

    public void setSendMessage (String sendMessage)
    {
        this.sendMessage = sendMessage;
    }

    @Nullable
    public AnswerToClient checkRequest ()
    {
        if (checkBool (getAddAdmin ())) return notBool ("add_admin");
        else if (checkBool (getChangeBio ())) return notBool ("change_bio");
        else if (checkBool (getChangeDescription ())) return notBool ("change_description");
        else if (checkBool (getChangeLink ())) return notBool ("change_link");
        else if (checkBool (getChangeManagementAccessLevel ())) return notBool ("change_management_access_level");
        else if (checkBool (getChangeNameGroup ())) return notBool ("change_name_group");
        else if (checkBool (getChangePicture ())) return notBool ("change_picture");
        else if (checkBool (getDelMainPic ())) return notBool ("del_main_pic");
        else if (checkBool (getDismissUser ())) return notBool ("dismiss_user");
        else if (checkBool (getDelMessageUser ())) return notBool ("del_message_user");
        else if (checkBool (getDelPicture ())) return notBool ("del_picture");
        else if (checkBool (getSetMainPicture ())) return notBool ("set_main_pic");
        else if (checkBool (getShowListMember ())) return notBool ("show_list_member");
        else if (checkBool (getTemporarilyClosed ())) return notBool ("temporarily_closed");
        else if (checkBool (getShowMemberHidden ())) return notBool ("show_member_hidden");
        else if (checkBool (getUploadPicture ())) return notBool ("upload_picture");
        else if (checkBool (getAddMember ())) return notBool ("add_member");
        else if (checkBool (getSendMessage ())) return notBool ("send_message");
        else return null;
    }

    private boolean checkBool (String str)
    {
        return !Str.IsEmpty (str) && !Str.HasBool (str);
    }

    @NotNull
    private AnswerToClient notBool (String which)
    {
        AnswerToClient answerToClient = AnswerToClient.OneAnswer (AnswerToClient.error400 () , RestNewManager.ValAnswer.invalid_request);
        answerToClient.put (AnswerToClient.CUK.which.name () , which);
        return answerToClient;
    }

}
