package com.rtomyj.skc.controller.stats;


import com.rtomyj.skc.controller.YgoApiBaseController;
import com.rtomyj.skc.helper.constants.SwaggerConstants;
import com.rtomyj.skc.model.stats.DatabaseStats;
import com.rtomyj.skc.model.stats.MonsterTypeStats;
import com.rtomyj.skc.service.stats.StatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 */
@RestController
@RequestMapping(path="/stats")
@CrossOrigin(origins="*")
@Slf4j
@Api(tags = SwaggerConstants.TAG_STATS_NAME)
public class StatsController extends YgoApiBaseController
{

    private final StatsService statsService;


    @Autowired
    public StatsController(final StatsService statsService)
    {

        this.statsService = statsService;

    }


    @ApiOperation(value = "Retrieve sum of all unique monster types for a given color of a card."
            , response = MonsterTypeStats.class
            , responseContainer = "Object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerConstants.http200)
            , @ApiResponse(code = 400, message = SwaggerConstants.http400)
            , @ApiResponse(code = 404, message = SwaggerConstants.http404)
    })
    @GetMapping("/card/monster_type/{cardColor}")
    public ResponseEntity<MonsterTypeStats> getMonsterTypeByColor(
            @ApiParam(
              value = SwaggerConstants.CARD_COLOR_DESCRIPTION
                    , defaultValue = "fusion"
            ) @NonNull @PathVariable("cardColor") final String cardColor
    )
    {

        // TODO: add loggers
        return ResponseEntity.ok(statsService.getMonsterTypeStats(cardColor));

    }


    @GetMapping()
    @ApiOperation(value = "Retrieve overview of the data currently in Database."
            , response = DatabaseStats.class
            , responseContainer = "Object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerConstants.http200)
            , @ApiResponse(code = 400, message = SwaggerConstants.http400)
            , @ApiResponse(code = 404, message = SwaggerConstants.http404)
    })
    public ResponseEntity<DatabaseStats> getDatabaseStats()
    {

        // TODO: add loggers
        return ResponseEntity.ok(statsService.getDatabaseStats());

    }

}
