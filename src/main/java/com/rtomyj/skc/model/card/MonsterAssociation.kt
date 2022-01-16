package com.rtomyj.skc.model.card

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.enums.LinkArrow.Companion.transformDBStringToEnum
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.slf4j.LoggerFactory

@JsonInclude(JsonInclude.Include.NON_EMPTY) // serializes non null fields - ie returns non null fields from REST request
@ApiModel(description = "Information about various properties only monster cards have that usually associate distinct monster cards (not in the same archetype) together.")
class MonsterAssociation(
    @ApiModelProperty(
        value = "The star rating of a monster card - determines tribute count.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val level: Int? = null,

    @ApiModelProperty(
        value = "Like level, except it determines which monsters can be used to summon the card.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val rank: Int? = null,

    @ApiModelProperty(
        value = "Pendulum rating used to perform pendulum summoning.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val scaleRating: Int? = null,

    @ApiModelProperty(
        value = "Number value determining link cost in summoning Link monster.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    val linkRating: Int? = null,

    @ApiModelProperty(
        value = "Positions of Link arrows for link monster.",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    var linkArrows: List<String>? = null
) {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java.name)

        @JvmStatic
        fun parseDBString(dbMonsterAssociationJson: String?, objectMapper: ObjectMapper): MonsterAssociation? {
            try {
                if (dbMonsterAssociationJson != null) return objectMapper.readValue(
                    dbMonsterAssociationJson,
                    MonsterAssociation::class.java
                )
            } catch (e: JsonProcessingException) {
                log.error(
                    "Exception occurred when parsing monster association column, {}",
                    e.toString()
                )
                return null
            }
            return null
        }


        /**
         * Calls [.transformMonsterLinkRating] on a list of Cards
         * @param cards list of cards whose link rating should be transformed
         */
        fun transformMonsterLinkRating(cards: List<Card>) {
            cards
                .stream()
                .map { it.monsterAssociation }
                .filter { it != null }
                .forEach { obj: MonsterAssociation -> obj.transformMonsterLinkRating() }
        }
    }


    /**
     * Takes monster link rating retrieved from DB (constants denoting position of arrow, eg: T-L (top left), T-R (top right)... etc) and converts them to emojis.
     */
    fun transformMonsterLinkRating() {
        if (this.linkArrows?.isNotEmpty() == true) {
            this.linkArrows = this
                .linkArrows!!
                .stream()
                .map { dbArrowString: String ->
                    transformDBStringToEnum(dbArrowString)
                        .toString()
                }
                .toList()

        }
    }
}
