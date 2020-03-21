package com.rtomyj.yugiohAPI.helper;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceLayerHelper
{
	private Object requestedResource;
	private HttpStatus status;
	@Builder.Default
	private Boolean inCache = false;
	@Builder.Default
	private Boolean isContentReturned = false;
}