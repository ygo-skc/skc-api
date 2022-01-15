package com.rtomyj.skc.enums;

public enum ProductType
{

	PACK("PACK")
	, DECK("DECK")
	, SET("SET");

	private final String product;

	ProductType(final String product) { this.product = product; }

	@Override
	public String toString()
	{
		return product;
	}

}