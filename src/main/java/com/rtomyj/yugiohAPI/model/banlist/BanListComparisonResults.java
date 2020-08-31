package com.rtomyj.yugiohAPI.model.banlist;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rtomyj.yugiohAPI.controller.card.CardController;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@ApiModel(description = "Contains information about a card. Gets the state of the card in the previous ban list compared to a chosen ban list.")
@JsonPropertyOrder({ "id", "name", "previousState" })
public class BanListComparisonResults extends RepresentationModel<BanListComparisonResults> implements HateoasLinks
{
	private String name;
	private String id;
	private String previousState;

	private static final Class<CardController> controllerClass = CardController.class;


	@Override
	public void setSelfLink()
	{
		this.add(
			linkTo(methodOn(controllerClass).getCard(id, false)).withSelfRel()
		);
	}


	@Override
	public void setLinks()
	{

		this.setSelfLink();

	}

}