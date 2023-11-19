package com.rtomyj.skc.util.constant

object AppConstants {
  var APP_VERSION: String = AppConstants::class.java.getPackage().implementationVersion ?: "LOCAL"
  const val APP_NAME = "skc-api"
  const val CLIENT_IP_MDC = "reqIp"
}