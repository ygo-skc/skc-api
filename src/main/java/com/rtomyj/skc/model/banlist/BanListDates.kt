package com.rtomyj.skc.model.banlist

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import org.springframework.hateoas.RepresentationModel
import com.rtomyj.skc.model.banlist.BanListDates
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.annotations.ApiParam
import com.rtomyj.skc.model.banlist.BanListDate
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import com.rtomyj.skc.controller.banlist.BanListDatesController
import lombok.*

@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "Start dates of ban lists.", parent = RepresentationModel::class, discriminator = "links")
class BanListDates : RepresentationModel<BanListDates>(), HateoasLinks {
	@ApiParam(value = "Array of objects containing valid start dates of all ban lists currently in DB.")
	@JsonProperty(value = "banListDates", index = 0)
	var dates: List<BanListDate>? = null

	override fun setSelfLink() {
		this.add(
			WebMvcLinkBuilder
				.linkTo(
					WebMvcLinkBuilder
						.methodOn(controllerClass)
						.banListStartDates
				)
				.withSelfRel()
		)
	}

	override fun setLinks() {
		setSelfLink()
		HateoasLinks.setLinks(dates)
	}

	companion object {
		private val controllerClass = BanListDatesController::class.java
	}
}