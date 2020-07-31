package com.rtomyj.yugiohAPI.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)	// serializes non null fields - ie returns non null fields from REST request
public class MonsterAssociation
{

    private String level;

    private String rank;

    private String scaleRating;

    private String linkRating;
    private List<String> linkArrows;

}