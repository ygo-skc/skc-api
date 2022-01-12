package com.rtomyj.skc.enums;

import java.util.HashMap;
import java.util.Map;

public enum LinkArrow {

    TOP_LEFT("↖️", "Top Left")
    , TOP_CENTER("⬆️", "Top Center")
    , TOP_RIGHT("↗️", "Top Right")
    , MIDDLE_RIGHT("➡️", "Middle Right")
    , BOTTOM_RIGHT("↘️", "Bottom Right")
    , BOTTOM_CENTER("⬇️", "Bottom Center")
    , BOTTOM_LEFT("↙️", "Bottom Left")
    , MIDDLE_LEFT("⬅️", "Middle Left");

    private final String arrowEmoji;
    private final String arrow;

    private static final Map<String, LinkArrow> dbStringToEnumMap;


    LinkArrow(final String arrowEmoji, final String arrow)
    {
        this.arrowEmoji = arrowEmoji;
        this.arrow = arrow;
    }


    @Override
    public String toString() { return this.arrowEmoji; }


    static {
        dbStringToEnumMap = new HashMap<>();
        dbStringToEnumMap.put("T-L", LinkArrow.TOP_LEFT);
        dbStringToEnumMap.put("T-C", LinkArrow.TOP_CENTER);
        dbStringToEnumMap.put("T-R", LinkArrow.TOP_RIGHT);
        dbStringToEnumMap.put("M-R", LinkArrow.MIDDLE_RIGHT);
        dbStringToEnumMap.put("B-R", LinkArrow.BOTTOM_RIGHT);
        dbStringToEnumMap.put("B-C", LinkArrow.BOTTOM_CENTER);
        dbStringToEnumMap.put("B-L", LinkArrow.BOTTOM_LEFT);
        dbStringToEnumMap.put("M-L", LinkArrow.MIDDLE_LEFT);
    }


    public static LinkArrow transformDBStringToEnum(final String dbString) {
        return dbStringToEnumMap.get(dbString.toUpperCase());
    }
}
