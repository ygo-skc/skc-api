package com.rtomyj.yugiohAPI.model.banlist;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.rtomyj.yugiohAPI.controller.banlist.BanListDatesController;

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
public class BanListStartDates extends RepresentationModel<BanListStartDates>
{

	private List<BanList> banListStartDates;

	private static final Class<BanListDatesController> controllerClass = BanListDatesController.class;


	private void setLink()
	{

		this.add(
			linkTo(methodOn(controllerClass).getBanListStartDates()).withSelfRel()
		);

	}


	public void setLinks()
	{

		this.setLink();
		BanList.setLinks(this.banListStartDates);

	}

}