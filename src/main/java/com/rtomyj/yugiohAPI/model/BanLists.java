package com.rtomyj.yugiohAPI.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Entity used to make DB calls with Hibernate on the ban_lists table.
 */
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



	/**
	 * Get card number.
	 * @return Card number.
	 */
	public String getCardNumber() {
		return cardNumber;
	}



	/**
	 * Get ban status
	 * @return Ban status.
	 */
	public String getBanStatus() {
		return banStatus;
	}



	/**
	 * Set ban status.
	 * @param banStatus Ban Status.
	 */
	public void setBanStatus(String banStatus) {
		this.banStatus = banStatus;
	}



	/**
	 * Get ban list date.
	 * @return Date of the ban list.
	 */
	public Date getBanListDate() {
		return banListDate;
	}



	/**
	 * Set ban list date
	 * @param banListDate Date of the ban list.
	 */
	public void setBanListDate(Date banListDate) {
		this.banListDate = banListDate;
	}



	/**
	 * Set card number
	 * @param cardNumber Card number.
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

}