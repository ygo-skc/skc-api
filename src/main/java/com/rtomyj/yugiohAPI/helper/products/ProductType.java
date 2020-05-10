package com.rtomyj.yugiohAPI.helper.products;

public enum ProductType
{
	BOOSTER("Booster");

	private final String productType;

	ProductType(final String productType) { this.productType = productType; }

	@Override
	public String toString()
	{
		return productType;
	}
}