package com.bardiademon.CyrusMessenger.Controller.Rest.Gap.Stickers;

import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public final class RequestCreateStickerGroup
{

    private String id;

    private String group_name;

    @JsonIgnore
    private MultipartFile group_image;

    private String description;

    @JsonProperty ("with_per")
    private String with_per;

    @JsonIgnore
    private boolean withPermission;

    private String licensed_users;

    @JsonIgnore
    private List <LicensedUsers> licensedUsers;

    public static final class LicensedUsers
    {
        private String username;
        private String type;

        @JsonIgnore
        private MainAccount mainAccount;

        @JsonIgnore
        private Groups groups;

        /**
         * for update
         */
        private boolean delete;

        public LicensedUsers ()
        {
        }

        public String getUsername ()
        {
            return username;
        }

        public void setUsername (String username)
        {
            this.username = username;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        @JsonIgnore
        public MainAccount getMainAccount ()
        {
            return mainAccount;
        }

        @JsonIgnore
        public void setMainAccount (MainAccount mainAccount)
        {
            this.mainAccount = mainAccount;
        }

        @JsonIgnore
        public Groups getGroups ()
        {
            return groups;
        }

        @JsonIgnore
        public void setGroups (Groups groups)
        {
            this.groups = groups;
        }

        public boolean isDelete ()
        {
            return delete;
        }

        public void setDelete (boolean delete)
        {
            this.delete = delete;
        }
    }

    public RequestCreateStickerGroup ()
    {
    }

    @JsonIgnore
    public boolean setLicensedUsers ()
    {
        try
        {
            JSONArray request = new JSONArray (getLicensed_users ());
            if (request.length () > 0)
            {
                licensedUsers = new ArrayList <> ();

                JSONObject json;
                for (int i = 0, len = request.length (); i < len; i++)
                {
                    json = request.getJSONObject (i);
                    LicensedUsers licensedUser = new LicensedUsers ();
                    licensedUser.setUsername (json.getString ("username"));
                    licensedUser.setType (json.getString ("type"));

                    licensedUsers.add (licensedUser);
                }
                return true;
            }
        }
        catch (JSONException e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , licensed_users);
        }
        return false;
    }

    @JsonIgnore
    public List <LicensedUsers> getLicensedUsers ()
    {
        return licensedUsers;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getGroup_name ()
    {
        return group_name;
    }

    public void setGroup_name (String group_name)
    {
        this.group_name = group_name;
    }

    public MultipartFile getGroup_image ()
    {
        return group_image;
    }

    public void setGroup_image (MultipartFile group_image)
    {
        this.group_image = group_image;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getWith_per ()
    {
        return with_per;
    }

    public void setWith_per (String with_per)
    {
        this.with_per = with_per;
    }

    public boolean isWithPermission ()
    {
        return withPermission;
    }

    public void setWithPermission (boolean withPermission)
    {
        this.withPermission = withPermission;
    }

    public String getLicensed_users ()
    {
        return licensed_users;
    }

    public void setLicensed_users (String licensed_users)
    {
        this.licensed_users = licensed_users;
    }
}
