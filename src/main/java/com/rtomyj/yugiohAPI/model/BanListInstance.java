package com.rtomyj.yugiohAPI.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
@ApiModel(description = "Describes and contains information about a specific ban list.")
@JsonTypeName("bannedCards")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonPropertyOrder({ "startDate", "numForbidden", "numLimited", "numSemiLimited", "forbidden", "limited", "semiLimited" })
public class BanListInstance extends RepresentationModel<BanListInstance>
{
	private String startDate;
	private int numForbidden;
	private int numLimited;
	private int numSemiLimited;
	private List<Card> forbidden;
	private List<Card> limited;
	private List<Card> semiLimited;

}