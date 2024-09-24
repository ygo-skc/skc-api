package com.rtomyj.skc.dao

import com.rtomyj.skc.model.Product
import com.rtomyj.skc.model.ProductContent
import com.rtomyj.skc.model.Products
import com.rtomyj.skc.util.enumeration.ProductType

interface ProductDao {
  fun getProductInfo(productId: String, locale: String): Product
  fun getProductsByLocale(locale: String): List<Product>
  fun getProductDetailsForCard(cardId: String): MutableList<Product>
  fun getProductContents(productId: String, locale: String): Set<ProductContent>
  fun getAllProductsByType(productType: ProductType, locale: String): Products
  fun getProductRarityCount(productId: String): Map<String, Int>
}