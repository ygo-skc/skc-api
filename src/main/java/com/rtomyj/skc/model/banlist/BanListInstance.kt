package com.rtomyj.skc.model.banlist

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.rtomyj.skc.constant.SwaggerConstants
import com.rtomyj.skc.controller.banlist.BanListDiffController
import com.rtomyj.skc.controller.banlist.BannedCardsController
import com.rtomyj.skc.model.HateoasLinks
import com.rtomyj.skc.model.card.Card
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("banListInstance")
@JsonPropertyOrder("effectiveDate", "comparedTo", "numForbidden", "numLimited", "numSemiLimited", "forbidden", "limited", "semiLimited")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(
	description = "Describes and contains information about a specific ban list.",
	parent = RepresentationModel::class
)
class BanListInstance : RepresentationModel<BanListInstance>(), HateoasLinks {
	@ApiModelProperty(
		value = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
		accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	var effectiveDate: String? = null

	@ApiModelProperty(
		value = SwaggerConstants.PREVIOUS_BAN_LIST_START_DATE_DESCRIPTION,
		accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	var comparedTo: String? = null

	@ApiModelProperty(
		value = "Total number of cards forbidden in this ban list instance; ie size of forbidden list.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY,
		position = 1
	)
	var numForbidden: Int? = null

	@ApiModelProperty(
		value = "Total number of cards limited in this ban list instance; ie size of limited list.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY,
		position = 2
	)
	var numLimited: Int? = null

	@ApiModelProperty(
		value = "Total number of cards semi-limited in this ban list instance; ie size of semi-limited list.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY,
		position = 3
	)
	var numSemiLimited: Int? = null

	@ApiModelProperty(
		value = "List of cards forbidden in this ban list instance.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY,
		position = 4
	)
	var forbidden: List<Card>? = null

	@ApiModelProperty(
		value = "List of cards limited in this ban list instance.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY,
		position = 5
	)
	var limited: List<Card>? = null

	@ApiModelProperty(
		value = "List of cards semi-limited in this ban list instance.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY,
		position = 6
	)
	var semiLimited: List<Card>? = null

	@ApiModelProperty(
		value = "Object containing info of cards that are newly added to this ban list compared to previous logical ban list. Note: this field will be null unless specified otherwise.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY,
		position = 7
	)
	var newContent: BanListNewContent? = null

	@ApiModelProperty(
		value = "Object containing info of cards that are removed from this ban list compared to previous logical ban list. Note: this field will be null unless specified otherwise.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY,
		position = 8
	)
	var  removedContent: BanListRemovedContent? = null


	companion object {
		val banListController = BannedCardsController::class.java
		val BAN_LIST_DIFF_CONTROLLER_CLASS = BanListDiffController::class.java
	}


	override fun setSelfLink() {
		this.add(
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(banListController).getBannedCards(effectiveDate, false, true)
			)
				.withSelfRel()
		)
	}


	override fun setLinks() {
		setSelfLink()
		this.add(
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS).getNewlyAddedContentForBanList(effectiveDate)
			).withRel("Ban List New Content")
		)
		this.add(
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(BAN_LIST_DIFF_CONTROLLER_CLASS)
					.getNewlyRemovedContentForBanList(effectiveDate)
			).withRel("Ban List Removed Content")
		)
		HateoasLinks.setLinks(forbidden)
		HateoasLinks.setLinks(limited)
		HateoasLinks.setLinks(semiLimited)
	}
}