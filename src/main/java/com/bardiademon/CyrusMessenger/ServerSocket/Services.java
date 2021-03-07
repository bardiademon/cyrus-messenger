package com.bardiademon.CyrusMessenger.ServerSocket;

import com.bardiademon.CyrusMessenger.This;
import com.bardiademon.CyrusMessenger.bardiademon.SmallSingleLetterClasses.l;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Services
{
    private final Map <String, Object> Services = new LinkedHashMap <> ();

    @SuppressWarnings ("unchecked")
    public <T> T Get (Class <T> aClass)
    {
        if (aClass == null)
        {
            l.n (Thread.currentThread ().getStackTrace () , "request is null");
            return null;
        }

        final String packageName = String.format ("%s.%s" , aClass.getPackageName () , aClass.getName ());
        if (Services.containsKey (packageName))
        {
            l.n (Thread.currentThread ().getStackTrace () , "Get Service <" + packageName + ">");
            return ((T) (Services.get (packageName)));
        }
        else
        {
            l.n (Thread.currentThread ().getStackTrace () , "put Service <" + packageName + ">");
            Services.put (packageName , This.Context ().getBean (aClass));
            return Get (aClass);
        }
    }
}
