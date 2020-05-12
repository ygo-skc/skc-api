package com.rtomyj.yugiohAPI.model.product.pack;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.yugiohAPI.controller.product.PackController;
import com.rtomyj.yugiohAPI.model.product.Product;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@With
@EqualsAndHashCode(callSuper=false)
@ApiModel(description = "Information about a YGO pack.")
@JsonInclude(Include.NON_EMPTY)
public class Pack extends Product
{

	private static final Class<PackController> controllerClass = PackController.class;


	// private void setLink()
	// {
	// 	this.add(
	// 		linkTo(methodOn(controllerClass).getPack(getProductId(), getLocale())).withSelfRel()
	// 	);
	// }


	// public void setLinks()
	// {
	// 	this.setLink();
	// 	if (this.getPackContent() != null)	PackContent.setLinks(this.getPackContent());	// set links for pack contents
	// }



	// public static void setLinks(final List<Pack> packs)
	// {
	// 	packs
	// 		.stream()
	// 		.forEach(pack -> pack.setLinks());
	// }
}