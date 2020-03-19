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
@ApiModel(description = "Information about a ban list (listRequested) obtained by comparing to a previous ban list (comparedTo). The information retrieved will be the removedCards.")
@JsonPropertyOrder({ "listRequested", "comparedTo", "numRemoved", "removedCards" })
public class BanListRemovedContent
{
	private String listRequested;
	private String comparedTo;
	private int numRemoved;
	private List<BanListComparisonResults> removedCards;
}