package com.rtomyj.yugiohAPI.model.pack;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Packs {
	private List<Pack> packs;
}