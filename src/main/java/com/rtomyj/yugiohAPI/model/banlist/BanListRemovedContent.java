package com.rtomyj.yugiohAPI.model.banlist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rtomyj.yugiohAPI.controller.banlist.BannedCardsController;
import com.rtomyj.yugiohAPI.controller.banlist.BanListNewContentController;
import com.rtomyj.yugiohAPI.controller.banlist.BanListRemovedContentController;

import com.rtomyj.yugiohAPI.model.HateoasLinks;
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
public class BanListRemovedContent extends RepresentationModel<BanListRemovedContent> implements HateoasLinks
{
	private String listRequested;
	private String comparedTo;

	private int numRemoved;
	private List<BanListComparisonResults> removedCards;

	private static final Class<BanListRemovedContentController> controllerClass = BanListRemovedContentController.class;
	private static final Class<BannedCardsController> banListController = BannedCardsController.class;
	private static final Class<BanListNewContentController> newController = BanListNewContentController.class;


	@Override
	public void setSelfLink()
	{

		this.add(
			linkTo(methodOn(controllerClass).getNewlyRemovedContentForBanList(listRequested)).withSelfRel()
		);

	}


	@Override
	public void setLinks()
	{
		this.setSelfLink();

		this.add(
				linkTo(methodOn(banListController).getBannedCards(listRequested, false, true)).withRel("Ban List Content")
		);
		this.add(
				linkTo(methodOn(newController).getNewlyAddedContentForBanList(listRequested)).withRel("Ban List New Content")
		);
		this.add(
				linkTo(methodOn(controllerClass).getNewlyRemovedContentForBanList(comparedTo)).withRel("Previous Ban Lists' Removed Content")
		);
		HateoasLinks.setLinks(removedCards);
	}
}