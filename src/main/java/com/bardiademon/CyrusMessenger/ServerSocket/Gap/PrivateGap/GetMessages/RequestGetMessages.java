package com.bardiademon.CyrusMessenger.ServerSocket.Gap.PrivateGap.GetMessages;

import com.bardiademon.CyrusMessenger.Model.Database.Default.Default;
import com.bardiademon.CyrusMessenger.Model.Database.Default.DefaultKey;
import com.bardiademon.CyrusMessenger.Model.Database.Groups.Groups.Groups.Groups;
import com.bardiademon.CyrusMessenger.Model.Database.Users.Users.MainAccount.MainAccount;
import com.bardiademon.CyrusMessenger.ServerSocket.RestSocket.PublicRequest;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RequestGetMessages extends PublicRequest
{
    /**
     * id group or id user hast
     * <p>
     * agar message haye group ro mikhad id = group id va agar user ro mikhad id = user id (MainAccount)
     *
     * @see MainAccount
     * @see Groups
     * @see MainAccount#getId()
     * @see Groups#getId()
     */
    private long id;

    /**
     * type ke taeen konande inde ke darkhast baraye chi hast
     * <p>
     * string gozashtam chon ke har chizi gozasht ersal beshe bad inja test mishe va khata va log moshakhas ersal mishe
     * </p>
     *
     * @see Type
     * @see GetMessages
     * @see GetMessages#checkRequest()
     */
    private String type;

    /**
     * page baraye in ke masalan payam ha 1.000.000 tedadeshon hast hamasho nimigiram to ye darkhast
     * <p>
     * page bandi mishe va har page maslan 20 tasho migiram
     * <p>
     * dar table Default sabt shode in tedad ha
     *
     * @see GetMessages
     * @see GetMessages#checkRequest()
     * @see Default
     * @see DefaultKey#max_get_gaps
     */
    @JsonProperty ("page")
    private int page;

    @JsonProperty ("personal_gaps_id")
    private long personalGapsId;

    public enum Type
    {
        user, group;

        public static Type to (String name)
        {
            try
            {
                return valueOf (name);
            }
            catch (Exception e)
            {
                l.n (Thread.currentThread ().getStackTrace () , e , ToJson.CreateClass.nj ("name" , name));
                return null;
            }
        }
    }

    public RequestGetMessages ()
    {
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public int getPage ()
    {
        return page;
    }

    public void setPage (int page)
    {
        this.page = page;
    }

    public long getPersonalGapsId ()
    {
        return personalGapsId;
    }

    public void setPersonalGapsId (long personalGapsId)
    {
        this.personalGapsId = personalGapsId;
    }
}
