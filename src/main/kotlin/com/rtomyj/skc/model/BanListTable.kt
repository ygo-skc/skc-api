package com.rtomyj.skc.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import java.io.Serializable
import java.util.*

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var banListDate: Date? = null

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