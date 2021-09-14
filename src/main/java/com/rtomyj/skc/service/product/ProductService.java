package com.rtomyj.skc.service.product;

import com.rtomyj.skc.dao.Dao;
import com.rtomyj.skc.enums.products.ProductType;
import com.rtomyj.skc.model.product.Product;
import com.rtomyj.skc.model.product.Products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProductService
{

	@Autowired
	@Qualifier("jdbc")
	private Dao dao;


	public Products getProductsByLocale(final String locale)
	{

		final Products allProductsByLocale = new Products();
		allProductsByLocale.setProducts(dao.getProductsByLocale(locale));
		allProductsByLocale.setLinks();

		return allProductsByLocale;

	}

	public Products getProductsByLocaleAndProductType(final ProductType productType, final String locale)
	{

		final Products products = dao.getAllProductsByType(productType, locale);
		products.setLocale(locale);
		products.setProductType(productType);
		products.setLinks();

		return products;

	}


	public Product getProductByLocale(final String packId, final String locale)
	{

		final Product product = dao.getProductInfo(packId, locale);
		product.getProductContent().addAll(dao.getProductContents(packId, locale));
		product.setLinks();

		return product;

	}

}