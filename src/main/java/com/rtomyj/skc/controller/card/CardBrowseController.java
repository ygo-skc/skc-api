package com.rtomyj.skc.controller.card;

import com.google.common.base.Suppliers;
import com.rtomyj.skc.controller.YgoApiBaseController;
import com.rtomyj.skc.constant.SwaggerConstants;
import com.rtomyj.skc.model.card.CardBrowseCriteria;
import com.rtomyj.skc.model.card.CardBrowseResults;
import com.rtomyj.skc.service.card.CardBrowseService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@RestController
@RequestMapping(path = "/card/browse", produces = "application/json; charset=UTF-8")
@CrossOrigin("*")
@Api(tags = {SwaggerConstants.TAG_CAR_TAG_NAMED})
@Slf4j
public class CardBrowseController extends YgoApiBaseController
{
    //ToDo: javadoc this class

    private final CardBrowseService cardBrowseService;

    private final Supplier<CardBrowseCriteria> cardBrowseCriteriaSupplier;


    @Autowired
    public CardBrowseController(final CardBrowseService cardBrowseService)
    {
        this.cardBrowseService = cardBrowseService;
        this.cardBrowseCriteriaSupplier  = Suppliers.memoizeWithExpiration(cardBrowseService::getBrowseCriteria, 10, TimeUnit.MINUTES);
    }


    @GetMapping()
    @ApiOperation(value = "Fetches cards given a set of criteria (use /api/v1/browse/criteria for valid criteria)."
            , response = CardBrowseResults.class
            , responseContainer = "Object"
    )
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = SwaggerConstants.http200)
    })
    public CardBrowseResults browse(
            @ApiParam(
                    value = "Desired set of card types to include in browse results."
                    , example = "effect,fusion"
            ) @RequestParam(value = "cardColors", defaultValue = "") final String cardColors
            , @ApiParam(
                    value = "Desired set of attributes to include in browse results."
                    , example = "wind,dark,light"
            ) @RequestParam(value = "attributes", defaultValue = "") final String attributes
            , @ApiParam(
                    value = "Desired set of monster types to include in browse results."
                    , example = "spellcaster,wyrm,warrior"
            ) @RequestParam(value = "monsterTypes", defaultValue = "") final String monsterTypes
            , @ApiParam(
                    value = "Desired set of monster sub types to include in browse results."
                    , example = "flip,gemini,toon"
            ) @RequestParam(value = "monsterSubTypes", defaultValue = "") final String monsterSubTypes
            , @ApiParam(
                    value = "Desired set of monster levels to include in browse results."
                    , example = "4,5,6,7,8"
            ) @RequestParam(value = "levels", defaultValue = "") final String monsterLevels
            , @ApiParam(
                    value = "Desired set of monster ranks to include in browse results."
                    , example = "4,7,8"
            ) @RequestParam(value = "ranks", defaultValue = "") final String monsterRanks
                    , @ApiParam(
                    value = "Desired set of monster ranks to include in browse results  ."
                    , example = "4,7,8"
            ) @RequestParam(value = "linkRatings", defaultValue = "") final String monsterLinkRatings
    )
    {

        log.info("Retrieving browse results.");
        final CardBrowseResults cardBrowseResults = cardBrowseService.getBrowseResults(cardColors, attributes, monsterTypes, monsterSubTypes, monsterLevels, monsterRanks, monsterLinkRatings);
        log.info("Successfully retrieved card browse results using criteria: [ cardColors={}, attributes={}, monsterTypes={}, monsterLevels={}, monsterRanks={}, monsterLinkRatings={} ]. Found {} matching results."
                , cardColors, attributes, monsterTypes, monsterLevels, monsterRanks, monsterLinkRatings, cardBrowseResults.getNumResults());

        return cardBrowseResults;

    }


    @GetMapping("/criteria")
    @ApiOperation(value = "Fetches valid criteria and valid values for each criteria that can be used in browse endpoint."
            , response = CardBrowseCriteria.class
            , responseContainer = "Object"
    )
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = SwaggerConstants.http200)
    })
    public CardBrowseCriteria browseCriteria()
    {
        log.info("Retrieving browse criteria.");
        final CardBrowseCriteria cardBrowseCriteria = cardBrowseCriteriaSupplier.get();
        log.info("Successfully retrieved browse criteria for cards.");

        return cardBrowseCriteria;
    }

}
