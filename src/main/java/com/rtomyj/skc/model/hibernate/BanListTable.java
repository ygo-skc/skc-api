package com.rtomyj.skc.model.hibernate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity describing the ban_lists table in mysql DB.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "ban_lists")
@JsonInclude(Include.NON_EMPTY) // serializes non null fields - ie returns non null fields from REST request
public class BanListTable implements Serializable
{

	private static final long serialVersionUID = 3890245600312215281L;

	/**
	 * Start date of ban list.
	 */
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "ban_list_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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