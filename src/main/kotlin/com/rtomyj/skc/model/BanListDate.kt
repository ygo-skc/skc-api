package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

/**
 * Model containing information about a Ban List.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // serializes non-null fields - ie returns non-null fields from REST request
@Schema(
  implementation = BanListDate::class,
  description = "Information about a ban lists effective date.",
)
data class BanListDate(
  /**
   * Start date of ban list.
   */
  @Schema(
    implementation = LocalDate::class,
    description = SwaggerConstants.BAN_LIST_START_DATE_DESCRIPTION,
  ) @JsonFormat(
    shape = JsonFormat.Shape.STRING,
    pattern = "yyyy-MM-dd"
  ) val effectiveDate: LocalDate
) {
  @Schema(
    implementation = String::class,
    description = "What format the ban list dates correspond to."
  )
  @JsonProperty(value = "format", index = 0)
  var format: String? = null
}