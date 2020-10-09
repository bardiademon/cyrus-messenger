package com.bardiademon.CyrusMessenger.Model.Database.Gap.Emojis;

public enum EmojiGroups
{
    smileys, love, people, gestures, activities, music_video, food,
    animals, nature, pictogram, arrow, objects, computer_desktop,
    clothes, transport, places, weather, clock, text, symbols, others, flags;

    public static EmojiGroups to (String name)
    {
        try
        {
            return valueOf (name);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
