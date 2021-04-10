package com.rtomyj.skc.helper.util;

public final class StringUtil
{

    public static String concatenateStringsWithDelimiter(final String delimiter, final String... values)
    {

        String joinedStr = "";
        for (String value: values)
        {
            if (!value.isEmpty())
            {
                if (joinedStr.equals(""))	joinedStr += value;
                else	joinedStr += delimiter + value;
            }
        }

        return joinedStr;

    }

}
