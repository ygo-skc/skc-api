package com.rtomyj.yugiohAPI.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


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
	 * @return item
	 */
	public String getCardNumber() {
		return cardNumber;
	}



	/**
	 * @return item
	 */
	public String getBanStatus() {
		return banStatus;
	}



	/**
	 * @param banStatus
	 */
	public void setBanStatus(String banStatus) {
		this.banStatus = banStatus;
	}



	/**
	 * @return item
	 */
	public Date getBanListDate() {
		return banListDate;
	}



	/**
	 * @param banListDate
	 */
	public void setBanListDate(Date banListDate) {
		this.banListDate = banListDate;
	}



	/**
	 * @param cardNumber
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

}