package com.rtomyj.yugiohAPI.helper;

/**
 * Class containing convenience methods for frequently used logging templates.
 */
public class LogHelper
{
	/**
	 * Builds a String to be used in a logging method to record the status of the request from the user along with the users IP
	 * 	and the endpoint they requested.
	 * @param ip address of request
	 * @param endPoint requested endpoint of API
	 * @param comment Unique comment for log explaining what the process being executed.
	 * @return String to use in logger method.
	 */
	public static String requestStatusLogString(String ip, String endPoint, String comment)
	{
		// %s/{ %s } hit - responding with: { %s }
		String log = new StringBuilder().append(ip)
			.append(" requested ")
			.append(endPoint)
			.append(" - ")
			.append(comment)
			.toString();

		return log;
	}
}