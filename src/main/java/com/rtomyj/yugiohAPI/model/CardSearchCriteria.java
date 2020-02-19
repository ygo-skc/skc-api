package com.rtomyj.yugiohAPI.model;

import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardSearchCriteria
{
	@Pattern(regexp = "^[0-9]{0,8}$")
	private String cardId;
	private String cardName;
	private String cardColor;
	private String cardAttribute;
}