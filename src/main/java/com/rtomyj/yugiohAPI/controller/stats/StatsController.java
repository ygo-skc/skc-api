package com.rtomyj.yugiohAPI.controller.stats;


import com.google.inject.internal.util.Strings;
import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.model.Stats.DatabaseStats;
import com.rtomyj.yugiohAPI.model.Stats.MonsterType;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 *
 */
@RestController
@RequestMapping(path = YgoApiBaseController.BASE_ENDPOINT + "/stats")
@CrossOrigin(origins = "*")
@Slf4j
public class StatsController extends YgoApiBaseController
{


    private Dao dao;


    @Autowired
    public StatsController(final HttpServletRequest request, @Qualifier("jdbc") final Dao dao)
    {

        this.request = request;
        this.dao = dao;

    }


    @GetMapping("/card/monster_type/{cardColor}")
    public MonsterType getMonsterTypeByColor(@NonNull @PathVariable("cardColor") final String cardColor)
    {
        return dao.getMonsterTypeStats(Strings.capitalize(cardColor));
    }


    @GetMapping()
    public DatabaseStats getDatabaseStats()
    {
        // TODO: add loggers
        log.info("Stat base endpoint hit");
        return dao.getDatabaseStats();
    }

}
