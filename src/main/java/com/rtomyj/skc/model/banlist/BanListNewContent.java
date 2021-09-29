package com.rtomyj.skc.model.banlist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rtomyj.skc.controller.banlist.BanListDiffController;
import com.rtomyj.skc.controller.banlist.BannedCardsController;

import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.model.HateoasLinks;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "listRequested", "comparedTo", "newCards" })
@ApiModel(
		description = "Cards added to requested ban list that were not in the previous ban list and/or cards that have a different ban list status (forbidden, limited, semi-limited) compared to the previous ban list."
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
			value = "Total new forbidden cards added to a ban list when compared to a previous logical ban list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private int numNewForbidden;

	@ApiModelProperty(
			value = "Total new limited cards added to a list when compared to a previous logical ban list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private int numNewLimited;

	@ApiModelProperty(
			value = "Total new semi-limited cards added to a ban list when compared to a previous logical ban list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private int numNewSemiLimited;

	@ApiModelProperty(
			value = "List containing newly forbidden cards and their previous ban status."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private List<CardsPreviousBanListStatus> newForbidden;

	@ApiModelProperty(
			value = "List containing newly limited cards and their previous ban status."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private List<CardsPreviousBanListStatus> newLimited;

	@ApiModelProperty(
			value = "List containing newly semi-limited cards and their previous ban status."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private List<CardsPreviousBanListStatus> newSemiLimited;

	private static final Class<BanListDiffController> BAN_LIST_DIFF_CONTROLLER_CLASS = BanListDiffController.class;
	private static final Class<BannedCardsController> banListController = BannedCardsController.class;


	@Override
	public void setSelfLink()
	{

		this.add(
			linkTo(methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS).getNewlyAddedContentForBanList(listRequested)).withSelfRel()
		);

	}


	public void setLinks()
	{

		this.setSelfLink();

		this.add(
				linkTo(methodOn(banListController).getBannedCards(listRequested, false, true)).withRel("Ban List Content")
		);
		this.add(
				linkTo(methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS).getNewlyRemovedContentForBanList(listRequested)).withRel("Ban List Removed Content")
		);

		HateoasLinks.setLinks(newForbidden);
		HateoasLinks.setLinks(newLimited);
		HateoasLinks.setLinks(newSemiLimited);

	}

}