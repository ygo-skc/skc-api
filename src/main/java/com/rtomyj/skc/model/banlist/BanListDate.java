package com.rtomyj.skc.model.banlist;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.skc.config.DateConfig;
import com.rtomyj.skc.controller.banlist.BanListNewContentController;
import com.rtomyj.skc.controller.banlist.BanListRemovedContentController;
import com.rtomyj.skc.controller.banlist.BannedCardsController;
import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@JsonInclude(Include.NON_EMPTY) // serializes non null fields - ie returns non null fields from REST request
@ApiModel(
		description = "Information about a ban lists effective date."
		, parent = RepresentationModel.class
)
public class BanListDate extends RepresentationModel<BanListDate> implements HateoasLinks
{

	/**
	 * Start date of ban list.
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@ApiModelProperty(
			value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private Date effectiveDate;

	private static final Class<BannedCardsController> BANNED_CARDS_CONTROLLER_CLASS = BannedCardsController.class;
	private static final Class<BanListNewContentController> BAN_LIST_NEW_CONTENT_CONTROLLER_CLASS = BanListNewContentController.class;
	private static final Class<BanListRemovedContentController> BAN_LIST_REMOVED_CONTENT_CONTROLLER_CLASS = BanListRemovedContentController.class;

	private final SimpleDateFormat banListSimpleDateFormat = DateConfig.getDBSimpleDateFormat();


	@Override
	public void setSelfLink()
	{

	}


	@Override
	public void setLinks()
	{

		final String banListDateStr = banListSimpleDateFormat.format(effectiveDate);

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


	public BanListDate(final Date effectiveDate)
	{

		this.effectiveDate = effectiveDate;

	}

}