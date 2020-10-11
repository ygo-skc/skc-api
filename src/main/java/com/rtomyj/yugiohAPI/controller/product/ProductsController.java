package com.rtomyj.yugiohAPI.controller.product;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.helper.enumeration.products.ProductType;
import com.rtomyj.yugiohAPI.model.product.Products;
import com.rtomyj.yugiohAPI.service.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path = "/products", produces = "application/json; charset=UTF-8")
@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = {SwaggerConstants.TAG_PRODUCT_TAG_NAME})
public class ProductsController extends YgoApiBaseController
{

    private static final String END_POINT = BASE_ENDPOINT + "/products";

    private final ProductService availableProductsService;


    @Autowired
    public ProductsController(final HttpServletRequest request, final ProductService availableProductsService)
    {

        this.availableProductsService = availableProductsService;
        this.request = request;

    }


    @GetMapping("/{locale}")
    @ApiOperation(value = "Retrieve all products for a given locale."
            , response = Products.class
            , responseContainer = "Object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerConstants.http200)
            , @ApiResponse(code = 400, message = SwaggerConstants.http400)
            , @ApiResponse(code = 404, message = SwaggerConstants.http404)
    })
    public ResponseEntity<Products> getProductsByLocale(
            @ApiParam(value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION, example = "en") @PathVariable("locale") final String locale)
    {

        return ResponseEntity.ok(availableProductsService.getProductsByLocale(locale));

    }


    @GetMapping("/{productType}/{locale}")
    @ApiOperation(value = "Retrieve products that fit a certain product type and locale."
            , response = Products.class
            , responseContainer = "Object")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerConstants.http200)
            , @ApiResponse(code = 400, message = SwaggerConstants.http400)
            , @ApiResponse(code = 404, message = SwaggerConstants.http404)
    })
    public ResponseEntity<Products> getProductsByLocaleAndProductType(
            @ApiParam(value = "A specific product type used to limit results.") @PathVariable("productType") final ProductType productType
            , @ApiParam(value = SwaggerConstants.PRODUCT_LOCALE_DESCRIPTION, example = "en") @PathVariable("locale") final String locale)
    {

        log.info(productType.toString());
        return ResponseEntity.ok(availableProductsService.getProductsByLocaleAndProductType(productType, locale));

    }

}