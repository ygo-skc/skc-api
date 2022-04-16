package com.rtomyj.skc.model.banlist

import com.fasterxml.jackson.annotation.JsonProperty
import com.rtomyj.skc.Open
import com.rtomyj.skc.controller.banlist.BanListDatesController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@Schema(
	implementation = BanListDates::class,
	description = "Start dates of ban lists.",
)
@Open
data class BanListDates(
	@Schema(
		implementation = BanListDates::class,
		description = "Array of objects containing valid start dates of all ban lists currently in DB."
	)
	@JsonProperty(value = "banListDates", index = 0)
	val dates: List<BanListDate>
) : RepresentationModel<BanListDates>(), HateoasLinks {

	override fun setSelfLink() {
		this.add(
			WebMvcLinkBuilder
				.linkTo(
					WebMvcLinkBuilder
						.methodOn(controllerClass)
						.banListStartDates()
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