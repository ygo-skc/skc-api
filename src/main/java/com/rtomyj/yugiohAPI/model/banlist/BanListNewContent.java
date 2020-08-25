package com.rtomyj.yugiohAPI.model.banlist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rtomyj.yugiohAPI.controller.banlist.BannedCardsController;
import com.rtomyj.yugiohAPI.controller.banlist.BanListNewContentController;
import com.rtomyj.yugiohAPI.controller.banlist.BanListRemovedContentController;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Information about a ban list (listRequested) obtained by comparing to a previous ban list (comparedTo). The information retrieved will be the newCards.")
@JsonPropertyOrder({ "listRequested", "comparedTo", "newCards" })
public class BanListNewContent extends RepresentationModel<BanListNewContent>
{
	private String listRequested;
	private String comparedTo;
	private NewCards newCards;

	private static final Class<BanListNewContentController> controllerClass = BanListNewContentController.class;
	private static final Class<BannedCardsController> banListController = BannedCardsController.class;
	private static final Class<BanListRemovedContentController> removedController = BanListRemovedContentController.class;



	private void setLink()
	{
		this.add(
			linkTo(methodOn(controllerClass).getNewlyAddedContentForBanList(listRequested)).withSelfRel()
		);

		this.add(
			linkTo(methodOn(banListController).getBannedCards(listRequested, false, true)).withRel("banlist")
		);

		this.add(
			linkTo(methodOn(removedController).getNewlyRemovedContentForBanList(listRequested)).withRel("removed-cards")
		);

		this.add(
			linkTo(methodOn(controllerClass).getNewlyAddedContentForBanList(comparedTo)).withRel("previous")
		);
	}



	public void setLinks()
	{
		this.setLink();
	}
}