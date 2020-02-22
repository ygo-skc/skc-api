package com.rtomyj.yugiohAPI.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Describes and contains information about a specific ban list.")
@JsonTypeName("bannedCards")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonPropertyOrder({ "date", "forbidden", "limited", "semiLimited" })
public class BanListInstance
{
	private String startDate;
	private List<Card> forbidden;
	private List<Card> limited;
	private List<Card> semiLimited;


	public void clear()
	{
		if (this.forbidden != null)	this.forbidden.clear();
		if (this.limited != null)	this.semiLimited.clear();
		if (this.semiLimited != null)	this.semiLimited.clear();
	}
}