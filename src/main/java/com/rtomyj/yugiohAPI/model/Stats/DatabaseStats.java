package com.rtomyj.yugiohAPI.model.Stats;


import com.rtomyj.yugiohAPI.controller.stats.StatsController;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Builder
public class DatabaseStats extends RepresentationModel<DatabaseStats> implements HateoasLinks
{

    private int productTotal;
    private int cardTotal;
    private int banListTotal;
    private int yearsOfBanListCoverage;

    private static final Class<StatsController> statsControllerClass = StatsController.class;


    @Override
    public void setSelfLink()
    {

        this.add(
                linkTo(methodOn(statsControllerClass).getDatabaseStats()).withSelfRel()
        );

    }


    @Override
    public void setLinks()
    {
        this.setSelfLink();
    }

}
