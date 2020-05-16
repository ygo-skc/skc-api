package com.rtomyj.yugiohAPI.service;

import com.rtomyj.yugiohAPI.dao.database.Dao;
import com.rtomyj.yugiohAPI.helper.products.ProductType;
import com.rtomyj.yugiohAPI.model.product.Product;
import com.rtomyj.yugiohAPI.model.product.Products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProductService
{

	@Autowired
	@Qualifier("jdbc")
	private Dao dao;

	public Products getAvailablePacks(final ProductType productType, final String locale)
	{
		final Products allPacks = dao.getAllProductsByType(productType, locale);
		allPacks.setLinks();

		return allPacks;
	}



	public Product getPack(final String packId, final String locale)
	{
		final Product pack = dao.getPackContents(packId, locale);
		pack.setLinks();

		return pack;
	}
}