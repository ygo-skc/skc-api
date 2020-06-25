package com.rtomyj.yugiohAPI.model.banlist;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.rtomyj.yugiohAPI.model.banlist.BanListComparisonResults;
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