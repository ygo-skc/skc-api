package com.rtomyj.yugiohAPI.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "listRequested", "comparedTo", "removedCards" })
public class BanListRemovedContent
{
	private String listRequested;
	private String comparedTo;
	private List<BanListComparisonResults> removedCards;
}