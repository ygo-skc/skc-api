package com.rtomyj.yugiohAPI.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class PackDetails
{
	private Pack pack;
	private List<PackContent> contents;
}