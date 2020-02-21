package com.rtomyj.yugiohAPI.helper;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ServiceLayerHelper
{
	private Object requestedResource;
	private HttpStatus status;
	private Boolean inCache = false;
	private Boolean isContentReturned = false;
}