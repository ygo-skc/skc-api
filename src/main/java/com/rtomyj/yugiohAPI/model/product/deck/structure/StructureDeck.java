package com.rtomyj.yugiohAPI.model.product.deck.structure;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rtomyj.yugiohAPI.model.product.pack.PackContent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StructureDeck {

	private String packId;
	private String locale;
	private String packName;
	private String productType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date packReleaseDate;
	private Integer packTotal;
	private Map<String, Integer> packRarityCount;
	private List<PackContent> packContent;

}