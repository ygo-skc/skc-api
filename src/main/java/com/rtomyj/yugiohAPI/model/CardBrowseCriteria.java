package com.rtomyj.yugiohAPI.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CardBrowseCriteria
{

    @ApiModelProperty(value = "Unique set of identifiers for the type of card."
            , example = "normal, effect, fusion"
            , dataType = "List")
    private Set<String> cardColors;

    @ApiModelProperty(value = "Unique set of identifiers of card attributes."
            , example = "Dark, Light, Earth, Wind, Water, Fire"
            , dataType = "List")
    private Set<String> attributes;

    @ApiModelProperty(value = "Unique set of levels for monster cards in database."
            , example = "1, 2, 3, 4, 5, 6"
            , dataType = "List")
    private Set<Integer> levels;

    @ApiModelProperty(value = "Unique set of ranks for monster cards in database."
            , example = "1, 2, 3, 4, 5, 6"
            , dataType = "List")
    private Set<Integer> ranks;

    @ApiModelProperty(value = "Unique set of link rating values for Link monster cards in database."
            , example = "1, 2, 3, 4, 5, 6"
            , dataType = "List")
    private Set<Integer> linkRatings;

}
