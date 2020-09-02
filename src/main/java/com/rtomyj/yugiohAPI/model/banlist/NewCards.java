package com.rtomyj.yugiohAPI.model.banlist;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.rtomyj.yugiohAPI.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "All new cards separated by status.")
@JsonPropertyOrder({ "numForbidden", "numLimited", "numSemiLimited", "forbidden", "limited", "semiLimited" })
public class NewCards extends RepresentationModel<NewCards> implements HateoasLinks
{

	private int numForbidden;
	private int numLimited;
	private int numSemiLimited;

	private List<CardPreviousBanListStatus> forbidden;
	private List<CardPreviousBanListStatus> limited;
	private List<CardPreviousBanListStatus> semiLimited;


	@Override
	public void setSelfLink()
	{

		throw new UnsupportedOperationException("Cannot set self link on this object: " + this.getClass().toString());

	}


	@Override
	public void setLinks()
	{

		HateoasLinks.setLinks(forbidden);
		HateoasLinks.setLinks(limited);
		HateoasLinks.setLinks(semiLimited);

	}

}