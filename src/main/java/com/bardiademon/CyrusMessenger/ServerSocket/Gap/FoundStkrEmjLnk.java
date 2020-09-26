package com.bardiademon.CyrusMessenger.ServerSocket.Gap;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// FoundStkrEmjLnk => Found Sticker Emoji Link
public final class FoundStkrEmjLnk
{
    // REGEX_FE => REGEX FIND EMOJI
    private static final String REGEX_FE = "EMOJI\\([0-9*a-z]*\\)";

    // REGEX_FE => REGEX FIND STICKER
    private static final String REGEX_FS = "STICKER\\([0-9*a-z]*\\)";

    // REGEX_FE => REGEX FIND LINK
    private static final String REGEX_FL = "(?:^|[\\W])((ht|f)tp(s?)://|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]*$~@!:/{};']*)";

    private final String str;

    private List <Found> emoji, sticker, link;

    private boolean foundEmoji, foundSticker, foundLink;

    public FoundStkrEmjLnk (String Str)
    {
        this.str = Str;
        find ();
    }

    private void find ()
    {
        link = find ((Pattern.compile (REGEX_FL , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)).matcher (str));
        sticker = find ((Pattern.compile (REGEX_FS , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)).matcher (str));
        emoji = find ((Pattern.compile (REGEX_FE , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)).matcher (str));

        foundLink = (link != null && link.size () > 0);
        foundSticker = (sticker != null && sticker.size () > 0);
        foundEmoji = (emoji != null && emoji.size () > 0);
    }

    private List <Found> find (Matcher matcher)
    {

        // s=>start , e=>end
        int s, e;
        List <Found> lst = null;

        while (matcher.find ())
        {
            if (lst == null) lst = new ArrayList <> ();
            s = matcher.start ();
            e = matcher.end ();
            lst.add (new Found (s , e , str.substring (s , e)));
        }
        return lst;
    }

    public boolean isFoundEmoji ()
    {
        return foundEmoji;
    }

    public boolean isFoundSticker ()
    {
        return foundSticker;
    }

    public boolean isFoundLink ()
    {
        return foundLink;
    }

    public List <Found> getEmoji ()
    {
        return emoji;
    }

    public List <Found> getSticker ()
    {
        return sticker;
    }

    public List <Found> getLink ()
    {
        return link;
    }

    public static class Found
    {
        private final int start, end;
        private final String value;

        public Found (final int start , final int end , final String value)
        {
            this.start = start;
            this.end = end;
            this.value = value;
        }

        public int getStart ()
        {
            return start;
        }

        public int getEnd ()
        {
            return end;
        }

        public String getValue ()
        {
            return value;
        }
    }
}
