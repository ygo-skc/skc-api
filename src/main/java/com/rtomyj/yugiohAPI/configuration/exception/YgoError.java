package com.rtomyj.yugiohAPI.configuration.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YgoError
{

	private static final long serialVersionUID = 1L;

	String message;
	String status;


	public static enum Error
	{
		D001("Input data does not conform to the expected spec.")
		, D002("No content found for request.");

		private final String error;

		Error(String error) { this.error = error; }

		@Override
		public String toString() { return this.error; }
	}
}