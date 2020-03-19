package com.rtomyj.yugiohAPI.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "All new cards separated by status.")
@JsonPropertyOrder({ "numForbidden", "numLimited", "numSemiLimited", "forbidden", "limited", "semiLimited" })
public class NewCards
{
	private int numForbidden;
	private int numLimited;
	private int numSemiLimited;

	private List<BanListComparisonResults> forbidden;
	private List<BanListComparisonResults> limited;
	private List<BanListComparisonResults> semiLimited;
}