package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.io.Serializable
import java.time.LocalDate

/**
 * Entity describing the ban_lists table in mysql DB.
 */
@Entity
@Table(name = "ban_lists")
@JsonInclude(
  JsonInclude.Include.NON_EMPTY
) // serializes non-null fields - ie returns non-null fields from REST request
class BanListTable : Serializable {
  /**
   * Start date of ban list.
   */
  @Id
  @Temporal(TemporalType.DATE)
  @Column(name = "ban_list_date")
  var banListDate: LocalDate? = null

  /**
   * The ID of the card.
   */
  @Id
  @Column(name = "card_number", length = 8)
  var cardNumber: String? = null

  /**
   * The ID of the card.
   */
  @Id
  @Column(name = "duel_format", length = 8)
  var format: String? = null

  /**
   * Whether card is forbidden, limited, or semi-limited
   */
  @Column(name = "ban_status", length = 15)
  var banStatus: String? = null

  override fun equals(other: Any?): Boolean {
    if (other?.javaClass != this.javaClass)
      return false

    val o = other as BanListTable
    return this.banListDate === o.banListDate && this.cardNumber === o.cardNumber && this.format === o.format
  }

  override fun hashCode(): Int {
    return this.banListDate.hashCode() + this.cardNumber.hashCode() + this.format.hashCode()
  }
}