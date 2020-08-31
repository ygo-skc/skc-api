package com.rtomyj.yugiohAPI.controller.stats;


import com.google.inject.internal.util.Strings;
import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.model.Stats.DatabaseStats;
import com.rtomyj.yugiohAPI.model.Stats.MonsterType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


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

    private static final String END_POINT = BASE_ENDPOINT + "/stats";

    private final Dao dao;


    @Autowired
    public StatsController(final HttpServletRequest request, @Qualifier("jdbc") final Dao dao)
    {

        this.request = request;
        this.dao = dao;

    }


    @ApiOperation(value = "Retrieve sum of all unique monster types for a given color of a card."
            , response = MonsterType.class
            , responseContainer = "Object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerConstants.http200)
            , @ApiResponse(code = 400, message = SwaggerConstants.http400)
            , @ApiResponse(code = 404, message = SwaggerConstants.http404)
    })
    @GetMapping("/card/monster_type/{cardColor}")
    public ResponseEntity<MonsterType> getMonsterTypeByColor(
            @ApiParam(
              value = SwaggerConstants.CARD_COLOR_DESCRIPTION
                    , defaultValue = "fusion"
            ) @NonNull @PathVariable("cardColor") final String cardColor
    )
    {

        return ResponseEntity.ok(dao.getMonsterTypeStats(Strings.capitalize(cardColor)));

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
        log.info("Stat base endpoint hit");
        return ResponseEntity.ok(dao.getDatabaseStats());

    }

}
