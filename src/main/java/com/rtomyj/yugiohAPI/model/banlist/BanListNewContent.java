package com.rtomyj.yugiohAPI.model.banlist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
@JsonPropertyOrder({ "listRequested", "comparedTo", "newCards" })
@ApiModel(
		description = "Cards added to requested ban list that were not in the previous ban list or cards that have a different ban list status (forbidden, limited, semi-limited) compared to the previous ban list."
		, parent = RepresentationModel.class
)
public class BanListNewContent extends RepresentationModel<BanListNewContent> implements HateoasLinks
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
			value = SwaggerConstants.NEWLY_ADDED_CARDS_TO_BAN_LIST_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private NewCards newCards;

	private static final Class<BanListNewContentController> controllerClass = BanListNewContentController.class;
	private static final Class<BannedCardsController> banListController = BannedCardsController.class;
	private static final Class<BanListRemovedContentController> removedController = BanListRemovedContentController.class;


	@Override
	public void setSelfLink()
	{

		this.add(
			linkTo(methodOn(controllerClass).getNewlyAddedContentForBanList(listRequested)).withSelfRel()
		);

	}


	public void setLinks()
	{

		this.setSelfLink();

		this.add(
				linkTo(methodOn(banListController).getBannedCards(listRequested, false, true)).withRel("Ban List Content")
		);
		this.add(
				linkTo(methodOn(removedController).getNewlyRemovedContentForBanList(listRequested)).withRel("Ban List Removed Content")
		);
		this.add(
				linkTo(methodOn(controllerClass).getNewlyAddedContentForBanList(comparedTo)).withRel("Previous Ban Lists' New Content")
		);

		newCards.setLinks();

	}

}