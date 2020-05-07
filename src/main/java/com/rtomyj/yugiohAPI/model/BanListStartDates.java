package com.rtomyj.yugiohAPI.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.rtomyj.yugiohAPI.controller.banlist.DatesController;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Start dates of ban lists.")
public class BanListStartDates extends RepresentationModel<BanListStartDates>
{
	List<BanList> banListStartDates;

	private static final Class<DatesController> controllerClass = DatesController.class;



	private void setLink()
	{
		this.add(
			linkTo(methodOn(controllerClass).getStartDatesOfBanLists()).withSelfRel()
		);
	}



	public void setLinks()
	{
		this.setLink();
		BanList.setLinks(this.banListStartDates);
	}
}