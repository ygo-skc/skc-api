package com.rtomyj.yugiohAPI.model.pack;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.yugiohAPI.controller.products.PackController;

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
public class Pack extends RepresentationModel<Pack>
{

	private String packId;
	private String locale;
	private String packName;
	private String productType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date packReleaseDate;
	private Integer packTotal;
	private Map<String, Integer> packRarityCount;
	private List<PackContent> packContent;

	private static final Class<PackController> controllerClass = PackController.class;


	private void setLink()
	{
		this.add(
			linkTo(methodOn(controllerClass).getPack(packId, locale)).withSelfRel()
		);
	}


	public void setLinks()
	{
		this.setLink();
		if (this.packContent != null)	PackContent.setLinks(this.packContent);	// set links for pack contents
	}



	public static void setLinks(final List<Pack> packs)
	{
		packs
			.stream()
			.forEach(pack -> pack.setLinks());
	}
}