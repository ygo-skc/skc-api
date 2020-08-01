package com.rtomyj.yugiohAPI.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CardBrowseCriteria
{

    private Set<String> cardColors;
    private Set<String> attributes;
    private Set<Integer> levels;

}
