package com.rtomyj.yugiohAPI.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)	// serializes non null fields - ie returns non null fields from REST request
public class MonsterAssociation
{

    private Integer level;

    private Integer rank;

    private Integer scaleRating;

    private Integer linkRating;
    private List<String> linkArrows;

}