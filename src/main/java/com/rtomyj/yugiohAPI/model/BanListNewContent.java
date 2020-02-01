package com.rtomyj.yugiohAPI.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({ "listRequested", "comparedTo", "newCards" })
public class BanListNewContent
{
	private String listRequested;
	private String comparedTo;
	private NewCards newCards;
}