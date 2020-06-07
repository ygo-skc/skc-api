package com.rtomyj.yugiohAPI.model.Stats;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MonsterType
{

    private String scope;
    private Map<String, Integer> monsterTypes;

}
