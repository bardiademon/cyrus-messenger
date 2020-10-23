package com.bardiademon.CyrusMessenger.Model.Database.Gap.Emojis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table (name = "gap_emojis")
public final class Emojis
{
    @Id
    @GeneratedValue
    @Column (unique = true, nullable = false)
    private long id;

    @Lob
    @Column (name = "emoji_code", nullable = false, unique = true)
    private String emojiCode;

    /**
     * tartip namajesh
     */
    @Column (name = "emoji_index")
    private int index = 0;

    @Column (name = "emoji_group", nullable = false)
    @Enumerated (EnumType.STRING)
    private EmojiGroups emojiGroup;

    @Lob
    @Column (name = "emoji_name", nullable = false)
    private String name;

    @Column (name = "group_color")
    private String groupColor;

    public Emojis ()
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

    public String getEmojiCode ()
    {
        return emojiCode;
    }

    public void setEmojiCode (String emojiCode)
    {
        this.emojiCode = emojiCode;
    }

    public int getIndex ()
    {
        return index;
    }

    public void setIndex (int index)
    {
        this.index = index;
    }

    public EmojiGroups getEmojiGroup ()
    {
        return emojiGroup;
    }

    public void setEmojiGroup (EmojiGroups emojiGroup)
    {
        this.emojiGroup = emojiGroup;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getGroupColor ()
    {
        return groupColor;
    }

    public void setGroupColor (String groupColor)
    {
        this.groupColor = groupColor;
    }
}
