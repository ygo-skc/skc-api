package com.rtomyj.yugiohAPI.model.Stats;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatabaseStats
{

    private int productTotal;
    private int cardTotal;
    private int banListTotal;

}
