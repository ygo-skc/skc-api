package com.rtomyj.skc.dao;

import com.rtomyj.skc.enums.ProductType;
import com.rtomyj.skc.model.product.Product;
import com.rtomyj.skc.model.product.ProductContent;
import com.rtomyj.skc.model.product.Products;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductDao {
    Product getProductInfo(final String productId, final String locale);

    List<Product> getProductsByLocale(final String locale);

    Set<Product> getProductDetailsForCard(final String cardId);

    Set<ProductContent> getProductContents(final String productId, final String locale);

    Products getAllProductsByType(final ProductType productType, final String locale);

    Map<String, Integer> getProductRarityCount(final String packId);
}
