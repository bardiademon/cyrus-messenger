package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers.StickerAccessLevel;

import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import com.bardiademon.CyrusMessenger.bardiademon.ToJson;

public enum StickerAccessLevelType
{
    group, user;

    public static StickerAccessLevelType to (String name)
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
