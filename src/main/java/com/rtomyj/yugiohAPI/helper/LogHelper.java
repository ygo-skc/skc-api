package com.rtomyj.yugiohAPI.helper;

public class LogHelper
{
	public static String requestInfo(String ip, String endPoint, String comment)
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