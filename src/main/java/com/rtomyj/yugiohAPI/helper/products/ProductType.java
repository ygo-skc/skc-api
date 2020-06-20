package com.rtomyj.yugiohAPI.helper.products;

public enum ProductType
{
	PACK("PACK")
	, DECK("DECK");

	private final String productType;

	ProductType(final String productType) { this.productType = productType; }

	@Override
	public String toString()
	{
		return productType;
	}
}