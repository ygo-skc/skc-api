package com.rtomyj.yugiohAPI.controller.product;

import com.rtomyj.yugiohAPI.controller.YgoApiBaseController;
import com.rtomyj.yugiohAPI.helper.constants.SwaggerConstants;
import com.rtomyj.yugiohAPI.helper.enumeration.products.ProductType;
import com.rtomyj.yugiohAPI.model.product.Product;
import com.rtomyj.yugiohAPI.model.product.Products;
import com.rtomyj.yugiohAPI.service.ProductService;
import io.swagger.annotations.Api;
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
@Api(tags = {SwaggerConstants.SWAGGER_TAG_PRODUCT})
public class ProductsController extends YgoApiBaseController
{

    private static final String END_POINT = BASE_ENDPOINT + "/products";

    private final ProductService availablePacksService;


    @Autowired
    public ProductsController(final HttpServletRequest request, final ProductService availablePacksService)
    {

        this.availablePacksService = availablePacksService;
        this.request = request;

    }


    @GetMapping("/{locale}")
    public ResponseEntity<Products> getProductsByLocale(@PathVariable("locale") final String locale)
    {
        return ResponseEntity.ok(availablePacksService.getProductsByLocale(locale));
    }


    @GetMapping("/{productType}/{locale}")
    public ResponseEntity<Products> getProduct(
            @PathVariable("productType") final ProductType productType
            , @PathVariable("locale") final String locale)
    {
        log.info(productType.toString());
        return ResponseEntity.ok(availablePacksService.getAvailablePacks(productType, locale));
    }

}