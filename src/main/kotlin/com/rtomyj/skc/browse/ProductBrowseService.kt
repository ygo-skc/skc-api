package com.rtomyj.skc.browse

import com.rtomyj.skc.dao.ProductDao
import com.rtomyj.skc.model.Products
import com.rtomyj.skc.util.enumeration.ProductType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ProductBrowseService @Autowired constructor(
  @param:Qualifier("product-jdbc") private val productDao: ProductDao
) {

  fun getAllProductsWithLocale(locale: String): Products {
    val allProductsForLocale = Products(locale)
        .apply {
          products = productDao.getProductsByLocale(locale)
        }

    return allProductsForLocale
  }


  fun getProductsUsingLocaleAndProductType(productType: ProductType, locale: String): Products {
    val products = productDao
        .getAllProductsByType(productType, locale)
        .apply {
          this.productType = productType
        }

    return products
  }
}