package com.rtomyj.skc.constant;

public class SwaggerConstants
{
    private SwaggerConstants()
    {
        throw new UnsupportedOperationException("Cannot create instance for class: " + this.getClass().toString());
    }


    // Success
    public static final String HTTP_200_SWAGGER_MESSAGE = "Request processed successfully.";


    // Client Error
    public static final String HTTP_400_SWAGGER_MESSAGE = "Malformed request. Look at API spec and conform to expected pattern(s).";
    public static final String HTTP_404_SWAGGER_MESSAGE = "No resource found for requested item.";


    public static final String RESULT_LIMIT_DESCRIPTION = "Max number of records/results to return.";
    public static final String SAVE_BANDWIDTH_DESCRIPTION = "If true, certain data values retrieved from DB will be truncated to save bandwidth on transit.";


    // Common Ban List endpoint Swagger descriptions
    public static final String BAN_LIST_FETCH_ALL_DESCRIPTION = "If true, compares desired ban list with the ban list it replaced (previous logical ban list) and returns the new cards added and the cards removed in the ban list transition. This option is equivalant to calling three endpoints - Banned Cards endpoint, Newly Banned endpoint and Removed endpoint.";
    public static final String BAN_LIST_START_DATE_DESCRIPTION = "Valid start date of a ban list stored in database. Must conform to yyyy-mm-dd format (use /api/v1/ban/dates to see a valid list of start dates).";
    public static final String RESTRICTED_IN_DESCRIPTION = "Information about the ban lists a card was restricted in. Restrictions are any of the ban list statuses (forbidden, limited, semi-limited).";
    public static final String PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION = "The start date of the previous logical ban list - in reference to the requested ban list - that is being used to compare differences in transitioning ban lists.";


    // Common Card endpoint Swagger descriptions
    public static final String CARD_FETCH_ALL_DESCRIPTION = "If true, returns information about a card such as ban lists it was restricted (forbidden, limited or semi-limited) in and products it was included in. Otherwise, only basic info about the card is returned.";
    public static final String CARD_ID_DESCRIPTION = "ID given to mostly all Yu-Gi-Oh! cards by Konami that uniquely identifies it.";
    public static final String CARD_NAME_DESCRIPTION = "The mame of a particular Yu-Gi-Oh! card.";
    public static final String CARD_ATTRIBUTE_DESCRIPTION = "One of the six monster attributes (seven including 'Divine')";
    public static final String CARD_COLOR_DESCRIPTION = "A simple identifier for card type. If the card is synchro, the card color is synchro.";
    public static final String MONSTER_TYPE_DESCRIPTION = "Monster type such as Spellcaster.";
    public static final String MONSTER_ASSOCIATION_DESCRIPTION = "Description off monsters associations such as Level, Link Rating, Rank, etc.";
    public static final String MONSTER_ATK_DESCRIPTION = "The attack stat of a monster card.";
    public static final String MONSTER_DEF_DESCRIPTION = "The defence stat of a monster card.";
    public static final String CARD_EFFECT_DESCRIPTION = "The text provided in a Yu-Gi-Oh! card either describing its effect or lore.";


    public static final String PRODUCT_LOCALE_DESCRIPTION = "A locale denoting products release region. As of now the only locale available is \"en\" but in the future other locales can be added to support products in other regions like Japan.";
    public static final String PRODUCTS_CARD_IS_FOUND_IN_DESCRIPTION = "Product information for all the products the card in question can be found in.";
    public static final String PRODUCT_ID_DESCRIPTION = "Unique identifier given by Konami to all products.";
    public static final String PRODUCT_NAME_DESCRIPTION = "Full product name.";
    public static final String PRODUCT_TYPE_DESCRIPTION = "A string identifier used by API/DB to separate or distinguish products.";
    public static final String PRODUCT_SUB_TYPE_DESCRIPTION = "A string identifier used by API/DB to separate or distinguish product types, ie, further distinguish products.";
    public static final String PRODUCT_RELEASE_DATE_DESCRIPTION = "Date product was released in locale specified by locale property.";
    public static final String PRODUCT_TOTAL_DESCRIPTION = "Total number of cards in the product.";
    public static final String PRODUCT_RARITY_STATS_DESCRIPTION = "Statistics on card rarities for the product.";
    public static final String PRODUCT_CONTENT_DESCRIPTION = "Actual content for product.";
    public static final String CARD_POSITION_IN_PRODUCT_DESCRIPTION = "The position of the card within the product.";
    public static final String CARD_RARITIES_FOR_POSITION_DESCRIPTION = "Set of possible rarities the card can be found in for the given product and position.";


    // Swagger tag names
    public static final String TEST_CALL_TAG_NAME = "Test Call";
    public static final String BAN_LIST_TAG_NAME = "Ban List";
    public static final String TAG_CAR_TAG_NAMED = "Card Info";
    public static final String TAG_PRODUCT_TAG_NAME = "Product Info";
    public static final String TAG_STATS_NAME = "Database Statistics";

}
