package com.rtomyj.yugiohAPI.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "Start dates of ban lists.")
public class BanListStartDates
{
	List<BanList> banListStartDates;
}