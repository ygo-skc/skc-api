package com.rtomyj.skc.enums;


public enum ErrorType
{
	G001("URL requested doesn't have proper syntax")
	, D001("Requested resource was not found")
	, D002("Error occurred interfacing with resource(s)");

	private final String error;

	ErrorType(String error) { this.error = error; }

	@Override
	public String toString() { return this.error; }
}