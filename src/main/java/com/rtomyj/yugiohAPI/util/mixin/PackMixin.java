package com.rtomyj.yugiohAPI.util.mixin;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rtomyj.yugiohAPI.model.product.pack.PackContent;

public abstract class PackMixin
{

	@JsonCreator
	public PackMixin(
		@JsonProperty("packId") final String productId
	) {}

	@JsonProperty("packId")
	private String productId;
	@JsonProperty("packLocale")
	private String productLocale;
	@JsonProperty("productName")
	private String productName;
	@JsonProperty("productType")
	private String productType;


	@JsonProperty("packReleaseDate")
	private Date productReleaseDate;

	@JsonProperty("packTotal")
	private Integer productTotal;

	@JsonProperty("packRarityCount")
	private Map<String, Integer> productRarityCount;
	@JsonProperty("packContent")
	private List<PackContent> productContent;

}