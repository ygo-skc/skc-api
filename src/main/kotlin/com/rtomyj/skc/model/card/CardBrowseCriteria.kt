package com.rtomyj.skc.model.card

import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.Open
import com.rtomyj.skc.controller.card.CardBrowseController
import com.rtomyj.skc.model.HateoasLinks
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(
    description = "Valid browse criteria and valid values per criteria. May also be used to identify which browse criteria is being used for browse results.",
    parent = RepresentationModel::class
)
@Open
data class CardBrowseCriteria(
    @ApiModelProperty(
        value = "Unique set of identifiers for the type of card.",
        example = "normal, effect, fusion",
        dataType = "List"
    )
    val cardColors: Set<String>,
    @ApiModelProperty(
        value = "Unique set of identifiers of card attributes.",
        example = "Dark, Light, Earth, Wind, Water, Fire",
        dataType = "List"
    )
    val attributes: Set<String>,
    @ApiModelProperty(
        value = "Unique set of identifiers for monster types.",
        example = "Spellcaster, Wyrm, Warrior, etc",
        dataType = "List"
    )
    val monsterTypes: Set<String>,
    @ApiModelProperty(
        value = "Unique set of identifiers for monster sub types.",
        example = "Flip, Toon, Gemini, etc",
        dataType = "List"
    )
    val monsterSubTypes: Set<String>,
    @ApiModelProperty(
        value = "Unique set of levels for monster cards in database.",
        example = "1, 2, 3, 4, 5, 6",
        dataType = "List"
    )
    val levels: Set<Int>,
    @ApiModelProperty(
        value = "Unique set of ranks for monster cards in database.",
        example = "1, 2, 3, 4, 5, 6",
        dataType = "List"
    )
    val ranks: Set<Int>,
    @ApiModelProperty(
        value = "Unique set of link rating values for Link monster cards in database.",
        example = "1, 2, 3, 4, 5, 6",
        dataType = "List"
    )
    val linkRatings: Set<Int>
) : RepresentationModel<CardBrowseCriteria>(), HateoasLinks {

    companion object {
        private val cardBrowseControllerClass = CardBrowseController::class.java
    }


    override fun setSelfLink() {
        this.add(
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(cardBrowseControllerClass).browseCriteria())
                .withSelfRel()
        )
    }

    override fun setLinks() {
        setSelfLink()
    }
}