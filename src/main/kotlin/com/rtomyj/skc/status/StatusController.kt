package com.rtomyj.skc.status

import com.rtomyj.skc.config.ReactiveMDC
import com.rtomyj.skc.dao.StatusDao
import com.rtomyj.skc.model.StatusResponse
import com.rtomyj.skc.skcsuggestionengine.SuggestionEngineStatusService
import com.rtomyj.skc.util.YgoApiBaseController
import com.rtomyj.skc.util.constant.AppConstants
import com.rtomyj.skc.util.constant.SwaggerConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


/**
 * Configures endpoint(s) for testing the health of the API.
 */
@RestController
@RequestMapping(path = ["/status"], produces = ["application/json; charset=UTF-8"])
@Tag(name = SwaggerConstants.STATUS_CALL_TAG_NAME)
class StatusController @Autowired constructor(
  @Qualifier("jdbc") val dao: StatusDao, val suggestionEngineStatusService: SuggestionEngineStatusService
) : YgoApiBaseController() {

  companion object {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
  }


  /**
   * Retrieve basic info of the API and status on all dependant downstream services.
   * @return Status info.
   */
  @GetMapping
  @Operation(
    summary = "Checking status of the API.", tags = [SwaggerConstants.STATUS_CALL_TAG_NAME]
  )
  @ApiResponse(responseCode = "200", description = SwaggerConstants.HTTP_200_SWAGGER_MESSAGE)
  fun status(): ResponseEntity<Mono<StatusResponse>> = ResponseEntity.ok(ReactiveMDC.deferMDC(Flux
      .concat(suggestionEngineStatusService.getStatus(), Mono.fromCallable(dao::dbConnection))
      .doOnSubscribe {
        log.info("Status of API was requested")
      }
      .collectMultimap { dsStatus ->
        if (dsStatus.name == "SKC DB") "critical" else "utility"
      }
      .map { downstreamStatus ->
        val criticalPathDown = downstreamStatus
            .getValue("critical")
            .stream()
            .filter {
              it.status.equals("down", ignoreCase = true)
            }
            .findAny().isPresent

        StatusResponse(
          if (criticalPathDown) "API is online but functionality is impacted" else "API is online and functional",
          AppConstants.APP_VERSION,
          downstreamStatus.values.flatten()
        )
      })
  )
}