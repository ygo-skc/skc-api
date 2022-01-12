package com.rtomyj.skc.service.stats;

import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.exception.ErrorType;
import com.rtomyj.skc.exception.YgoException;
import com.rtomyj.skc.model.stats.DatabaseStats;
import com.rtomyj.skc.model.stats.MonsterTypeStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class StatsService {
    private final Dao dao;


    @Autowired
    public StatsService(@Qualifier("jdbc") final Dao dao)
    {
        this.dao = dao;
    }


    public MonsterTypeStats getMonsterTypeStats(final String cardColor)
    {
        final MonsterTypeStats monsterTypeStats = dao.getMonsterTypeStats(cardColor);

        if (monsterTypeStats.getMonsterTypes().isEmpty()) {
            throw new YgoException("Requested monster type not found in DB", ErrorType.D001);
        }

        monsterTypeStats.setLinks();
        return monsterTypeStats;
    }


    public DatabaseStats getDatabaseStats()
    {
        final DatabaseStats databaseStats = dao.getDatabaseStats();
        databaseStats.setLinks();

        return databaseStats;
    }
}
