package com.bardiademon.CyrusMessenger.bardiademon;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Hash256
{
    public String hash (String str)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance ("SHA-256");
            byte[] messageDigest = md.digest (str.getBytes ());
            BigInteger no = new BigInteger (1 , messageDigest);
            StringBuilder hashText = new StringBuilder ((no.toString (16)));
            while (hashText.length () < 32) hashText.insert (0 , "0");
            return hashText.toString ();
        }
        catch (NoSuchAlgorithmException e)
        {
            return str;
        }
    }

    public String hash (String str1 , String str2)
    {
        str1 = hash (str1);
        str2 = hash (str2);

        char[] charsStr1 = str1.toCharArray ();
        char[] charsStr2 = str2.toCharArray ();

        StringBuilder result = new StringBuilder ();
        for (char chStr1 : charsStr1)
            for (char chStr2 : charsStr2) result.append (hash (String.valueOf ((int) chStr1) + ((int) chStr2)));

        result = new StringBuilder (hash (result.toString ()));

        for (char chStr2 : charsStr2)
            for (char chStr1 : charsStr1) result.append (hash (String.valueOf ((int) chStr1) + ((int) chStr2)));

        return hash (result.toString ());
    }
}
