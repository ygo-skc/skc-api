package com.rtomyj.skc.find.banlist.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * Entity describing the ban_lists table in mysql DB.
 */
@Entity
@Table(name = "ban_lists")
@JsonInclude(
    JsonInclude.Include.NON_EMPTY
) // serializes non null fields - ie returns non null fields from REST request
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

    companion object {
        private const val serialVersionUID = 3890245600312215281L
    }
}