package com.rtomyj.skc.enums;


public enum ErrorTypes
{

	D101("URL requested doesn't have proper syntax.")
	, D001("Requested resource was not found.");

	private final String error;

	ErrorTypes(String error) { this.error = error; }

	@Override
	public String toString() { return this.error; }

}