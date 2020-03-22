package com.rtomyj.yugiohAPI.model;

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
}