package com.bardiademon.CyrusMessenger;

import com.bardiademon.CyrusMessenger.Controller.AnswerToClient;
import com.bardiademon.CyrusMessenger.Interface.bardiademon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Code
{

    private List<Character> finalResult;
    private boolean endCreatePassword = true;

    private int _charInt = 8;
    private boolean isNumber, isLowercaseLetters, isCapitalLetters, isOther;

    private String code;

    private Code (int NumberOfChar , boolean Number , boolean LowercaseLetters , boolean CapitalLetters , boolean Other)
    {
        if (NumberOfChar > 8) this._charInt = NumberOfChar;
        this.isNumber = Number;
        this.isLowercaseLetters = LowercaseLetters;
        this.isCapitalLetters = CapitalLetters;
        this.isOther = Other;
    }

    public static long CodeNumber ()
    {
        return CodeNumber (0);
    }

    public static long CodeNumber (int NumberOfChar)
    {
        Code code = new Code (NumberOfChar , true , false , false , false);
        code.createCode ();
        return (Long.parseLong (code.getCode ()));
    }


    public static void CreateCodeIsNotExists (Code _Code , int NumberOfCreate , CreateCode _CreateCode)
    {
        int counter = 0;
        while ((counter++) <= NumberOfCreate)
        {
            _Code.createCode ();
            if (_CreateCode.AfterCreate (_Code.getCode () , (counter >= NumberOfCreate))) return;
        }
    }

    public interface CreateCode
    {
        boolean AfterCreate (String code , boolean last);
    }

    public static String CreateCode ()
    {
        Code code = new Code (8 , true , true , true , true);
        code.createCode ();
        return code.getCode ();
    }

    // OF => Other false
    public static String CreateCodeOF ()
    {
        Code code = new Code (8 , true , true , true , false);
        code.createCode ();
        return code.getCode ();
    }    // OF => Other false

    public static Code GetCodeOF ()
    {
        return new Code (8 , true , true , true , false);
    }

    public static Code GetCodeNumber ()
    {
        return new Code (8 , true , false , false , false);
    }

    public static String CreateCodeLogin ()
    {
        Code code = new Code (100 , true , true , true , false);
        code.createCode ();
        return code.getCode ();
    }

    public static String Name ()
    {
        Code code = new Code (50 , true , true , true , false);
        code.createCode ();
        return code.getCode ();
    }

    @bardiademon
    public void createCode ()
    {
        if (endCreatePassword)
        {
            endCreatePassword = false;

            String number = null, capitalLetters = null, lowercaseLetters = null, other = null;

            if (!isNumber && !isLowercaseLetters && !isCapitalLetters)
            {
                isNumber = true;
                isLowercaseLetters = true;
                isCapitalLetters = true;
            }

            if (isNumber)
            {
                number = createPassNumber (_charInt);
                number = gettingTheSizeRequested (number , _charInt);
            }

            if (isLowercaseLetters)
            {
                lowercaseLetters = createPassCOrLLetters (_charInt , false);
                lowercaseLetters = gettingTheSizeRequested (lowercaseLetters , _charInt);
            }
            if (isCapitalLetters)
            {
                capitalLetters = createPassCOrLLetters (_charInt , true);
                capitalLetters = gettingTheSizeRequested (capitalLetters , _charInt);
            }
            if (isOther)
            {
                other = createOther (_charInt);
                other = gettingTheSizeRequested (other , _charInt);
            }


            finalResult = new ArrayList<> ();

            if (isNumber && number != null) setFinalResult (number);
            if (isLowercaseLetters && lowercaseLetters != null) setFinalResult (lowercaseLetters);
            if (isCapitalLetters && capitalLetters != null) setFinalResult (capitalLetters);
            if (isOther && other != null) setFinalResult (other);

            Collections.shuffle (finalResult);

            code = gettingTheSizeRequested (_charInt);

            endCreatePassword = true;
        }

    }

    @bardiademon
    private void setFinalResult (String created)
    {
        char[] chars = created.toCharArray ();
        for (char aChar : chars) finalResult.add (aChar);
    }

    @bardiademon
    private String createOther (int _char)
    {
        final String[] other = {"~" , "`" , "!" , "@" , "#" , "$" , "%" , "^" , "&" , "*" , "(" , ")" , "-" , "_" , "=" , "+" , "\\" , "|" , "."};

        StringBuilder result = new StringBuilder ();

        Random random = new Random ();
        for (int i = 0; i < _char; i++)
        {
            int index = random.nextInt (other.length);
            result.append (other[index]);
        }
        return result.toString ();
    }

    @bardiademon
    private String createPassNumber (int _char)
    {
        Random random = new Random ();

        List<Long> lstNumber = new ArrayList<> ();
        for (int i = 0; i < _char; i++) lstNumber.add (Math.abs (random.nextLong ()));

        Collections.shuffle (lstNumber);

        StringBuilder number = new StringBuilder ();
        for (Long integer : lstNumber) number.append (integer);

        return number.toString ();
    }

    /**
     * capitalOrLower => true = capital | false = Lower
     */
    @bardiademon
    private String createPassCOrLLetters (int _char , boolean capitalOrLower)
    {
        int max, min;

        if (capitalOrLower)
        {
            min = 65;
            max = 90;
        }
        else
        {
            min = 97;
            max = 122;
        }

        Random random = new Random ();

        List<String> lstStr = new ArrayList<> ();
        for (int i = 0; i < _char; i++)
        {
            char c = (char) (random.nextInt ((max - min) + 1) + min);
            lstStr.add (String.valueOf (c));
        }
        Collections.shuffle (lstStr);

        StringBuilder result = new StringBuilder ();
        for (String str : lstStr) result.append (str);
        return result.toString ();
    }

    @bardiademon
    private String gettingTheSizeRequested (int _char)
    {
        Random random = new Random ();

        StringBuilder result = new StringBuilder ();

        for (int i = 0; i < _char; i++)
            result.append (finalResult.get (random.nextInt (finalResult.size ())));
        return result.toString ();
    }

    @bardiademon
    private String gettingTheSizeRequested (String created , int _char)
    {
        Random random = new Random ();

        StringBuilder result = new StringBuilder ();

        char[] createdChar = created.toCharArray ();
        for (int i = 0; i < _char; i++)
            result.append (createdChar[random.nextInt (createdChar.length)]);
        return result.toString ();
    }

    public String getCode ()
    {
        return code;
    }
}
