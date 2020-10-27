package com.bardiademon.CyrusMessenger.bardiademon;

public final class Pagination
{

    public Answer computing (final int Page , final long Size , final int NumberOfEachPage)
    {
        if (Size == NumberOfEachPage)
            return new Answer (1 , 1 , 0 , NumberOfEachPage);
        else
        {
            double result = (((double) Size) / ((double) NumberOfEachPage));

            String str = Double.toString (result);

            int allPage = 0;
            try
            {
                if (Integer.parseInt (str.substring (str.indexOf (".") + 1)) > 0)
                    allPage = ((int) result + 1);
            }
            catch (Exception e)
            {
                allPage = ((int) result);
            }

            if (allPage <= 0) allPage = 1;

            if (Page > allPage) return new Answer ();

            int end = NumberOfEachPage * Page;
            int start = (Math.abs (end - NumberOfEachPage));

            if (Page > 1) start++;

            if (end > Size) return new Answer (1 , 1 , start , (int) Size);

            return new Answer (allPage , Page , start , end);
        }
    }

    public static final class Answer
    {
        public final int AllPage, ThisPage, Start, End;

        private Answer ()
        {
            this (0 , 0 , 0 , 0);
        }

        private Answer (final int AllPage , final int ThisPage , final int Start , final int End)
        {
            this.AllPage = AllPage;
            this.ThisPage = ThisPage;
            this.Start = Start;
            this.End = End;
        }

        @Override
        public String toString ()
        {
            return "Answer{" +
                    "AllPage=" + AllPage +
                    ", ThisPage=" + ThisPage +
                    ", Start=" + Start +
                    ", End=" + End +
                    '}';
        }
    }
}
