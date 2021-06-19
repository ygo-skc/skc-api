package com.rtomyj.skc.model.card;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)	// serializes non null fields - ie returns non null fields from REST request
@ApiModel(
        description = "Information about various properties only monster cards have that usually associate distinct monster cards (not in the same archetype) together."
)
public class MonsterAssociation
{

    @ApiModelProperty(
            value = "The star rating of a monster card - determines tribute count."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private Integer level;

    @ApiModelProperty(
            value = "Like level, except it determines which monsters can be used to summon the card."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private Integer rank;

    @ApiModelProperty(
            value = "Pendulum rating used to perform pendulum summoning."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private Integer scaleRating;

    @ApiModelProperty(
            value = "Number value determining link cost in summoning Link monster."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private Integer linkRating;

    @ApiModelProperty(
            value = "Positions of Link arrows for link monster."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private List<String> linkArrows;

}