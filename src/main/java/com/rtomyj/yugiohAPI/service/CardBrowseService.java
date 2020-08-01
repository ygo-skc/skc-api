package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardBrowseService
{

    private Dao dao;


    public CardBrowseService(@Autowired @Qualifier("jdbc") Dao dao)
    {

        this.dao = dao;

    }


    public String getBrowseResults()
    {

        log.info("THE SERVICE LAYER HAS BEEN HIT!!!!!!");
        return "Y000";

    }

}
