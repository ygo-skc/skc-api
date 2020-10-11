package com.rtomyj.yugiohAPI.helper.exceptions;


public enum Error
{

	D101("URL requested doesn't have proper syntax.")
	, D001("Requested resource was not found.");

	private final String error;

	Error(String error) { this.error = error; }

	@Override
	public String toString() { return this.error; }

}