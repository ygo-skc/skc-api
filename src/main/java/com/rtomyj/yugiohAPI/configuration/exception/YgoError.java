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
	String message;
	String status;


	public static enum Error
	{
		D101("URL requested doesn't have proper syntax.")
		, D002("No content found for request.");

		private final String error;

		Error(String error) { this.error = error; }

		@Override
		public String toString() { return this.error; }
	}
}