package com.rtomyj.yugiohAPI.model.card;

import com.rtomyj.yugiohAPI.controller.card.CardBrowseController;
import com.rtomyj.yugiohAPI.model.HateoasLinks;
import com.rtomyj.yugiohAPI.model.card.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardBrowseResults extends RepresentationModel<CardBrowseResults> implements HateoasLinks {

    private int numResults;
    private CardBrowseCriteria requestedCriteria;
    private List<Card> results;

    private static final Class<CardBrowseController> cardBrowseController = CardBrowseController.class;


    @Override
    public void setSelfLink()
    {

        this.add(
                linkTo(methodOn(cardBrowseController).browse("", "", "")).withSelfRel()
        );

    }


    @Override
    public void setLinks()
    {

        this.setSelfLink();

        HateoasLinks.setLinks(results);

    }

}
