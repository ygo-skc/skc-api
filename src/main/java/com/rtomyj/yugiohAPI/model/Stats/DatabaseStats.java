package com.rtomyj.yugiohAPI.model.Stats;


import com.rtomyj.yugiohAPI.controller.stats.StatsController;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Builder
@ApiModel(
        description = "High level stats for data in the database."
        , parent = RepresentationModel.class
)
public class DatabaseStats extends RepresentationModel<DatabaseStats> implements HateoasLinks
{

    @ApiModelProperty(
            value = "Total number of products in the database."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private int productTotal;

    @ApiModelProperty(
            value = "Total number of cards in the database."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private int cardTotal;

    @ApiModelProperty(
            value = "Total number of ban lists in the database."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private int banListTotal;

    @ApiModelProperty(
            value = "Total number of years spanned/covered by ban lists stored in database."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
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
