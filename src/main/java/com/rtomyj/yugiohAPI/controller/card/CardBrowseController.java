package com.rtomyj.yugiohAPI.controller.card;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.helper.Logging;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.model.BrowseResults;
import com.rtomyj.yugiohAPI.model.CardBrowseCriteria;
import com.rtomyj.yugiohAPI.service.CardBrowseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    private static final String END_POINT = BASE_ENDPOINT + "/card/browse";

    private final CardBrowseService cardBrowseService;


    @Autowired
    public CardBrowseController(final HttpServletRequest request, final CardBrowseService cardBrowseService)
    {

        this.request = request;
        this.cardBrowseService = cardBrowseService;

    }


    @GetMapping()
    @ApiOperation(value = "Fetches cards given a set of criteria (use /api/v1/browse/criteria for valid options)."
            , response = BrowseResults.class
            , responseContainer = "Object"
    )
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = SwaggerConstants.http200)
    })
    public BrowseResults browse(
            @RequestParam(value = "cardColors", defaultValue = "") final String cardColors
            , @RequestParam(value = "attributes", defaultValue = "") final String attributes
            , @RequestParam(value = "levels", defaultValue = "") final String monsterLevels
    )
    {

        Logging.configureMDC(request, END_POINT);
        final BrowseResults browseResults = cardBrowseService.getBrowseResults(cardColors, attributes, monsterLevels);
        MDC.clear();

        return browseResults;

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

        Logging.configureMDC(request, END_POINT + "/criteria");
        final CardBrowseCriteria cardBrowseCriteria = cardBrowseService.getBrowseCriteria();
        MDC.clear();

        return cardBrowseCriteria;

    }

}
