package com.rtomyj.skc.exception

import lombok.ToString
import org.springframework.http.HttpStatus

@ToString
enum class ErrorType(val error: String, val httpStatus: HttpStatus) {
	G001("URL or data in body doesn't use proper syntax", HttpStatus.NOT_FOUND)

	, D001("Requested resource was not found", HttpStatus.NOT_FOUND)
	, D002("Error occurred interfacing with resource(s)", HttpStatus.INTERNAL_SERVER_ERROR);
}