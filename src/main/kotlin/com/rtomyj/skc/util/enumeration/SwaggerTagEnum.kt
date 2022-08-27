package com.rtomyj.skc.util.enumeration

import com.rtomyj.skc.util.constant.SwaggerConstants
//import springfox.documentation.service.Tag

enum class SwaggerTagEnum(
	val tagName: String, val tagDescription: String
) {
	TEST_CALL_TAG(
		SwaggerConstants.TEST_CALL_TAG_NAME,
		"Simple test to see if api is up and running."
	),
	BAN_LIST_TAG(
		SwaggerConstants.BAN_LIST_TAG_NAME,
		"Request information about current or past ban lists."
	),
	CARD_TAG(
		SwaggerConstants.TAG_CARD_TAG_NAMED,
		"Request information on a card, search for cards or get browse data for cards."
	),
	PRODUCT_TAG(
		SwaggerConstants.TAG_PRODUCT_TAG_NAME,
		"Request information on products: browse available products, get product info."
	),
	STATISTICS_TAG(
		SwaggerConstants.TAG_STATS_NAME,
		"See information about the data currently in the Database."
	);


//	fun tag(): Tag = Tag(this.tagName, this.tagDescription)
}