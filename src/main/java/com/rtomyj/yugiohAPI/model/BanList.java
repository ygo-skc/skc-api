package com.rtomyj.yugiohAPI.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Builder;
import lombok.Data;


/**
 * Entity describing the ban_lists table in mysql DB.
 */
@Data
@Builder
@Entity
@Table(name = "ban_lists", schema = "yugioh_API_DB")
public class BanList implements Serializable {

	private static final long serialVersionUID = 3890245600312215281L;

	/**
	 * Start date of ban list.
	 */
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "ban_list_date")
	private Date banListDate;

	/**
	 * The ID of the card.
	 */
	@Id
	@Column(name = "card_number", length=8)
	private String cardNumber;

	/**
	 * Whether card is forbidden, limited, or semi-limited
	 */
	@Column(name = "ban_status", length=15)
	private String banStatus;
}