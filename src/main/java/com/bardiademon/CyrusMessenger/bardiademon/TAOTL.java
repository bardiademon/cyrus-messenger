package com.bardiademon.CyrusMessenger.bardiademon;

import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import java.io.IOException;
import java.net.URL;

// TAOTL => The authenticity of the link
public final class TAOTL
{

    public boolean isLink (String link)
    {
        try
        {
            if (link.substring (0 , 7).equals ("http://") || link.substring (0 , 8).equals ("https://"))
            {
                ((new URL (link)).openConnection ()).connect ();
                return true;
            }
            else throw new IOException ("Http or https nof found");
        }
        catch (Exception e)
        {
            l.n (Thread.currentThread ().getStackTrace () , e , TAOTL.class.getName ());
            return false;
        }
    }
}
