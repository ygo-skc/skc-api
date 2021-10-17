package com.rtomyj.skc.model.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.model.card.Card;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)	// serializes non null fields - ie returns non null fields from REST request
@ApiModel(parent = RepresentationModel.class)
public class ProductContent extends RepresentationModel<ProductContent>
{

	@ApiModelProperty(value = "Information about card.")
	private Card card;

	@ApiModelProperty(value = SwaggerConstants.CARD_POSITION_IN_PRODUCT_DESCRIPTION)
	private String productPosition;

	@ApiModelProperty(value = SwaggerConstants.CARD_RARITIES_FOR_POSITION_DESCRIPTION)
	private Set<String> rarities;


	public void setLinks()
	{
		this.card.setLinks();
	}


	public static void setLinks(@NonNull final List<ProductContent> packContents)
	{
		packContents
			.forEach(packContent -> packContent.setLinks());
	}

}