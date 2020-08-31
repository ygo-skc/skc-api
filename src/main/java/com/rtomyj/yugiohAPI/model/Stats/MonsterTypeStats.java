package com.rtomyj.yugiohAPI.model.Stats;

import com.rtomyj.yugiohAPI.controller.stats.StatsController;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Builder
public class MonsterTypeStats extends RepresentationModel<MonsterTypeStats> implements HateoasLinks
{

    private String scope;
    private Map<String, Integer> monsterTypes;

    private static final Class<StatsController> statsControllerClass = StatsController.class;


    @Override
    public void setSelfLink()
    {

        this.add(
                linkTo(methodOn(statsControllerClass).getMonsterTypeByColor(scope)).withSelfRel()
        );

    }


    @Override
    public void setLinks()
    {

        this.setSelfLink();

    }


}
