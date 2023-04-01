package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.rtomyj.skc.find.banlist.controller.BanListDatesController
import com.rtomyj.skc.util.HateoasLinks
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@Schema(
	implementation = BanListDates::class,
	description = "Start dates of ban lists.",
)
data class BanListDates(
	@Schema(
		implementation = BanListDates::class,
		description = "Array of objects containing valid start dates of all ban lists currently in DB."
	)
	@JsonProperty(value = "banListDates", index = 1)
	val dates: List<BanListDate>
) : RepresentationModel<BanListDates>(), HateoasLinks {
	@JsonIgnore
	lateinit var format: String

	override fun setSelfLink() {
		this.add(
			WebMvcLinkBuilder
				.linkTo(
					WebMvcLinkBuilder
						.methodOn(controllerClass)
						.banListStartDates(format)
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