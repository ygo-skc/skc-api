package com.rtomyj.yugiohAPI.model.banlist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.rtomyj.yugiohAPI.controller.banlist.BanListDatesController;

import com.rtomyj.yugiohAPI.model.HateoasLinks;
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
@ApiModel(description = "Start dates of ban lists.")
public class BanListStartDates extends RepresentationModel<BanListStartDates> implements HateoasLinks
{

	private List<BanList> banListStartDates;

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
		HateoasLinks.setLinks(this.banListStartDates);

	}

}