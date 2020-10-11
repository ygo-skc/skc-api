package com.rtomyj.yugiohAPI.model.banlist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.rtomyj.yugiohAPI.controller.banlist.BanListNewContentController;
import com.rtomyj.yugiohAPI.controller.banlist.BanListRemovedContentController;
import com.rtomyj.yugiohAPI.controller.banlist.BannedCardsController;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;

import com.rtomyj.yugiohAPI.model.HateoasLinks;
import com.rtomyj.yugiohAPI.model.card.Card;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@Builder
@With
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("banListInstance")
@JsonPropertyOrder({ "startDate", "numForbidden", "numLimited", "numSemiLimited", "forbidden", "limited",
		"semiLimited" })
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@ApiModel(
		description = "Describes and contains information about a specific ban list."
		, parent = RepresentationModel.class
)
public class BanListInstance extends RepresentationModel<BanListInstance> implements HateoasLinks
{

	@ApiModelProperty(
			value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
			, position = 0
	)
	private String effectiveDate;

	@ApiModelProperty(
			value = "Total number of cards forbidden in this ban list instance; ie size of forbidden list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
			, position = 1
	)
	private int numForbidden;

	@ApiModelProperty(
			value = "Total number of cards limited in this ban list instance; ie size of limited list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
			, position = 2
	)
	private int numLimited;

	@ApiModelProperty(
			value = "Total number of cards semi-limited in this ban list instance; ie size of semi-limited list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
			, position = 3
	)
	private int numSemiLimited;

	@ApiModelProperty(
			value = "List of cards forbidden in this ban list instance."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
			, position = 4
	)
	private List<Card> forbidden;

	@ApiModelProperty(
			value = "List of cards limited in this ban list instance."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
			, position = 5
	)
	private List<Card> limited;

	@ApiModelProperty(
			value = "List of cards semi-limited in this ban list instance."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
			, position = 6
	)
	private List<Card> semiLimited;

	@ApiModelProperty(
			value = "Object containing info of cards that are newly added to this ban list compared to previous logical ban list. Note: this field will be null unless specified otherwise."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
			, position = 7
	)
	private BanListNewContent newContent;

	@ApiModelProperty(
			value = "Object containing info of cards that are removed from this ban list compared to previous logical ban list. Note: this field will be null unless specified otherwise."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
			, position = 8
	)
	private BanListRemovedContent removedContent;

	private static final Class<BannedCardsController> banListController = BannedCardsController.class;
	private static final Class<BanListRemovedContentController> removedController = BanListRemovedContentController.class;
	private static final Class<BanListNewContentController> newController = BanListNewContentController.class;


	@Override
	public void setSelfLink()
	{

		this.add(
				linkTo(methodOn(banListController).getBannedCards(effectiveDate, false, true))
						.withSelfRel()
		);

	}


	@Override
	public void setLinks()
		throws YgoException
	{
		this.setSelfLink();

		this.add(
				linkTo(methodOn(newController).getNewlyAddedContentForBanList(effectiveDate)).withRel("Ban List New Content")
		);
		this.add(
				linkTo(methodOn(removedController).getNewlyRemovedContentForBanList(effectiveDate)).withRel("Ban List Removed Content")
		);

		HateoasLinks.setLinks(forbidden);
		HateoasLinks.setLinks(limited);
		HateoasLinks.setLinks(semiLimited);

	}

}