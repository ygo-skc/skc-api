package com.rtomyj.skc.model.stats;

import com.rtomyj.skc.controller.stats.StatsController;
import com.rtomyj.skc.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Builder
@ApiModel(
        description = "Statistics for monster types stored in Database."
        , parent = RepresentationModel.class
)
@EqualsAndHashCode(callSuper = true)
public class MonsterTypeStats extends RepresentationModel<MonsterTypeStats> implements HateoasLinks
{
    @ApiModelProperty(
            value = "The scope or filter used when retrieving monster type stats."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private String scope;

    @ApiModelProperty(
            value = "Monster types and the total number of cards currently in Database that have the type for given scope."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
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
