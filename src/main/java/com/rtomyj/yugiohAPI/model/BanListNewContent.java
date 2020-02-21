package com.rtomyj.yugiohAPI.model;

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
@ApiModel(description = "Information about a ban list (listRequested) obtained by comparing to a previous ban list (comparedTo). The information retrieved will be the newCards.")
@JsonPropertyOrder({ "listRequested", "comparedTo", "newCards" })
public class BanListNewContent
{
	private String listRequested;
	private String comparedTo;
	private NewCards newCards;
}