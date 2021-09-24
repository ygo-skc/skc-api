package com.rtomyj.skc.enums;


public enum Rarity
{

	C("Common")
	, R("Rarity")
	, SR("Super Rare")
	, UR("Ultra Rare")
	, SCRT("Secret Rare");

	private final String rarityName;

	Rarity(final String rarityName) { this.rarityName = rarityName; }

	@Override
	public String toString()
	{
		return rarityName;
	}

}