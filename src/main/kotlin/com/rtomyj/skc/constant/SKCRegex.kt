package com.rtomyj.skc.constant

object SKCRegex {
	const val DB_DATE = "[0-9]{4}-[0-9]{2}-[0-9]{2}"
	const val CARD_ID = "[0-9]{8}"
	const val PRODUCT_ID = "[a-zA-Z0-9]{3,4}(-SE)?" // TODO: update db to handle Special Editions more elegantly
	const val PRODUCT_TYPE = "[a-zA-Z]+"
	const val LOCALE = "[a-zA-Z]{2}"
}