package com.rtomyj.skc.model.stats

import com.rtomyj.skc.controller.stats.StatsController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@ApiModel(description = "Statistics for monster types stored in Database.", parent = RepresentationModel::class)
data class MonsterTypeStats(
	@ApiModelProperty(
		value = "The scope or filter used when retrieving monster type stats.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	val scope: String,
	@ApiModelProperty(
		value = "Monster types and the total number of cards currently in Database that have the type for given scope.",
		accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	val monsterTypes: MutableMap<String, Int>
) : RepresentationModel<MonsterTypeStats?>(), HateoasLinks {

	override fun setSelfLink() {
		this.add(
			WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(statsControllerClass).monsterTypesForgivenCardColor(
					scope
				)
			).withSelfRel()
		)
	}


	override fun setLinks() {
		setSelfLink()
	}


	companion object {
		private val statsControllerClass = StatsController::class.java
	}
}