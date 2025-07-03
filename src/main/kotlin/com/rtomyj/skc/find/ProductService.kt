package com.rtomyj.skc.find

import com.rtomyj.skc.dao.ProductDao
import com.rtomyj.skc.model.MonsterAssociation
import com.rtomyj.skc.model.Product
import com.rtomyj.skc.model.ProductContent
import com.rtomyj.skc.skcsuggestionengine.TrafficService
import com.rtomyj.skc.util.enumeration.TrafficResourceType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProductService @Autowired constructor(
  @param:Qualifier("product-jdbc") private val productDao: ProductDao,
  private val trafficService: TrafficService
) {
  fun getSingleProductUsingLocale(productId: String, locale: String, clientIP: String): Mono<Product> = Mono
      .zip(
        Mono.fromCallable { productDao.getProductInfo(productId, locale) },
        Mono.fromCallable { productDao.getProductContents(productId, locale) },
        trafficService.submitTrafficData(TrafficResourceType.PRODUCT, productId, clientIP)
      )
      .map {
        it.t1.productContent.addAll(it.t2)
        MonsterAssociation
            .transformMonsterLinkRating(
              it.t1
                  .productContent
                  .stream()
                  .filter { productContent -> productContent.card != null }
                  .map { productContent: ProductContent -> productContent.card!! }
                  .toList()
            )

        it.t1
      }
}