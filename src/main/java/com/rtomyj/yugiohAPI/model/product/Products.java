package com.rtomyj.yugiohAPI.model.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Data;

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
public class Products extends RepresentationModel<Products>
{

	@ApiModelProperty(
			value = "List of YuGiOh products."
			, accessMode = ApiModelProperty.AccessMode.READ_ONLY
	)
	private List<Product> products;

	private Class<Products> controllerClass;


	private void setLink()
	{

		// System.out.println(controllerClass);
		// if (controllerClass.getName().equals("PackController"))
		// this.add(
		// 	linkTo(methodOn(controllerClass).getPack(packId, packLocale)).withSelfRel()
		// );

	}


	public void setLinks()
	{
		this.setLink();
		Product.setLinks(products);
	}

}