package com.rtomyj.yugiohAPI.model.pack;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Packs extends RepresentationModel<Packs>
{
	private List<Pack> packs;
}