package com.rtomyj.yugiohAPI.model.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.rtomyj.yugiohAPI.controller.product.ProductsController;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.helper.enumeration.products.ProductType;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Data;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@ApiModel(
		description = "Contains information about YuGiOh products."
		, parent = RepresentationModel.class
)
public class Products extends RepresentationModel<Products> implements HateoasLinks
{

	@ApiModelProperty(
			value = "List of YuGiOh products."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private List<Product> products;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String locale;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_TYPE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private ProductType productType;

	private static final Class<ProductsController> controllerClass = ProductsController.class;


	@Override
	public void setSelfLink()
	{

		this.add(
				linkTo(methodOn(controllerClass)
						.getProductsByLocaleAndProductType(productType, locale))
						.withSelfRel()
		);

	}


	@Override
	public void setLinks()
	{

		this.setSelfLink();
		Product.setLinks(products);

	}

}