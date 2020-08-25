package com.rtomyj.yugiohAPI.model.banlist;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.yugiohAPI.controller.banlist.BannedCardsController;

import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Data;

/**
 * Entity describing the ban_lists table in mysql DB.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@Entity
@Table(name = "ban_lists")
@JsonInclude(Include.NON_EMPTY) // serializes non null fields - ie returns non null fields from REST request
public class BanList extends RepresentationModel<BanList> implements Serializable
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

	private static final Class<BannedCardsController> controllerClass = BannedCardsController.class;
	//private static final SimpleDateFormat banListSimpleDateFormat = DateConfig.getDBSimpleDateFormat();


	private void setLink()
	{
		// this.add(
		// 	linkTo(methodOn(controllerClass).getBannedCards(banListSimpleDateFormat.format(banListDate), false)).withSelfRel()
		// );
	}



	public void setLinks()
	{
		this.setLink();
	}



	public static void setLinks(final List<BanList> banlists)
	{
		System.out.println(banlists);
		System.out.println(banlists.get(0).getClass());
		// banlists
		// 	.forEach(banlist -> banlist.setLinks());
	}
}