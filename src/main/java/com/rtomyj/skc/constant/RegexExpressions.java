package com.rtomyj.skc.constant;


public class RegexExpressions
{
	private RegexExpressions()
	{
		throw new UnsupportedOperationException("Cannot create instance for class: " + this.getClass().toString());
	}


	public static final String DB_DATE_PATTERN = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	public static final String CARD_ID_PATTERN = "[0-9]{8}";
}