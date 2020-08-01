package com.rtomyj.yugiohAPI.helper.enumeration.products;


public enum Rarity
{
	C("Common")
	, R("Rarity")
	, SR("Super Rare")
	, UR("Ultra Rare")
	, SCTR("Secret Rare");

	private final String rarity;

	Rarity(final String rarity) { this.rarity = rarity; }

	@Override
	public String toString()
	{
		return rarity;
	}
}