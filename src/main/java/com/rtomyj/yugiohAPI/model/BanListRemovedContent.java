package com.rtomyj.yugiohAPI.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rtomyj.yugiohAPI.controller.banlist.CardsController;
import com.rtomyj.yugiohAPI.controller.banlist.NewController;
import com.rtomyj.yugiohAPI.controller.banlist.RemovedController;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Information about a ban list (listRequested) obtained by comparing to a previous ban list (comparedTo). The information retrieved will be the removedCards.")
@JsonPropertyOrder({ "listRequested", "comparedTo", "numRemoved", "removedCards" })
public class BanListRemovedContent extends RepresentationModel<BanListRemovedContent>
{
	private String listRequested;
	private String comparedTo;

	private int numRemoved;
	private List<BanListComparisonResults> removedCards;

	private static final Class<RemovedController> controllerClass = RemovedController.class;
	private static final Class<CardsController> banListController = CardsController.class;
	private static final Class<NewController> newController = NewController.class;



	private void setLink()
	{
		this.add(
			linkTo(methodOn(controllerClass).getRemovedContent(listRequested)).withSelfRel()
		);

		this.add(
			linkTo(methodOn(banListController).getBannedCards(listRequested, false)).withRel("banlist")
		);

		this.add(
			linkTo(methodOn(newController).getNewContent(listRequested)).withRel("new-cards")
		);

		this.add(
			linkTo(methodOn(controllerClass).getRemovedContent(comparedTo)).withRel("previous")
		);
	}



	public void setLinks()
	{
		this.setLink();
		BanListComparisonResults.setLinks(removedCards);
	}
}