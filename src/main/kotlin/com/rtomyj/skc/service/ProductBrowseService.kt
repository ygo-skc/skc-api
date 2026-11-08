package com.rtomyj.skc.service

import com.rtomyj.skc.dao.ProductDao
import com.rtomyj.skc.model.Products
import com.rtomyj.skc.util.Blocking
import com.rtomyj.skc.util.enumeration.ProductType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProductBrowseService
    @Autowired
    constructor(
        @param:Qualifier("product-jdbc") private val productDao: ProductDao,
    ) {
        fun getAllProductsWithLocale(locale: String): Mono<Products> =
            Blocking.mono {
                Products(locale)
                    .apply {
                        products = productDao.getProductsByLocale(locale)
                    }
            }

        fun getProductsUsingLocaleAndProductType(
            productType: ProductType,
            locale: String,
        ): Mono<Products> =
            Blocking.mono {
                productDao
                    .getAllProductsByType(productType, locale)
                    .apply {
                        this.productType = productType
                    }
            }
    }
