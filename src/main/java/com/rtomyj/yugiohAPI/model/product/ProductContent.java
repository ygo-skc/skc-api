package com.rtomyj.yugiohAPI.model.product;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rtomyj.yugiohAPI.model.card.Card;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)	// serializes non null fields - ie returns non null fields from REST request
public class ProductContent extends RepresentationModel<ProductContent>
{
	private Card card;
	private String position;
	private Set<String> rarities;



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