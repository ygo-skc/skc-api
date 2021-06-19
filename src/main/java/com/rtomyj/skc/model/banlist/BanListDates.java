package com.rtomyj.skc.model.banlist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.rtomyj.skc.controller.banlist.BanListDatesController;

import com.rtomyj.skc.model.HateoasLinks;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Start dates of ban lists.", parent = RepresentationModel.class, discriminator = "links")
public class BanListDates extends RepresentationModel<BanListDates> implements HateoasLinks
{

	@ApiParam(
			value = "Array of objects containing valid start dates of all ban lists currently in DB."
	)
	private List<BanListDate> banListDates;

	private static final Class<BanListDatesController> controllerClass = BanListDatesController.class;


	@Override
	public void setSelfLink()
	{

		this.add(
			linkTo(methodOn(controllerClass).getBanListStartDates()).withSelfRel()
		);

	}


	@Override
	public void setLinks()
	{

		this.setSelfLink();
		HateoasLinks.setLinks(this.banListDates);

	}

}