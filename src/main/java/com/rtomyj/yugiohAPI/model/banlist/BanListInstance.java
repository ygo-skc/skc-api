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
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;

import com.rtomyj.yugiohAPI.model.HateoasLinks;
import com.rtomyj.yugiohAPI.model.card.Card;
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
@ApiModel(description = "Describes and contains information about a specific ban list.")
@JsonTypeName("banListInstance")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonPropertyOrder({ "startDate", "numForbidden", "numLimited", "numSemiLimited", "forbidden", "limited",
		"semiLimited" })
public class BanListInstance extends RepresentationModel<BanListInstance> implements HateoasLinks
{

	private String startDate;
	private int numForbidden;
	private int numLimited;
	private int numSemiLimited;

	private List<Card> forbidden;
	private List<Card> limited;
	private List<Card> semiLimited;

	private BanListNewContent newContent;
	private BanListRemovedContent removedContent;

	private static final Class<BannedCardsController> banListController = BannedCardsController.class;
	private static final Class<BanListRemovedContentController> removedController = BanListRemovedContentController.class;
	private static final Class<BanListNewContentController> newController = BanListNewContentController.class;


	@Override
	public void setSelfLink()
	{

		this.add(
				linkTo(methodOn(banListController).getBannedCards(startDate, false, true))
						.withSelfRel()
		);

	}


	@Override
	public void setLinks()
		throws YgoException
	{
		this.setSelfLink();

		this.add(
				linkTo(methodOn(newController).getNewlyAddedContentForBanList(startDate)).withRel("Ban List New Content")
		);
		this.add(
				linkTo(methodOn(removedController).getNewlyRemovedContentForBanList(startDate)).withRel("Ban List Removed Content")
		);

		HateoasLinks.setLinks(forbidden);
		HateoasLinks.setLinks(limited);
		HateoasLinks.setLinks(semiLimited);

	}

}