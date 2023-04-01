package com.rtomyj.skc.find.product

import com.rtomyj.skc.browse.product.dao.ProductDao
import com.rtomyj.skc.model.MonsterAssociation
import com.rtomyj.skc.model.Product
import com.rtomyj.skc.model.ProductContent
import com.rtomyj.skc.skcsuggestionengine.traffic.TrafficService
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ProductService @Autowired constructor(
	@Qualifier("product-jdbc") private val productDao: ProductDao,
	private val trafficService: TrafficService
) {
	fun getSingleProductUsingLocale(productId: String, locale: String, clientIP: String): Product {
		GlobalScope.launch {
			trafficService.submitTrafficData(TrafficResourceType.PRODUCT, productId, clientIP)
		}

		val product = productDao.getProductInfo(productId, locale)

		product.productContent.addAll(productDao.getProductContents(productId, locale))
		product.setLinks()

		MonsterAssociation
			.transformMonsterLinkRating(
				product
					.productContent
					.stream()
					.filter { it.card != null }
					.map { productContent: ProductContent -> productContent.card!! }
					.toList()
			)
		return product
	}
}