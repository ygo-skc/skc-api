package com.rtomyj.yugiohAPI.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;


/**
 * Entity used to make DB calls with Hibernate on the ban_lists table.
 */
@Data
@Entity
@Table(name = "ban_lists", schema = "yugioh_API_DB")
public class BanLists implements Serializable {

	private static final long serialVersionUID = 3890245600312215281L;

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "ban_list_date")
	private Date banListDate;

	@Id
	@Column(name = "card_number", length=8)
	private String cardNumber;

	@Column(name = "ban_status", length=15)
	private String banStatus;
}