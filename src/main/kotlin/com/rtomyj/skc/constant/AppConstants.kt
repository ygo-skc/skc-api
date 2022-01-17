package com.rtomyj.skc.constant

object AppConstants {
	var APP_VERSION: String = AppConstants::class.java.getPackage().implementationVersion ?: "LOCAL"
}