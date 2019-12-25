package com.rtomyj.yugiohAPI.helper;

import org.springframework.http.HttpStatus;

/**
 * Class containing convenience methods for frequently used logging templates.
 */
public class LogHelper
{
	/**
	 * Builds a String to be used in a logger to record the status of the request from the user along with the users IP
	 * 	and the endpoint they requested.
	 * @param ip address of request.
	 * @param resourceRequested String representation of the what the user was trying to query.
	 * @param endPoint requested endpoint of API.
	 * @param status Unique comment for log explaining what the process being executed.
	 * @return String to use in logger method.
	 */
	public static String requestStatusLogString(String ip, String resourceRequested, String endPoint, HttpStatus status)
	{
		return new StringBuilder().append(ip)
			.append(" requested ")
			.append(String.format("(( %s ))", resourceRequested))
			.append(" from ")
			.append(endPoint)
			.append(" - ")
			.append(String.format("Responding with {{ %s }}", status))
			.toString();
	}



	/**
	 * Builds a String to be used in a logger to record the status of the request from the user along with the users IP
	 * 	and the endpoint they requested.
	 * @param ip address of request.
	 * @param resourceRequested String representation of the what the user was trying to query.
	 * @param endPoint requested endpoint of API.
	 * @param status Unique comment for log explaining what the process being executed.
	 * @param retrievedFromCache Whether a cache was used to obtain the resource.
	 * @param isResourceReturned Whether something is returned to the user from an endpoint
	 * @return String to use in logger method.
	 */
	public static String requestStatusLogString(String ip, String resourceRequested, String endPoint, HttpStatus status
		, boolean retrievedFromCache, boolean isResourceReturned)
	{
		String locationOfResource = (retrievedFromCache) ? ": {{ From CACHE }}": ": {{ From DATABASE }}";
		locationOfResource = (isResourceReturned)? locationOfResource: "";

		return new StringBuilder()
			.append(LogHelper.requestStatusLogString(ip, resourceRequested, endPoint, status))
			.append( locationOfResource )
			.toString();
	}
}