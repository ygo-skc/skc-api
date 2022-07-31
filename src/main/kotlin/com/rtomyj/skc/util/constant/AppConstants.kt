package com.rtomyj.skc.util.constant

object AppConstants {
	var APP_VERSION: String = AppConstants::class.java.getPackage().implementationVersion ?: "LOCAL"
}