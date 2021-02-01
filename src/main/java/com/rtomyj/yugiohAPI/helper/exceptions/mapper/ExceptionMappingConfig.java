package com.rtomyj.yugiohAPI.helper.exceptions.mapper;


import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@ConfigurationProperties()
@Component
@Slf4j
@Data
@ToString
public class ExceptionMappingConfig {

    @PostConstruct()
    public void before()
    {
        log.debug("yyyyooooo");
        log.debug(test);
        log.debug(mapper.toString());
    }

//    private Map<String, Temp> mapper;

    private Map<String, MapperDirectives> mapper;

    private String test;

}
