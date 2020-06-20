package com.rtomyj.yugiohAPI.model.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@JsonInclude(Include.NON_EMPTY)
public class Products extends RepresentationModel<Products>
{
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