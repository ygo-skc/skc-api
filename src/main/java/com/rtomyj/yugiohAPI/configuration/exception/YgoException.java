package com.rtomyj.yugiohAPI.configuration.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YgoException extends Exception
{
	private  String code;
	private String message;
}