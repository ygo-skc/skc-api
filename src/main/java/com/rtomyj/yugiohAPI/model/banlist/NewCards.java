package com.rtomyj.yugiohAPI.model.banlist;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.rtomyj.yugiohAPI.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "numForbidden", "numLimited", "numSemiLimited", "forbidden", "limited", "semiLimited" })
@ApiModel(
		description = "All newly added cards or cards that have changed ban list status (forbidden, limited, semi-limited) separated by ban status."
		, parent = RepresentationModel.class
)
public class NewCards extends RepresentationModel<NewCards> implements HateoasLinks
{

	@ApiModelProperty(
			value = "Total new forbidden cards added to a ban list when compared to a previous logical ban list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private int numForbidden;

	@ApiModelProperty(
			value = "Total new limited cards added to a list when compared to a previous logical ban list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private int numLimited;

	@ApiModelProperty(
			value = "Total new semi-limited cards added to a ban list when compared to a previous logical ban list."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private int numSemiLimited;

	@ApiModelProperty(
			value = "List containing newly forbidden cards and their previous ban status."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private List<CardPreviousBanListStatus> forbidden;

	@ApiModelProperty(
			value = "List containing newly limited cards and their previous ban status."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private List<CardPreviousBanListStatus> limited;

	@ApiModelProperty(
			value = "List containing newly semi-limited cards and their previous ban status."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
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