package com.rtomyj.yugiohAPI.model.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rtomyj.yugiohAPI.controller.card.CardBrowseController;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(
        description = "Valid browse criteria and valid values per criteria. May also be used to identify which browse criteria is being used for browse results."
        , parent = RepresentationModel.class
)
public class CardBrowseCriteria extends RepresentationModel<CardBrowseCriteria> implements HateoasLinks
{

    @ApiModelProperty(
            value = "Unique set of identifiers for the type of card."
            , example = "normal, effect, fusion"
            , dataType = "List"
    )
    private Set<String> cardColors;

    @ApiModelProperty(
            value = "Unique set of identifiers of card attributes."
            , example = "Dark, Light, Earth, Wind, Water, Fire"
            , dataType = "List"
    )
    private Set<String> attributes;

    @ApiModelProperty(
            value = "Unique set of identifiers for monster types."
            , example = "Spellcaster, Wyrm, Warrior, etc"
            , dataType = "List"
    )
    private Set<String> monsterTypes;

    @ApiModelProperty(
            value = "Unique set of levels for monster cards in database."
            , example = "1, 2, 3, 4, 5, 6"
            , dataType = "List"
    )
    private Set<Integer> levels;

    @ApiModelProperty(
            value = "Unique set of ranks for monster cards in database."
            , example = "1, 2, 3, 4, 5, 6"
            , dataType = "List"
    )
    private Set<Integer> ranks;

    @ApiModelProperty(
            value = "Unique set of link rating values for Link monster cards in database."
            , example = "1, 2, 3, 4, 5, 6"
            , dataType = "List"
    )
    private Set<Integer> linkRatings;

    private static final Class<CardBrowseController> cardBrowseControllerClass = CardBrowseController.class;


    @Override
    public void setSelfLink()
    {

        this.add(
                linkTo(methodOn(cardBrowseControllerClass).browseCriteria())
                        .withSelfRel()
        );

    }


    @Override
    public void setLinks()
    {

        this.setSelfLink();

    }

}
