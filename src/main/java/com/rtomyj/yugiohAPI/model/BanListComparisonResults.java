package com.rtomyj.yugiohAPI.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rtomyj.yugiohAPI.controller.CardController;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@ApiModel(description = "Contains information about a card. Gets the state of the card in the previous ban list compared to a chosen ban list.")
@JsonPropertyOrder({ "id", "name", "previousState" })
public class BanListComparisonResults extends RepresentationModel<BanListComparisonResults>
{
	private String name;
	private String id;
	private String previousState;

	private static final Class<CardController> controllerClass = CardController.class;



	private void setLink()
	{
		this.add(
			linkTo(methodOn(controllerClass).getCard(id)).withSelfRel()
		);
	}



	public void setLinks()
	{
		this.setLink();
	}



	public static void setLinks(@NonNull final List<BanListComparisonResults> comparisonResults)
	{
		comparisonResults
			.forEach(comparisonResult -> comparisonResult.setLinks());
	}
}