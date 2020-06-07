package com.rtomyj.yugiohAPI.model.product;

import java.util.List;

import com.rtomyj.yugiohAPI.model.Card;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString
@Builder
public class ProductContent extends RepresentationModel<ProductContent>
{
	private Card card;
	private Integer position;
	private String rarity;



	public void setLinks()
	{
		this.card.setLinks();
	}


	public static void setLinks(@NonNull final List<ProductContent> packContents)
	{
		packContents
			.stream()
			.forEach(packContent -> packContent.setLinks());
	}
}