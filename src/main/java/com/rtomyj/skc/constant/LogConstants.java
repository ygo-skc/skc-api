package com.rtomyj.skc.constant;

public class LogConstants
{
	private LogConstants()
	{
		throw new UnsupportedOperationException("Cannot create instance for class: " + this.getClass().toString());
	}


	public static final String EXCEPTION_PROVIDER_LOG = "Exception occurred w/ message: {}, with ErrorType: {}, responding with: {}";

	public static final String ERROR_READING_OBJECT_USING_OBJECT_MAPPER = "Could not de-serialize object. Exception occurred: {}";
}