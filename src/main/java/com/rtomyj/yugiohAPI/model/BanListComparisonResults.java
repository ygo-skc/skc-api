package com.rtomyj.yugiohAPI.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "id", "name", "previousState" })
public class BanListComparisonResults
{
	private String name;
	private String id;
	private String previousState;
}