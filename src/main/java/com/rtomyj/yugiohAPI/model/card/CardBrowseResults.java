package com.rtomyj.yugiohAPI.model.card;

import com.rtomyj.yugiohAPI.model.card.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardBrowseResults {

    private int numResults;
    private List<Card> results;

}
