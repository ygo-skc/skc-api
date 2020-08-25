package com.rtomyj.yugiohAPI.model.product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.yugiohAPI.controller.product.ProductController;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;


@Data
@Builder
@With
@EqualsAndHashCode(callSuper=false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Information about a YGO pack.")
@JsonInclude(Include.NON_EMPTY)
public class Product extends RepresentationModel<Product>
{

	private String productId;
	private String productLocale;
	private String productName;
	private String productType;
	private String productSubType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date productReleaseDate;
	private Integer productTotal;
	private Map<String, Integer> productRarityCount;
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



	public static void setLinks(final List<Product> packs)
	{
		packs
			.stream()
			.forEach(pack -> pack.setLinks());
	}

}