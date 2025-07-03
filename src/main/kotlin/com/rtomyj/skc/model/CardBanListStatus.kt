package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.rtomyj.skc.util.constant.SwaggerConstants
import com.rtomyj.skc.util.enumeration.BanListFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.*

/**
 * Model containing information about a Ban List.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // serializes non-null fields - ie returns non-null fields from REST request
@Schema(
  implementation = BanListInstance::class,
  description = "Each object instance describes a particular card, a start date of a ban list it was a part of, and the specific status (forbidden, limited, semi-limited).",
)
data class CardBanListStatus(
  /**
   * Start date of ban list.
   */
  @field:Schema(
    implementation = Date::class,
    description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
  )
  @field:JsonFormat(
    shape = JsonFormat.Shape.STRING,
    pattern = "yyyy-MM-dd"
  )
  val banListDate: LocalDate,

  /**
   * The ID of the card.
   */
  @field:Schema(
    implementation = String::class,
    description = SwaggerConstants.CARD_ID_DESCRIPTION,
  )
  val cardID: String,

  /**
   * Whether card is forbidden, limited, or semi-limited
   */
  @field:Schema(
    implementation = String::class,
    description = "The ban status for the card (forbidden, limited, semi-limited).",
  )
  val banStatus: String,
//    @JsonIgnore
  val format: BanListFormat
)