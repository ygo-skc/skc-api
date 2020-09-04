package com.rtomyj.yugiohAPI.model.banlist;

import com.rtomyj.yugiohAPI.controller.card.CardController;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@ApiModel(
		description = "Contains info about a card that had its ban status (forbidden, limited, semi-limited) changed in reference to a previous logical ban list."
		, parent = RepresentationModel.class
)
public class CardPreviousBanListStatus extends RepresentationModel<CardPreviousBanListStatus> implements HateoasLinks
{

	@ApiModelProperty(
			value = SwaggerConstants.CARD_NAME_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String cardName;

	@ApiModelProperty(
			value = SwaggerConstants.CARD_ID_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String cardId;

	@ApiModelProperty(
			value = "The previous ban status the card had when compared to current ban list."
			,  accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String previousBanStatus;

	private static final Class<CardController> controllerClass = CardController.class;


	@Override
	public void setSelfLink()
	{
		this.add(
			linkTo(methodOn(controllerClass).getCard(cardId, false)).withSelfRel()
		);
	}


	@Override
	public void setLinks()
	{

		this.setSelfLink();

	}

}