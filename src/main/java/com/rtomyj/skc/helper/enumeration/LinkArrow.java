package com.rtomyj.skc.helper.enumeration;

public enum LinkArrow {

    TOP_LEFT("T-L", "Top Left")
    , TOP_CENTER("T-C", "Top Center")
    , TOP_RIGHT("T-R", "Top Right")
    , MIDDLE_RIGHT("M-R", "Middle Right")
    , BOTTOM_RIGHT("B-R", "Bottom Right")
    , BOTTOM_CENTER("B-C", "Bottom Center")
    , BOTTOM_LEFT("B-L", "Bottom Left")
    , MIDDLE_LEFT("M-L", "Middle Left");

    private String arrowAbbreviated;
    private String arrow;


    LinkArrow(final String arrowAbbreviated, final String arrow)
    {

        this.arrowAbbreviated = arrowAbbreviated;
        this.arrow = arrow;

    }


    @Override
    public String toString() { return this.arrow; }


    public String getArrowAbbreviated() { return this.arrowAbbreviated; }

}
