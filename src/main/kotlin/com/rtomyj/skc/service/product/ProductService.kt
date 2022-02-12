package com.rtomyj.skc.service.product

import com.rtomyj.skc.dao.ProductDao
import com.rtomyj.skc.enums.ProductType
import com.rtomyj.skc.model.card.MonsterAssociation
import com.rtomyj.skc.model.product.Product
import com.rtomyj.skc.model.product.ProductContent
import com.rtomyj.skc.model.product.Products
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ProductService @Autowired constructor(
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


    fun getSingleProductUsingLocale(productId: String, locale: String): Product {
        val product = productDao.getProductInfo(productId, locale)

        product.productContent.addAll(productDao.getProductContents(productId, locale))
        product.setLinks()

        MonsterAssociation
            .transformMonsterLinkRating(
                product
                    .productContent
                    .stream()
                    .filter {it.card != null}
                    .map { productContent: ProductContent -> productContent.card!! }
                    .toList()
            )
        return product
    }
}