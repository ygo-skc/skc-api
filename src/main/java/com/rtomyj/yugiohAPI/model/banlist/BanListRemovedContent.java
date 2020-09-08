package com.rtomyj.yugiohAPI.model.banlist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rtomyj.yugiohAPI.controller.banlist.BannedCardsController;
import com.rtomyj.yugiohAPI.controller.banlist.BanListNewContentController;
import com.rtomyj.yugiohAPI.controller.banlist.BanListRemovedContentController;

import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "listRequested", "comparedTo", "numRemoved", "removedCards" })
@ApiModel(
		description = "Cards that were removed from a ban list compared to the previous logical ban list."
		, parent = RepresentationModel.class
)
public class BanListRemovedContent extends RepresentationModel<BanListRemovedContent> implements HateoasLinks
{

	@ApiModelProperty(
			value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String listRequested;

	@ApiModelProperty(
			value = SwaggerConstants.PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String comparedTo;

	@ApiModelProperty(
			value = "Total number of cards removed in requested ban list compared to previous ban list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private int numRemoved;

	@ApiModelProperty(
			value = "List containing removed cards and their previous ban status."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private List<CardsPreviousBanListStatus> removedCards;

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