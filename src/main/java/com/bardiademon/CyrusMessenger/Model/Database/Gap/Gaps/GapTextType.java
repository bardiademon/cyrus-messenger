package com.bardiademon.CyrusMessenger.Model.Database.Gap.Gaps;

import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;

public enum GapTextType
{
    question_yes_no, question, normal;

    public static GapTextType to (final String name)
    {
        try
        {
            return valueOf (name);
        }
        catch (Exception e)
        {
            l.n (Thread.currentThread ().getStackTrace () , new Exception (name) , GapTextType.class.getName ());
            return null;
        }
    }
}
