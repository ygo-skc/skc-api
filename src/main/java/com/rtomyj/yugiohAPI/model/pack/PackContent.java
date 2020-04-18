package com.rtomyj.yugiohAPI.model.pack;


import java.util.List;

import com.rtomyj.yugiohAPI.model.Card;

import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString
@Builder
public class PackContent extends RepresentationModel<PackContent>
{
	private Card card;
	private Integer position;
	private String rarity;



	public void setLinks()
	{
		this.card.setLinks();
	}


	public static void setLinks(@NonNull final List<PackContent> packContents)
	{
		packContents
			.stream()
			.forEach(packContent -> packContent.setLinks());
	}
}