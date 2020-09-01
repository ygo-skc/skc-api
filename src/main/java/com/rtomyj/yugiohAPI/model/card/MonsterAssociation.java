package com.rtomyj.yugiohAPI.model.card;


import com.fasterxml.jackson.annotation.JsonInclude;
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
public class MonsterAssociation
{

    private Integer level;

    private Integer rank;

    private Integer scaleRating;

    private Integer linkRating;
    private List<String> linkArrows;

}