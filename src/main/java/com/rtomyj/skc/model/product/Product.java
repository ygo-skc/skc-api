package com.rtomyj.skc.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.controller.product.ProductController;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.With;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Data
@Builder
@With
@EqualsAndHashCode(callSuper=false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@ApiModel(
		description = "Information about a Yu-Gi-Oh! product such as a booster pack or tin."
)
public class Product extends RepresentationModel<Product>
{

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_ID_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String productId;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String productLocale;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_NAME_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String productName;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_TYPE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String productType;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_SUB_TYPE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private String productSubType;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_RELEASE_DATE_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date productReleaseDate;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_TOTAL_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private Integer productTotal;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_RARITY_STATS_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private Map<String, Integer> productRarityStats;

	@ApiModelProperty(
			value = SwaggerConstants.PRODUCT_CONTENT_DESCRIPTION
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private List<ProductContent> productContent;

	private static final Class<ProductController> controllerClass = ProductController.class;


	private void setLink()
	{
		this.add(
			linkTo(methodOn(controllerClass).getProduct(productId, productLocale)).withSelfRel()
		);
	}


	public void setLinks()
	{
		this.setLink();
		if (this.productContent != null)	ProductContent.setLinks(this.productContent);	// set links for pack contents
	}


	public static void setLinks(@NonNull final List<Product> products)
	{
		products
			.forEach(Product::setLinks);
	}
}