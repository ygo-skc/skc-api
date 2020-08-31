package com.rtomyj.yugiohAPI.model.banlist;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.yugiohAPI.config.DateConfig;
import com.rtomyj.yugiohAPI.controller.banlist.BanListNewContentController;
import com.rtomyj.yugiohAPI.controller.banlist.BanListRemovedContentController;
import com.rtomyj.yugiohAPI.controller.banlist.BannedCardsController;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Model containing information about a Ban List.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY) // serializes non null fields - ie returns non null fields from REST request
public class BanList extends RepresentationModel<BanList> implements HateoasLinks
{

	/**
	 * Start date of ban list.
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date banListDate;

	/**
	 * The ID of the card.
	 */
	private String cardNumber;

	/**
	 * Whether card is forbidden, limited, or semi-limited
	 */
	private String banStatus;

	private static final Class<BannedCardsController> BANNED_CARDS_CONTROLLER_CLASS = BannedCardsController.class;
	private static final Class<BanListNewContentController> BAN_LIST_NEW_CONTENT_CONTROLLER_CLASS = BanListNewContentController.class;
	private static final Class<BanListRemovedContentController> BAN_LIST_REMOVED_CONTENT_CONTROLLER_CLASS = BanListRemovedContentController.class;

	private static final SimpleDateFormat banListSimpleDateFormat = DateConfig.getDBSimpleDateFormat();


	@Override
	public void setSelfLink()
	{

	}


	@Override
	public void setLinks()
	{

		final String banListDateStr = banListSimpleDateFormat.format(banListDate);

		this.add(
				linkTo(methodOn(BANNED_CARDS_CONTROLLER_CLASS)
						.getBannedCards(banListDateStr, false, true))
						.withRel("Ban List Content"));
		this.add(
				linkTo(methodOn(BAN_LIST_NEW_CONTENT_CONTROLLER_CLASS)
						.getNewlyAddedContentForBanList(banListDateStr))
						.withRel("Ban List New Content"));
		this.add(
				linkTo(methodOn(BAN_LIST_REMOVED_CONTENT_CONTROLLER_CLASS)
						.getNewlyRemovedContentForBanList(banListDateStr))
						.withRel("Ban List Removed Content"));

	}


	public BanList(final Date banListDate)
	{

		this.banListDate = banListDate;

	}

}