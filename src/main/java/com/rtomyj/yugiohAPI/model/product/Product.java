package com.rtomyj.yugiohAPI.model.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rtomyj.yugiohAPI.model.product.pack.PackContent;

import lombok.Data;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@JsonInclude(Include.NON_EMPTY)
public class Product
{

	private String productId;
	private String locale;
	private String productName;
	private String productType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date productReleaseDate;

	private Integer productTotal;

	private Map<String, Integer> productRarityCount;
	private List<PackContent> productContent;

}