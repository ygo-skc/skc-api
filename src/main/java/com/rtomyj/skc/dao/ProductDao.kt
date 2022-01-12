package com.rtomyj.skc.dao

import com.rtomyj.skc.model.product.ProductContent
import com.rtomyj.skc.enums.ProductType
import com.rtomyj.skc.model.product.Product
import com.rtomyj.skc.model.product.Products

interface ProductDao {
	fun getProductInfo(productId: String, locale: String): Product
	fun getProductsByLocale(locale: String): List<Product>
	fun getProductDetailsForCard(cardId: String): Set<Product>
	fun getProductContents(productId: String, locale: String): Set<ProductContent>
	fun getAllProductsByType(productType: ProductType, locale: String): Products
	fun getProductRarityCount(packId: String): Map<String, Int>
}