package com.rtomyj.yugiohAPI.helper.enumeration;

import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import lombok.Getter;

@Getter
public enum SwaggerTagEnum {
    TEST_CALL_TAG(SwaggerConstants.SWAGGER_TAG_TEST_CALL, "Simple test to see if api is up and running.")
    , BAN_LIST_TAG(SwaggerConstants.SWAGGER_TAG_BAN_LIST, "Request information about current and past ban lists.");

    private String tagName, tagDescription;


    private SwaggerTagEnum(final String tagName, final String tagDescription)
    {

        this.tagName = tagName;
        this.tagDescription = tagDescription;

    }

}
