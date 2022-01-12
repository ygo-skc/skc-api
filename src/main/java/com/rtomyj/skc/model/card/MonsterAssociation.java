package com.rtomyj.skc.model.card;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtomyj.skc.enums.LinkArrow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_EMPTY)	// serializes non null fields - ie returns non null fields from REST request
@ApiModel(
        description = "Information about various properties only monster cards have that usually associate distinct monster cards (not in the same archetype) together."
)
@Slf4j
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


    public static MonsterAssociation parseDBString(final String dbMonsterAssociationJson, final ObjectMapper objectMapper) {
        try {
            if (dbMonsterAssociationJson != null)
                return objectMapper.readValue(dbMonsterAssociationJson, MonsterAssociation.class);
        } catch (JsonProcessingException e) {
            log.error("Exception occurred when parsing monster association column, {}", e.toString());
            return null;
        }

        return null;
    }


    /**
     * Takes monster link rating retrieved from DB (constants denoting position of arrow, eg: T-L (top left), T-R (top right)... etc) and converts them to emojis.
     */
    public void transformMonsterLinkRating() {
        if (this.getLinkArrows() != null && !this.getLinkArrows().isEmpty()) {
            this.setLinkArrows(
                    this
                            .getLinkArrows()
                            .stream()
                            .map(dbArrowString -> LinkArrow.transformDBStringToEnum(dbArrowString).toString())
                            .toList()
            );
        }
    }


    /**
     * Calls {@link #transformMonsterLinkRating()} on a list of Cards
     * @param cards list of cards whose link rating should be transformed
     */
    public static void transformMonsterLinkRating(final List<Card> cards) {
        cards
                .stream()
                .map(Card::getMonsterAssociation)
                .filter(Objects::nonNull)
                .forEach(MonsterAssociation::transformMonsterLinkRating);
    }
}