package com.bardiademon.CyrusMessenger.Model.Database.Default;

import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;

public class DefaultException extends Exception
{
    public DefaultException (String message)
    {
        super (message);
        l.n (Thread.currentThread ().getStackTrace () , this , DefaultException.class.getName ());
    }
}
