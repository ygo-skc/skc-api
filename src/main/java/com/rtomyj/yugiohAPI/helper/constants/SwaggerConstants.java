package com.rtomyj.yugiohAPI.helper.constants;

public class SwaggerConstants
{

    public static final String http200 = "Request processed successfully.";

    public static final String http400 = "Malformed request. Look at API spec and conform to expected pattern(s).";
    public static final String http404 = "No resource found for requested item.";

    public static final String RESULT_LIMIT_DESCRIPTION = "Max number of records/results to return.";
    public static final String SAVE_BANDWIDTH_DESCRIPTION = "If true, certain data values retrieved from DB will be truncated to save bandwidth on transit.";

    // Common Ban List endpoint Swagger descriptions
    public static final String BAN_LIST_FETCH_ALL_DESCRIPTION = "If true, compares desired ban list with the ban list it replaced (previous logical ban list) and returns the new cards added and the cards removed in the ban list transition. This option is equivalant to calling three endpoints - Banned Cards endpoint, Newly Banned endpoint and Removed endpoint.";
    public static final String BAN_LIST_START_DATE_DESCRIPTION = "Valid start date of a ban list stored in database. Must conform to yyyy-mm-dd format (use /api/v1/ban/dates to see a valid list of start dates).";

    // Common Card endpoint Swagger descriptions
    public static final String CARD_FETCH_ALL_DESCRIPTION = "If true, returns information about a card such as ban lists it was restricted (forbidden, limited or semi-limited) in and products it was included in. Otherwise, only basic info about the card is returned.";
    public static final String CARD_ID_DESCRIPTION = "ID given to mostly all YuGiOh cards by Konami that uniquely identifies it.";
    public static final String CARD_NAME_DESCRIPTION = "A mame of a YuGiOh card.";
    public static final String CARD_ATTRIBUTE_DESCRIPTION = "One of the six monster attributes (seven including 'Divine')";
    public static final String CARD_COLOR_DESCRIPTION = "A simple identifier for card type. If the card is synchro, the card color is synchro.";
    public static final String MONSTER_TYPE_DESCRIPTION = "Monster type such as Spellcaster.";

    // Swagger tag names
    public static final String TEST_CALL_TAG_NAME = "Test Call";
    public static final String BAN_LIST_TAG_NAME = "Ban List";
    public static final String TAG_CAR_TAG_NAMED = "Card Info";
    public static final String TAG_PRODUCT_TAG_NAME = "Product Info";

}
