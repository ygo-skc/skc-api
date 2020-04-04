package com.rtomyj.yugiohAPI.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.rtomyj.yugiohAPI.controller.banlist.CardsController;
import com.rtomyj.yugiohAPI.helper.exceptions.YgoException;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
@ApiModel(description = "Describes and contains information about a specific ban list.")
@JsonTypeName("bannedCards")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonPropertyOrder({ "startDate", "numForbidden", "numLimited", "numSemiLimited", "forbidden", "limited",
		"semiLimited" })
public class BanListInstance extends RepresentationModel<BanListInstance> {
	private String startDate;
	private int numForbidden;
	private int numLimited;
	private int numSemiLimited;
	private List<Card> forbidden;
	private List<Card> limited;
	private List<Card> semiLimited;



	public static void addLinksToBanListInstance(final BanListInstance banListInstance, final boolean lowBandwidth)
		throws YgoException
	{
		banListInstance.add(
			linkTo(methodOn(CardsController.class).getBannedCards(banListInstance.getStartDate(), lowBandwidth))
				.withSelfRel()
		);
	}
}