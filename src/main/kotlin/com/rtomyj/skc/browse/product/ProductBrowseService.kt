package com.rtomyj.skc.browse.product

import com.rtomyj.skc.browse.product.dao.ProductDao
import com.rtomyj.skc.enums.ProductType
import com.rtomyj.skc.browse.product.model.Products
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ProductBrowseService @Autowired constructor(
    @Qualifier("product-jdbc") private val productDao: ProductDao
) {

    fun getAllProductsWithLocale(locale: String): Products {
        val allProductsForLocale = Products(locale)
            .apply {
                products = productDao.getProductsByLocale(locale)
                setLinks()
            }

        return allProductsForLocale
    }


    fun getProductsUsingLocaleAndProductType(productType: ProductType, locale: String): Products {
        val products = productDao.getAllProductsByType(productType, locale)
            .apply {
                this.productType = productType
                setLinks()
            }

        return products
    }
}