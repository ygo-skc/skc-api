package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.rtomyj.skc.util.enumeration.LinkArrow.Companion.transformDBStringToEnum
import io.swagger.v3.oas.annotations.media.Schema
import org.slf4j.LoggerFactory

@JsonInclude(JsonInclude.Include.NON_EMPTY) // serializes non-null fields - ie returns non-null fields from REST request
@Schema(
  implementation = MonsterAssociation::class,
  description = "Information about various properties only monster cards have that usually associate distinct monster cards (not in the same archetype) together."
)
class MonsterAssociation(
  @Schema(
    implementation = Int::class,
    description = "The star rating of a monster card - determines tribute count.",
  )
  val level: Int? = null,

  @Schema(
    implementation = Int::class,
    description = "Like level, except it determines which monsters can be used to summon the card.",
  )
  val rank: Int? = null,

  @Schema(
    implementation = Int::class,
    description = "Pendulum rating used to perform pendulum summoning.",
  )
  val scaleRating: Int? = null,

  @Schema(
    implementation = Int::class,
    description = "Number value determining link cost in summoning Link monster.",
  )
  val linkRating: Int? = null,

  @Schema(
    implementation = List::class,
    description = "Positions of Link arrows for link monster.",
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
    @JvmStatic
    fun transformMonsterLinkRating(cards: List<Card>) {
      cards
          .stream()
          .map { it.monsterAssociation }
          .filter { it != null }
          .forEach { it!!.transformMonsterLinkRating() }
    }


    /**
     * Calls [.transformMonsterLinkRating] on a card
     * @param card card whose link rating should be transformed
     */
    fun transformMonsterLinkRating(card: Card) = card.monsterAssociation?.transformMonsterLinkRating()
  }


  /**
   * Takes monster link rating retrieved from DB (constants denoting position of arrow, eg: T-L (top left), T-R (top right)... etc.) and converts them to emojis.
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
