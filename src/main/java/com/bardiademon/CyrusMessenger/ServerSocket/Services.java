package com.bardiademon.CyrusMessenger.ServerSocket;

import com.bardiademon.CyrusMessenger.ThisApp;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Services
{
    private final Map <String, Object> Services = new LinkedHashMap <> ();

    public Object getService (Class <?> aClass)
    {
        if (aClass == null) return null;

        String packageName = String.format ("%s.%s" , aClass.getPackageName () , aClass.getName ());
        if (Services.containsKey (packageName))
            return Services.get (packageName);
        else
        {
            Services.put (packageName , ThisApp.Context ().getBean (aClass));
            return getService (aClass);
        }
    }
}
