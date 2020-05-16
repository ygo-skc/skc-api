package com.rtomyj.yugiohAPI.helper.products;

public enum ProductType
{
	BOOSTER("BOOSTER")
	, STRUCTURE_DECK("STRUCTURE_DECK");

	private final String productType;

	ProductType(final String productType) { this.productType = productType; }

	@Override
	public String toString()
	{
		return productType;
	}
}