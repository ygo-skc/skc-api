package com.rtomyj.yugiohAPI.model.pack;

import com.rtomyj.yugiohAPI.model.Card;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class PackContent
{
	private Card card;
	private Integer position;
	private String rarity;
}