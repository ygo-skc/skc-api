package com.rtomyj.skc.model.stats

import com.rtomyj.skc.controller.stats.StatsController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@ApiModel(description = "High level stats for data in the database.", parent = RepresentationModel::class)
data class DatabaseStats(
	@ApiModelProperty(
		value = "Total number of products in the database.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY
	) val productTotal: Int,
	@ApiModelProperty(
		value = "Total number of cards in the database.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY
	) val cardTotal: Int,
	@ApiModelProperty(
		value = "Total number of ban lists in the database.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY
	) val banListTotal: Int,
	@ApiModelProperty(
		value = "Total number of years spanned/covered by ban lists stored in database.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY
	) val yearsOfBanListCoverage: Int
) : RepresentationModel<DatabaseStats>(), HateoasLinks {

	companion object {
		private val statsControllerClass = StatsController::class.java
	}


	override fun setSelfLink() {
		this.add(
			WebMvcLinkBuilder
				.linkTo(
					WebMvcLinkBuilder.methodOn(statsControllerClass)
						.databaseStats()
				)
				.withSelfRel()
		)
	}


	override fun setLinks() {
		setSelfLink()
	}
}