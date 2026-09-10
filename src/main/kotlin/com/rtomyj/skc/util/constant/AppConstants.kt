package com.rtomyj.skc.util.constant

object AppConstants {
    val APP_VERSION: String = AppConstants::class.java.getPackage().implementationVersion ?: "LOCAL"
    const val APP_NAME = "skc-api"
    const val CLIENT_IP_MDC = "reqIp"
}
