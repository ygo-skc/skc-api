package com.rtomyj.skc.model.card;

import com.rtomyj.skc.controller.card.CardBrowseController;
import com.rtomyj.skc.model.HateoasLinks;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
        description = "Card information for cards that fit requested browse criteria."
        , parent = RepresentationModel.class
)
public class CardBrowseResults extends RepresentationModel<CardBrowseResults> implements HateoasLinks {

    @ApiModelProperty(
            value = "Total browse results."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private int numResults;

    @ApiModelProperty(
            value = "Criteria used to fetch these results."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private CardBrowseCriteria requestedCriteria;

    @ApiModelProperty(
            value = "Card info of all cards that fit criteria requested by user."
            , accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private List<Card> results;

    private static final Class<CardBrowseController> cardBrowseController = CardBrowseController.class;


    // TODO: add links
    @Override
    public void setSelfLink()
    {
    }


    @Override
    public void setLinks()
    {

        this.setSelfLink();

        HateoasLinks.setLinks(results);

    }

}
