package com.rtomyj.yugiohAPI.model.pack;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.With;


@Data
@Builder
@With
@ToString
@JsonInclude(Include.NON_EMPTY)
public class Pack
{
	private String packId;
	private String packName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date packReleaseDate;
	private Integer packTotal;
	private Map<String, Integer> packRarityCount;
	private List<PackContent> packContent;
}