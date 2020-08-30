package com.rtomyj.yugiohAPI.helper.enumeration;

import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import lombok.Getter;

@Getter
public enum SwaggerTagEnum {
    TEST_CALL_TAG(SwaggerConstants.TEST_CALL_TAG_NAME, "Simple test to see if api is up and running.")
    , BAN_LIST_TAG(SwaggerConstants.BAN_LIST_TAG_NAME, "Request information about current and past ban lists.")
    , CARD_TAG(SwaggerConstants.TAG_CAR_TAG_NAMED, "Request information on a card, search for cards or get browse data for cards.")
    , PRODUCT_TAG(SwaggerConstants.TAG_PRODUCT_TAG_NAME, "Request information on a product, browse available products.");

    private String tagName, tagDescription;


    private SwaggerTagEnum(final String tagName, final String tagDescription)
    {

        this.tagName = tagName;
        this.tagDescription = tagDescription;

    }

}
