package com.rtomyj.yugiohAPI.model;

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
	private String cardId;
	private String cardName;
	private String cardColor;
	private String cardAttribute;
}