package com.rtomyj.yugiohAPI.controller.card;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.helper.Logging;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.model.card.CardBrowseResults;
import com.rtomyj.yugiohAPI.model.card.CardBrowseCriteria;
import com.rtomyj.yugiohAPI.service.card.CardBrowseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/card/browse", produces = "application/json; charset=UTF-8")
@CrossOrigin("*")
@Api(tags = {SwaggerConstants.TAG_CAR_TAG_NAMED})
public class CardBrowseController extends YgoApiBaseController
{

    private final CardBrowseService cardBrowseService;


    @Autowired
    public CardBrowseController(final CardBrowseService cardBrowseService)
    {

        this.cardBrowseService = cardBrowseService;

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
                    value = "Desired set of monster levels to include in browse results."
                    , example = "4,5,6,7,8"
            ) @RequestParam(value = "levels", defaultValue = "") final String monsterLevels
            , @ApiParam(
                    value = "Desired set of monster ranks to include in browse results."
                    , example = "4,7,8"
            ) @RequestParam(value = "ranks", defaultValue = "") final String monsterRanks
                    , @ApiParam(
                    value = "Desired set of monster ranks to include in browse results."
                    , example = "4,7,8"
            ) @RequestParam(value = "linkRatings", defaultValue = "") final String monsterLinkRatings
    )
    {

        return cardBrowseService.getBrowseResults(cardColors, attributes, monsterLevels, monsterRanks, monsterLinkRatings);

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

        return cardBrowseService.getBrowseCriteria();

    }

}
