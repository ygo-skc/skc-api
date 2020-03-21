package com.rtomyj.yugiohAPI.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "Contains information about a card. Gets the state of the card in the previous ban list compared to a chosen ban list.")
@JsonPropertyOrder({ "id", "name", "previousState" })
public class BanListComparisonResults
{
	private String name;
	private String id;
	private String previousState;
}